package teamproject.bloom.dto.cartitem;

import java.math.BigDecimal;

public record CartItemDto(
        Long id,
        Long wineId,
        String title,
        String variety,
        BigDecimal price,
        int quantity
) {
}
