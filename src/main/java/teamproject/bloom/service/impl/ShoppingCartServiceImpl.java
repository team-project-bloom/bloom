package teamproject.bloom.service.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import teamproject.bloom.dto.cartitem.CartItemRequestDto;
import teamproject.bloom.dto.cartitem.CartItemUpdateDto;
import teamproject.bloom.dto.shoppingcart.ShoppingCartResponseDto;
import teamproject.bloom.exception.unchecked.EntityNotFoundException;
import teamproject.bloom.mapper.CartItemMapper;
import teamproject.bloom.mapper.ShoppingCartMapper;
import teamproject.bloom.model.CartItem;
import teamproject.bloom.model.ShoppingCart;
import teamproject.bloom.model.User;
import teamproject.bloom.model.Wine;
import teamproject.bloom.repository.cartitem.CartItemRepository;
import teamproject.bloom.repository.shoppingcart.ShoppingCartRepository;
import teamproject.bloom.repository.user.UserRepository;
import teamproject.bloom.repository.wine.WineRepository;
import teamproject.bloom.service.ShoppingCartService;

@Service
@RequiredArgsConstructor
@Transactional
public class ShoppingCartServiceImpl implements ShoppingCartService {
    private final UserRepository userRepository;
    private final WineRepository wineRepository;
    private final ShoppingCartRepository shoppingCartRepository;
    private final ShoppingCartMapper shoppingCartMapper;
    private final CartItemMapper cartItemMapper;
    private final CartItemRepository cartItemRepository;

    @Override
    public ShoppingCartResponseDto addItem(CartItemRequestDto cartItemDto, String userName) {
        User user = getUserByName(userName);
        Wine wine = wineRepository.findById(cartItemDto.wineId()).orElseThrow(
                () -> new EntityNotFoundException("Can`t find wine by id "
                        + cartItemDto.wineId())
        );
        ShoppingCart cart = shoppingCartRepository.findByUserId(user.getId());
        cart.getCartItems().stream()
                .filter(item -> item.getWine().getId().equals(cartItemDto.wineId()))
                .findFirst()
                .ifPresentOrElse(item -> item.setQuantity(item.getQuantity()
                                + cartItemDto.quantity()),
                        () -> addCartItemToCart(cartItemDto, wine, cart));
        return shoppingCartMapper.toDto(shoppingCartRepository.save(cart));
    }

    @Override
    public ShoppingCartResponseDto getAllImages(String userName) {
        User user = getUserByName(userName);
        ShoppingCart cart = shoppingCartRepository.findByUserId(user.getId());
        return shoppingCartMapper.toDto(cart);
    }

    @Override
    public ShoppingCartResponseDto updateCartItem(CartItemUpdateDto updateDto,
                                                  Long itemId, String userName) {
        User user = getUserByName(userName);
        ShoppingCart cart = shoppingCartRepository.findByUserId(user.getId());
        CartItem cartItem = cartItemRepository.findByIdAndShoppingCartId(itemId, cart.getId())
                .map(item -> {
                    item.setQuantity(updateDto.quantity());
                    return item;
                }).orElseThrow(
                        () -> new EntityNotFoundException(
                                String.format(
                                        "Can`t find CartItem by id %s and ShoppingCart id %s",
                                        itemId, cart.getId())));
        cartItemRepository.save(cartItem);
        return shoppingCartMapper.toDto(cart);
    }

    @Override
    public void deleteCartItem(Long id, String userName) {
        User user = getUserByName(userName);
        ShoppingCart cart = shoppingCartRepository.findByUserId(user.getId());
        CartItem cartItem = cart.getCartItems()
                .stream()
                .filter(item -> item.getId().equals(id))
                .findFirst()
                .orElseThrow(() -> new EntityNotFoundException("Can`t find CartItem by id " + id));
        cart.getCartItems().remove(cartItem);
    }

    @Override
    public void createShoppingCart(User user) {
        ShoppingCart shoppingCart = new ShoppingCart();
        shoppingCart.setUser(user);
        shoppingCartRepository.save(shoppingCart);
    }

    private User getUserByName(String userName) {
        return userRepository.findByUserName(userName).orElseThrow(
                () -> new EntityNotFoundException("Can`t find user by name " + userName)
        );
    }

    private void addCartItemToCart(
            CartItemRequestDto itemDto, Wine wine, ShoppingCart cart) {
        CartItem cartItem = cartItemMapper.toModel(itemDto);
        cartItem.setWine(wine);
        cart.addItemToCart(cartItem);
    }
}
