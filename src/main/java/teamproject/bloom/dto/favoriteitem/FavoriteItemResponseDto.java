package teamproject.bloom.dto.favoriteitem;

import java.math.BigDecimal;
import teamproject.bloom.model.Wine;

public record FavoriteItemResponseDto(
        Long id,
        Long userId,
        Long wineId,
        String title,
        BigDecimal price,
        Wine.Variety variety,
        Wine.Value value,
        String region,
        String grape
) {
}
