package teamproject.bloom.dto.wine;

import java.math.BigDecimal;
import teamproject.bloom.model.Wine;

public record WineResponseDto(
        Long id,
        String title,
        BigDecimal price,
        String region,
        Wine.Variety variety,
        Wine.Value value
) {
}
