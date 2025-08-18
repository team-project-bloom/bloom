package teamproject.bloom.util;

import static teamproject.bloom.security.JwtAnonymousFilter.roles;
import static teamproject.bloom.util.FavoriteItemTestUtil.favoriteItem;

import java.util.HashSet;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import teamproject.bloom.dto.user.UserLoginResponseDto;
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

    public static UserLoginResponseDto mapTokenToUserLoginResponseDto(String token) {
        return new UserLoginResponseDto(
                token
        );
    }

    public static Authentication authentication(User user) {
        return new UsernamePasswordAuthenticationToken(
                user,
                null,
                roles
        );
    }
}
