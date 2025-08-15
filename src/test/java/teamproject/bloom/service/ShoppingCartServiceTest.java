package teamproject.bloom.service;

import static org.junit.Assert.assertThrows;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.when;
import static teamproject.bloom.util.ShoppingCartTestUtil.cartItem;
import static teamproject.bloom.util.ShoppingCartTestUtil.cartItemRequestDto;
import static teamproject.bloom.util.ShoppingCartTestUtil.createEmptyShoppingCart;
import static teamproject.bloom.util.ShoppingCartTestUtil.createShoppingCart;
import static teamproject.bloom.util.ShoppingCartTestUtil.mapCartToCartDto;
import static teamproject.bloom.util.ShoppingCartTestUtil.user;
import static teamproject.bloom.util.WineTestUtil.createWine;

import java.util.Optional;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import teamproject.bloom.dto.cartitem.CartItemRequestDto;
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
        User user = user(1L);
        Wine wine = createWine(1L, "Wine");
        int quantity = 3;
        ShoppingCart cart = createShoppingCart(1L, wine, quantity);
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
        User user = user(1L);
        Wine wine = createWine(1L, "Wine");
        int quantity = 3;
        ShoppingCart cart = createEmptyShoppingCart(3L);
        CartItemRequestDto cartItemRequestDto = cartItemRequestDto(wine.getId(), quantity);
        CartItem cartItem = cartItem(1L, wine, quantity);
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
    public void addItem_IncorrectDataUserId_ReturnStatus() {
        User user = user(544L);
        Wine wine = createWine(1L, "Wine");
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
    public void addItem_IncorrectDataWineId_ReturnStatus() {
        User user = user(544L);
        Wine wine = createWine(1L, "Wine");
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
        User user = user(1L);
        Wine wine = createWine(1L, "Wine");
        ShoppingCart cart = createShoppingCart(1L, wine, 3);
        ShoppingCartResponseDto expected = mapCartToCartDto(cart);

        when(userRepository.findByUserName(user.getUserName())).thenReturn(Optional.of(user));
        when(shoppingCartRepository.findByUserId(cart.getId())).thenReturn(cart);
        when(shoppingCartMapper.toDto(cart)).thenReturn(expected);
        ShoppingCartResponseDto actual = shoppingCartService.getAllImages(user.getUserName());

        assertEquals(expected, actual);
    }
}
