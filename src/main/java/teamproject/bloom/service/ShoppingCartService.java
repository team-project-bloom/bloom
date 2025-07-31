package teamproject.bloom.service;

import org.springframework.data.domain.Pageable;
import teamproject.bloom.dto.cartitem.CartItemRequestDto;
import teamproject.bloom.dto.cartitem.CartItemUpdateDto;
import teamproject.bloom.dto.shoppingcart.ShoppingCartResponseDto;

public interface ShoppingCartService {
    ShoppingCartResponseDto addItem(CartItemRequestDto addCartItemDto, String userName);

    ShoppingCartResponseDto getAllImages(String userName, Pageable pageable);

    ShoppingCartResponseDto updateCartItem(CartItemUpdateDto updateDto,
                                           Long itemId, String userName);

    void deleteCartItem(Long id, String userName);
}
