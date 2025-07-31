package teamproject.bloom.dto.wine;

import java.math.BigDecimal;
import teamproject.bloom.model.Wine;

public record WineResponseWithAllParamsDto(
        Long id,
        String title,
        BigDecimal price,
        Float alcohol,
        Wine.Variety variety,
        Wine.Value value,
        Integer vintage,
        String imgUrl,
        Long grapeId,
        Long regionId,
        String description
) {
}
