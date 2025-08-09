package teamproject.bloom.util;

import java.math.BigDecimal;
import teamproject.bloom.dto.wine.WineResponseDto;
import teamproject.bloom.dto.wine.WineSearchParametersDto;
import teamproject.bloom.model.Grape;
import teamproject.bloom.model.Region;
import teamproject.bloom.model.Wine;

public class WineTestUtil {
    public static Wine createWine(Long id, String name) {
        Wine wine = new Wine();
        wine.setId(id);
        wine.setTitle(name);
        wine.setPrice(BigDecimal.valueOf(123.12));
        wine.setAlcohol(12.5f);
        wine.setVariety(Wine.Variety.RED);
        wine.setValue(Wine.Value.ORGANIC);
        wine.setVintage(2021);
        wine.setGrape(createGrape(1L, "Grape1"));
        wine.setRegion(createRegion(1L, "Region1"));
        wine.setDescription("Good wine!");
        return wine;
    }

    public static WineSearchParametersDto createParamDto() {
        return new WineSearchParametersDto(
                new String[1],
                BigDecimal.valueOf(1.1),
                BigDecimal.valueOf(2.2),
                new Float[1],
                new Wine.Variety[1],
                new Wine.Value[1],
                2021,
                2022,
                new String[1],
                new String[1]
        );
    }

    public static Grape createGrape(Long id, String name) {
        Grape grape = new Grape();
        grape.setId(id);
        grape.setName(name);
        return grape;
    }

    public static Region createRegion(Long id, String name) {
        Region region = new Region();
        region.setId(id);
        region.setName(name);
        return region;
    }

    public static WineResponseDto mapWineToWineResponseDto(Wine wine) {
        return new WineResponseDto(
                wine.getId(),
                wine.getTitle(),
                wine.getPrice(),
                wine.getAlcohol(),
                wine.getVariety(),
                wine.getValue(),
                wine.getVintage(),
                wine.getGrape().getName(),
                wine.getRegion().getName(),
                wine.getDescription()
        );
    }
}
