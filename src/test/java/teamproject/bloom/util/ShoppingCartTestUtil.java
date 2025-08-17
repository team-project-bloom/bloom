package teamproject.bloom.util;

import static teamproject.bloom.util.UserTestUtil.user;

import java.util.HashSet;
import java.util.Set;
import java.util.stream.Collectors;
import teamproject.bloom.dto.cartitem.CartItemDto;
import teamproject.bloom.dto.cartitem.CartItemRequestDto;
import teamproject.bloom.dto.cartitem.CartItemUpdateDto;
import teamproject.bloom.dto.shoppingcart.ShoppingCartResponseDto;
import teamproject.bloom.model.CartItem;
import teamproject.bloom.model.ShoppingCart;
import teamproject.bloom.model.Wine;

public class ShoppingCartTestUtil {
    public static ShoppingCart shoppingCart(Long id, Wine wine, int quantity) {
        ShoppingCart cart = new ShoppingCart();
        cart.setId(id);
        cart.setUser(user(id));
        cart.setCartItems(new HashSet<>());

        CartItem cartItem = cartItem(1L, wine, quantity, cart);
        cart.getCartItems().add(cartItem);
        return cart;
    }

    public static ShoppingCart emptyShoppingCart(Long id) {
        ShoppingCart shoppingCart = new ShoppingCart();
        shoppingCart.setId(id);
        shoppingCart.setUser(user(1L));
        shoppingCart.setCartItems(new HashSet<>());
        return shoppingCart;
    }

    public static CartItem cartItem(Long id, Wine wine, int quantity, ShoppingCart cart) {
        CartItem cartItem = new CartItem();
        cartItem.setId(id);
        cartItem.setShoppingCart(cart);
        cartItem.setWine(wine);
        cartItem.setQuantity(quantity);
        return cartItem;
    }

    public static CartItemUpdateDto cartItemUpdateDto() {
        return new CartItemUpdateDto(
                3
        );
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
