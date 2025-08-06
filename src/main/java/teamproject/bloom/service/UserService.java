package teamproject.bloom.service;

import org.springframework.security.core.Authentication;
import teamproject.bloom.dto.user.UserLoginResponseDto;
import teamproject.bloom.exception.checked.RegistrationException;

public interface UserService {
    UserLoginResponseDto register(String userName, String authHeader) throws RegistrationException;

    String getUserName(Authentication authentication);

    String getToken(String bearerToken);
}
