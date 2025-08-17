package teamproject.bloom.util;

import static teamproject.bloom.util.FavoriteItemTestUtil.favoriteItem;

import java.util.HashSet;
import teamproject.bloom.model.FavoriteItem;
import teamproject.bloom.model.User;
import teamproject.bloom.model.Wine;

public class UserTestUtil {
    public static User user(Long id) {
        User user = new User();
        user.setId(id);
        user.setUserName("userName");
        user.setFavorites(new HashSet<>());
        return user;
    }

    public static User userWithFavorite(Long id, Wine wine, Long itemId) {
        User user = new User();
        user.setId(id);
        user.setUserName("userName");
        user.setFavorites(new HashSet<>());

        FavoriteItem favoriteItem = favoriteItem(itemId, wine, user);
        user.getFavorites().add(favoriteItem);
        return user;
    }
}
