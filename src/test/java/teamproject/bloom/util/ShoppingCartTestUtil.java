package teamproject.bloom.util;

import java.util.HashSet;
import java.util.Set;
import java.util.stream.Collectors;
import teamproject.bloom.dto.cartitem.CartItemDto;
import teamproject.bloom.dto.cartitem.CartItemRequestDto;
import teamproject.bloom.dto.shoppingcart.ShoppingCartResponseDto;
import teamproject.bloom.model.CartItem;
import teamproject.bloom.model.FavoriteItem;
import teamproject.bloom.model.ShoppingCart;
import teamproject.bloom.model.User;
import teamproject.bloom.model.Wine;

public class ShoppingCartTestUtil {
    public static ShoppingCart createShoppingCart(Long id, Wine wine, int quantity) {
        ShoppingCart shoppingCart = new ShoppingCart();
        shoppingCart.setId(id);
        shoppingCart.setUser(user(id));
        shoppingCart.setCartItems(Set.of(cartItem(1L, wine, quantity)));
        return shoppingCart;
    }

    public static ShoppingCart createEmptyShoppingCart(Long id) {
        ShoppingCart shoppingCart = new ShoppingCart();
        shoppingCart.setId(id);
        shoppingCart.setUser(user(1L));
        shoppingCart.setCartItems(new HashSet<>());
        return shoppingCart;
    }

    public static User user(Long id) {
        User user = new User();
        user.setId(id);
        user.setUserName("userName");
        user.setFavorites(Set.of(new FavoriteItem()));
        return user;
    }

    public static CartItem cartItem(Long id, Wine wine, int quantity) {
        CartItem cartItem = new CartItem();
        cartItem.setId(id);
        cartItem.setShoppingCart(new ShoppingCart());
        cartItem.setWine(wine);
        cartItem.setQuantity(quantity);
        return cartItem;
    }

    public static CartItemRequestDto cartItemRequestDto(Long wineId, int quantity) {
        return new CartItemRequestDto(
                wineId,
                quantity
        );
    }

    public static Set<CartItemDto> mapCartItemToCartItemDto(Set<CartItem> items) {
        return items.stream()
                .map(item -> new CartItemDto(
                        item.getId(),
                        item.getWine().getId(),
                        item.getWine().getTitle(),
                        item.getWine().getVariety().name(),
                        item.getWine().getPrice(),
                        item.getQuantity()
                ))
                .collect(Collectors.toSet());

    }

    public static ShoppingCartResponseDto mapCartToCartDto(ShoppingCart cart) {
        return new ShoppingCartResponseDto(
                cart.getId(),
                cart.getUser().getId(),
                mapCartItemToCartItemDto(cart.getCartItems())
        );
    }
}
