package teamproject.bloom.dto.favoriteitem;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;

public record FavoriteWineRequestDto(
        @Positive
        @NotNull
        Long wineId
) {
}
