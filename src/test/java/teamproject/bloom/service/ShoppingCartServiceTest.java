package teamproject.bloom.service;

import static org.junit.Assert.assertThrows;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static teamproject.bloom.util.ShoppingCartTestUtil.cartItem;
import static teamproject.bloom.util.ShoppingCartTestUtil.cartItemRequestDto;
import static teamproject.bloom.util.ShoppingCartTestUtil.cartItemUpdateDto;
import static teamproject.bloom.util.ShoppingCartTestUtil.emptyShoppingCart;
import static teamproject.bloom.util.ShoppingCartTestUtil.mapCartToCartDto;
import static teamproject.bloom.util.ShoppingCartTestUtil.shoppingCart;
import static teamproject.bloom.util.UserTestUtil.user;
import static teamproject.bloom.util.WineTestUtil.wine;

import java.util.Optional;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
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
import teamproject.bloom.service.impl.ShoppingCartServiceImpl;

@ExtendWith(MockitoExtension.class)
public class ShoppingCartServiceTest {
    @InjectMocks
    private ShoppingCartServiceImpl shoppingCartService;
    @Mock
    private UserRepository userRepository;
    @Mock
    private WineRepository wineRepository;
    @Mock
    private ShoppingCartRepository shoppingCartRepository;
    @Mock
    private ShoppingCartMapper shoppingCartMapper;
    @Mock
    private CartItemRepository cartItemRepository;
    @Mock
    private CartItemMapper cartItemMapper;

    @Test
    @DisplayName("Verify method addItem with correct data")
    public void addItem_CorrectData_ReturnDto() {
        User user = user(1L, "userName");
        Wine wine = wine(1L, "Wine");
        int quantity = 3;
        ShoppingCart cart = shoppingCart(1L, wine, quantity, user);
        CartItemRequestDto cartItemRequestDto = cartItemRequestDto(wine.getId(), quantity);
        ShoppingCartResponseDto expected = mapCartToCartDto(cart);

        when(userRepository.findByUserName(user.getUserName())).thenReturn(Optional.of(user));
        when(wineRepository.findById(wine.getId())).thenReturn(Optional.of(wine));
        when(shoppingCartRepository.findByUserId(user.getId())).thenReturn(cart);
        when(shoppingCartRepository.save(cart)).thenReturn(cart);
        when(shoppingCartMapper.toDto(cart)).thenReturn(expected);
        ShoppingCartResponseDto actual = shoppingCartService.addItem(
                cartItemRequestDto, user.getUserName());

        assertEquals(expected, actual);
    }

    @Test
    @DisplayName("Verify method addItem with correct data")
    public void addItem_CorrectDataEmptyCart_ReturnDto() {
        User user = user(1L, "userName");
        Wine wine = wine(1L, "Wine");
        int quantity = 3;
        ShoppingCart cart = emptyShoppingCart(3L, user);
        CartItemRequestDto cartItemRequestDto = cartItemRequestDto(wine.getId(), quantity);
        CartItem cartItem = cartItem(1L, wine, quantity, cart);
        ShoppingCartResponseDto expected = mapCartToCartDto(cart);

        when(userRepository.findByUserName(user.getUserName())).thenReturn(Optional.of(user));
        when(wineRepository.findById(wine.getId())).thenReturn(Optional.of(wine));
        when(shoppingCartRepository.findByUserId(user.getId())).thenReturn(cart);
        when(cartItemMapper.toModel(cartItemRequestDto)).thenReturn(cartItem);
        when(shoppingCartRepository.save(cart)).thenReturn(cart);
        when(shoppingCartMapper.toDto(cart)).thenReturn(expected);
        ShoppingCartResponseDto actual = shoppingCartService.addItem(
                cartItemRequestDto, user.getUserName());

        assertEquals(expected, actual);
    }

    @Test
    @DisplayName("""
            Verify method addItem with incorrect data.
             A user by id isn`t exist
            """)
    public void addItem_IncorrectDataUserId_ReturnException() {
        User user = user(544L, "userName");
        Wine wine = wine(1L, "Wine");
        int quantity = 3;
        CartItemRequestDto cartItemRequestDto = cartItemRequestDto(wine.getId(), quantity);

        when(userRepository.findByUserName(user.getUserName())).thenReturn(Optional.empty());
        Exception actual = assertThrows(EntityNotFoundException.class,
                () -> shoppingCartService.addItem(cartItemRequestDto, user.getUserName()));

        String expected = "Can`t find user by name " + user.getUserName();
        assertEquals(expected, actual.getMessage());
    }

    @Test
    @DisplayName("""
            Verify method addItem with incorrect data.
             A wine by id isn`t exist
            """)
    public void addItem_IncorrectDataWineId_ReturnException() {
        User user = user(544L, "userName");
        Wine wine = wine(1L, "Wine");
        int quantity = 3;
        CartItemRequestDto cartItemRequestDto = cartItemRequestDto(wine.getId(), quantity);

        when(userRepository.findByUserName(user.getUserName())).thenReturn(Optional.of(user));
        when(wineRepository.findById(wine.getId())).thenReturn(Optional.empty());
        Exception actual = assertThrows(EntityNotFoundException.class,
                () -> shoppingCartService.addItem(cartItemRequestDto, user.getUserName()));

        String expected = "Can`t find wine by id " + wine.getId();
        assertEquals(expected, actual.getMessage());
    }

    @Test
    @DisplayName("Verify method getAllImages with correct data")
    public void getAllImages_CorrectData_ReturnDto() {
        User user = user(1L, "userName");
        Wine wine = wine(1L, "Wine");
        ShoppingCart cart = shoppingCart(1L, wine, 3, user);
        ShoppingCartResponseDto expected = mapCartToCartDto(cart);

        when(userRepository.findByUserName(user.getUserName())).thenReturn(Optional.of(user));
        when(shoppingCartRepository.findByUserId(cart.getId())).thenReturn(cart);
        when(shoppingCartMapper.toDto(cart)).thenReturn(expected);
        ShoppingCartResponseDto actual = shoppingCartService.getAllImages(user.getUserName());

        assertEquals(expected, actual);
    }

    @Test
    @DisplayName("Verify method updateCartItem with correct data")
    public void updateCartItem_CorrectData_ReturnDto() {
        User user = user(1L, "userName");
        Wine wine = wine(1L, "Wine");
        int quantity = 2;
        ShoppingCart cart = shoppingCart(1L, wine, quantity, user);
        CartItem cartItem = cartItem(1L, wine, quantity, cart);
        CartItemUpdateDto cartItemUpdateDto = cartItemUpdateDto(3);
        ShoppingCartResponseDto expected = mapCartToCartDto(cart);

        when(userRepository.findByUserName(user.getUserName())).thenReturn(Optional.of(user));
        when(shoppingCartRepository.findByUserId(user.getId())).thenReturn(cart);
        when(cartItemRepository.findByIdAndShoppingCartId(cartItem.getId(), cart.getId()))
                .thenReturn(Optional.of(cartItem));
        when(cartItemRepository.save(cartItem)).thenReturn(cartItem);
        when(shoppingCartMapper.toDto(cart)).thenReturn(expected);
        ShoppingCartResponseDto actual = shoppingCartService.updateCartItem(
                cartItemUpdateDto, cartItem.getId(), user.getUserName());

        assertEquals(expected, actual);
    }

    @Test
    @DisplayName("""
            Verify method updateCartItem with incorrect data.
             A cartItem by id isn`t exist
            """)
    public void updateCartItem_IncorrectData_ReturnException() {
        User user = user(1L, "userName");
        Wine wine = wine(1L, "Wine");
        int quantity = 2;
        ShoppingCart cart = shoppingCart(1L, wine, quantity, user);
        CartItem cartItem = cartItem(1L, wine, quantity, cart);
        CartItemUpdateDto cartItemUpdateDto = cartItemUpdateDto(3);

        when(userRepository.findByUserName(user.getUserName())).thenReturn(Optional.of(user));
        when(shoppingCartRepository.findByUserId(user.getId())).thenReturn(cart);
        when(cartItemRepository.findByIdAndShoppingCartId(cartItem.getId(), cart.getId()))
                .thenReturn(Optional.empty());
        Exception actual = assertThrows(EntityNotFoundException.class,
                () -> shoppingCartService.updateCartItem(
                        cartItemUpdateDto, cartItem.getId(), user.getUserName()));

        String expected = String.format(
                "Can`t find CartItem by id %s and ShoppingCart id %s",
                cartItem.getId(), cart.getId());
        assertEquals(expected, actual.getMessage());
    }

    @Test
    @DisplayName("Verify method deleteCartItem with correct data")
    public void deleteCartItem_CorrectData_Void() {
        User user = user(1L, "userName");
        Wine wine = wine(1L, "Wine");
        int quantity = 2;
        ShoppingCart cart = shoppingCart(1L, wine, quantity, user);

        when(userRepository.findByUserName(user.getUserName())).thenReturn(Optional.of(user));
        when(shoppingCartRepository.findByUserId(user.getId())).thenReturn(cart);
        shoppingCartService.deleteCartItem(1L, user.getUserName());

        verify(userRepository, times(1)).findByUserName(user.getUserName());
        verify(shoppingCartRepository, times(1)).findByUserId(user.getId());
    }

    @Test
    @DisplayName("""
            Verify method deleteCartItem with incorrect data.
             A cartItem by id isn`t exist
            """)
    public void deleteCartItem_IncorrectData_ReturnException() {
        User user = user(1L, "userName");
        long itemId = 15L;
        ShoppingCart cart = emptyShoppingCart(1L, user);

        when(userRepository.findByUserName(user.getUserName())).thenReturn(Optional.of(user));
        when(shoppingCartRepository.findByUserId(user.getId())).thenReturn(cart);
        Exception actual = assertThrows(EntityNotFoundException.class,
                () -> shoppingCartService.deleteCartItem(itemId, user.getUserName()));

        String expected = "Can`t find CartItem by id " + itemId;
        assertEquals(expected, actual.getMessage());
    }

    @Test
    @DisplayName("Verify method createShoppingCart with correct data")
    public void createShoppingCart_CorrectData_Void() {
        User user = user(1L, "userName");

        when(shoppingCartRepository.save(any(ShoppingCart.class)))
                .thenReturn(any(ShoppingCart.class));
        shoppingCartService.createShoppingCart(user);

        verify(shoppingCartRepository, times(1))
                .save(any(ShoppingCart.class));
    }
}
