package teamproject.bloom.dto.shoppingcart;

import java.util.Set;
import teamproject.bloom.dto.cartitem.CartItemDto;

public record ShoppingCartResponseDto(
        Long id,
        Long userId,
        Set<CartItemDto> itemCartDtos
) {
}
