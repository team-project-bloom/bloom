package teamproject.bloom.dto.cartitem;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;

public record CartItemRequestDto(
        @Positive
        @NotNull
        Long wineId,
        @Positive
        int quantity
) {
}
