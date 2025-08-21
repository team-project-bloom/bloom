package teamproject.bloom.dto.wine;

import java.math.BigDecimal;
import teamproject.bloom.model.Wine;

public record WineSearchParametersDto(
        String[] title,
        BigDecimal priceFrom,
        BigDecimal priceTo,
        Float[] alcohol,
        Wine.Variety[] variety,
        Wine.Value[] value,
        Integer vintageFrom,
        Integer vintageTo,
        String[] grape,
        String[] region
) {
}
