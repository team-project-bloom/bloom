package teamproject.bloom.util;

import java.util.Set;
import teamproject.bloom.model.FavoriteItem;
import teamproject.bloom.model.User;

public class UserTestUtil {
    public static User createUser(Long id, String userName) {
        User user = new User();
        user.setId(id);
        user.setUserName(userName);
        user.setFavorites(Set.of(new FavoriteItem()));
        return user;
    }
}
