package teamproject.bloom.util;

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
}
