package teamproject.bloom.dto.wine;

import java.math.BigDecimal;
import teamproject.bloom.model.Wine;

public record WineResponseDto(
        Long id,
        String title,
        BigDecimal price,
        Long regionId,
        Wine.Variety variety,
        Wine.Value value,
        String imgUrl
) {
}
