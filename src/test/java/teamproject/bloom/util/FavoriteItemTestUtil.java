package teamproject.bloom.util;

import static teamproject.bloom.model.Wine.Value.NON_ORGANIC;
import static teamproject.bloom.model.Wine.Value.VEGAN;
import static teamproject.bloom.model.Wine.Variety.ORANGE;
import static teamproject.bloom.model.Wine.Variety.RED;

import java.math.BigDecimal;
import java.util.List;
import teamproject.bloom.dto.favoriteitem.FavoriteItemResponseDto;
import teamproject.bloom.dto.favoriteitem.FavoriteWineRequestDto;
import teamproject.bloom.model.FavoriteItem;
import teamproject.bloom.model.User;
import teamproject.bloom.model.Wine;

public class FavoriteItemTestUtil {
    public static FavoriteItem favoriteItem(Long id, Wine wine, User user) {
        FavoriteItem favoriteItem = new FavoriteItem();
        favoriteItem.setId(id);
        favoriteItem.setWine(wine);
        favoriteItem.setUser(user);
        return favoriteItem;
    }

    public static FavoriteWineRequestDto favoriteWineRequestDto(Long wineId) {
        return new FavoriteWineRequestDto(
                wineId
        );
    }

    public static FavoriteItemResponseDto mapFavoriteItemToDto(FavoriteItem favoriteItem) {
        return new FavoriteItemResponseDto(
                favoriteItem.getId(),
                favoriteItem.getUser().getId(),
                favoriteItem.getWine().getId(),
                favoriteItem.getWine().getTitle(),
                favoriteItem.getWine().getPrice(),
                favoriteItem.getWine().getVariety(),
                favoriteItem.getWine().getValue(),
                favoriteItem.getWine().getRegion().getName(),
                favoriteItem.getWine().getGrape().getName()
        );
    }

    public static List<FavoriteItemResponseDto> listFavoriteItemResponseDtos() {
        FavoriteItemResponseDto dto1 = new FavoriteItemResponseDto(
                1L,
                1L,
                1L,
                "Wine1",
                BigDecimal.valueOf(11.11),
                RED,
                NON_ORGANIC,
                "Region1",
                "Grape1"
        );
        FavoriteItemResponseDto dto2 = new FavoriteItemResponseDto(
                2L,
                1L,
                2L,
                "Wine2",
                BigDecimal.valueOf(22.22),
                ORANGE,
                VEGAN,
                "Region2",
                "Grape2"
        );
        return List.of(dto1, dto2);
    }

    public static FavoriteItemResponseDto favoriteItemResponseDto() {
        return new FavoriteItemResponseDto(
                2L,
                1L,
                2L,
                "Wine2",
                BigDecimal.valueOf(22.22),
                ORANGE,
                VEGAN,
                "Region2",
                "Grape2"
        );
    }
}
