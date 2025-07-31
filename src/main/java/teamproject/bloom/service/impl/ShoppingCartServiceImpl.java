package teamproject.bloom.service.impl;

import java.util.Optional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import teamproject.bloom.dto.cartitem.CartItemRequestDto;
import teamproject.bloom.dto.shoppingcart.ShoppingCartDto;
import teamproject.bloom.exception.EntityNotFoundException;
import teamproject.bloom.mapper.CartItemMapper;
import teamproject.bloom.mapper.ShoppingCartMapper;
import teamproject.bloom.model.CartItem;
import teamproject.bloom.model.ShoppingCart;
import teamproject.bloom.model.User;
import teamproject.bloom.model.Wine;
import teamproject.bloom.repository.ShoppingCartRepository;
import teamproject.bloom.repository.UserRepository;
import teamproject.bloom.repository.WineRepository;
import teamproject.bloom.service.ShoppingCartService;
import teamproject.bloom.service.UserService;

@Service
@RequiredArgsConstructor
@Transactional
public class ShoppingCartServiceImpl implements ShoppingCartService {
    private final UserRepository userRepository;
    private final UserService userService;
    private final WineRepository wineRepository;
    private final ShoppingCartRepository shoppingCartRepository;
    private final ShoppingCartMapper shoppingCartMapper;
    private final CartItemMapper cartItemMapper;

    @Override
    public ShoppingCartDto addItem(CartItemRequestDto cartItemDto, String userName) {
        Optional<User> userFronDb = userRepository.findByUserName(userName);
        User user = userFronDb.orElseGet(() -> userService.saveUser(userName));
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

    private void addCartItemToCart(
            CartItemRequestDto itemDto, Wine wine, ShoppingCart cart) {
        CartItem cartItem = cartItemMapper.toModel(itemDto);
        cartItem.setWine(wine);
        cart.addItemToCart(cartItem);
    }
}
