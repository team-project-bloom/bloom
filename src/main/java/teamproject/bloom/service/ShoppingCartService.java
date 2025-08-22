package teamproject.bloom.service;

import teamproject.bloom.dto.cartitem.CartItemRequestDto;
import teamproject.bloom.dto.cartitem.CartItemUpdateDto;
import teamproject.bloom.dto.shoppingcart.ShoppingCartResponseDto;
import teamproject.bloom.model.User;

public interface ShoppingCartService {
    ShoppingCartResponseDto addItem(CartItemRequestDto addCartItemDto, String userName);

    ShoppingCartResponseDto getAllImages(String userName);

    ShoppingCartResponseDto updateCartItem(CartItemUpdateDto updateDto,
                                           Long itemId, String userName);

    void deleteCartItem(Long id, String userName);

    void createShoppingCart(User user);
}
