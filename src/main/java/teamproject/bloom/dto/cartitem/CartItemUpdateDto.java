package teamproject.bloom.dto.cartitem;

import jakarta.validation.constraints.Positive;

public record CartItemUpdateDto(
        @Positive
        int quantity
) {
}
