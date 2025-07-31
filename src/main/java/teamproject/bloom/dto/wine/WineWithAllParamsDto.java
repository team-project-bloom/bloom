package teamproject.bloom.dto.wine;

import java.math.BigDecimal;
import teamproject.bloom.model.Wine;

public record WineWithAllParamsDto(
        Long id,
        String title,
        BigDecimal price,
        Float alcohol,
        Wine.Variety variety,
        Wine.Value value,
        Integer vintage,
        String img,
        Long grapeId,
        Long regionId,
        String description
) {
}
