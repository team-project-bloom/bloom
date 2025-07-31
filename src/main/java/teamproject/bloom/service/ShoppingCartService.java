package teamproject.bloom.service;

import teamproject.bloom.dto.cartitem.CartItemRequestDto;
import teamproject.bloom.dto.shoppingcart.ShoppingCartDto;

public interface ShoppingCartService {
    ShoppingCartDto addItem(CartItemRequestDto addCartItemDto, String userName);
}
