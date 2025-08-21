package teamproject.bloom.util;

import static teamproject.bloom.model.Wine.Value.NON_ORGANIC;
import static teamproject.bloom.model.Wine.Value.ORGANIC;
import static teamproject.bloom.model.Wine.Value.VEGAN;
import static teamproject.bloom.model.Wine.Variety.ORANGE;
import static teamproject.bloom.model.Wine.Variety.RED;
import static teamproject.bloom.model.Wine.Variety.ROSE;

import java.math.BigDecimal;
import java.util.List;
import teamproject.bloom.dto.wine.WineResponseDto;
import teamproject.bloom.dto.wine.WineSearchParametersDto;
import teamproject.bloom.model.Grape;
import teamproject.bloom.model.Region;
import teamproject.bloom.model.Wine;

public class WineTestUtil {
    public static Wine wine(Long id, String name) {
        Wine wine = new Wine();
        wine.setId(id);
        wine.setTitle(name);
        wine.setPrice(BigDecimal.valueOf(123.12));
        wine.setAlcohol(12.5f);
        wine.setVariety(RED);
        wine.setValue(ORGANIC);
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

    public static WineSearchParametersDto oneParamDto() {
        return new WineSearchParametersDto.Builder().title("2").build();
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

    public static WineResponseDto wineResponseDto() {
        return new WineResponseDto(
                2L,
                "Wine2",
                BigDecimal.valueOf(22.22),
                2.20f,
                ORANGE,
                VEGAN,
                2020,
                "Grape2",
                "Region2",
                "Good wine 2"
        );
    }

    public static List<WineResponseDto> listWineResponseDtos() {
        WineResponseDto dto1 = new WineResponseDto(
                1L,
                "Wine1",
                BigDecimal.valueOf(11.11),
                1.10f,
                RED,
                NON_ORGANIC,
                2024,
                "Grape1",
                "Region1",
                "Good wine 1"
        );
        WineResponseDto dto2 = new WineResponseDto(
                2L,
                "Wine2",
                BigDecimal.valueOf(22.22),
                2.20f,
                ORANGE,
                VEGAN,
                2020,
                "Grape2",
                "Region2",
                "Good wine 2"
        );
        WineResponseDto dto3 = new WineResponseDto(
                3L,
                "Wine3",
                BigDecimal.valueOf(33.33),
                3.30f,
                ROSE,
                ORGANIC,
                2021,
                "Grape3",
                "Region3",
                "Good wine 3"
        );
        return List.of(dto1, dto2, dto3);
    }
}
