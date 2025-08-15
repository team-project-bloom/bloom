package teamproject.bloom.util;

import static teamproject.bloom.util.UserTestUtil.createUser;
import static teamproject.bloom.util.WineTestUtil.createWine;

import teamproject.bloom.model.FavoriteItem;

public class FavoriteItemTestUtil {
    public static FavoriteItem createFavoriteItem(Long id) {
        FavoriteItem favoriteItem = new FavoriteItem();
        favoriteItem.setId(id);
        favoriteItem.setWine(createWine(1L, "Wine"));
        favoriteItem.setUser(createUser(1L, "User"));
        return favoriteItem;
    }
}
