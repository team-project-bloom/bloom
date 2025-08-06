package teamproject.bloom.service;

import org.springframework.security.core.Authentication;
import teamproject.bloom.dto.user.UserLoginResponseDto;

public interface UserService {
    UserLoginResponseDto register(String userName, String authHeader);

    String getUserName(Authentication authentication);

    String getToken(String bearerToken);
}
