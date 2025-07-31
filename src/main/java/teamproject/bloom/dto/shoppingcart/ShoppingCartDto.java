package teamproject.bloom.dto.shoppingcart;

import java.util.Set;
import teamproject.bloom.dto.cartitem.CartItemDto;

public record ShoppingCartDto(
        Long id,
        Long userId,
        Set<CartItemDto> itemCartDtos
) {
}
