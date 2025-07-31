package teamproject.bloom.service;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import teamproject.bloom.dto.cartitem.CartItemDto;
import teamproject.bloom.dto.cartitem.CartItemRequestDto;
import teamproject.bloom.dto.shoppingcart.ShoppingCartResponseDto;

public interface ShoppingCartService {
    ShoppingCartResponseDto addItem(CartItemRequestDto addCartItemDto, String userName);

    Page<CartItemDto> getAllImages(String userName, Pageable pageable);
}
