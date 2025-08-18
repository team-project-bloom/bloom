package teamproject.bloom.service.impl;

import java.util.UUID;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;
import teamproject.bloom.dto.user.UserLoginResponseDto;
import teamproject.bloom.mapper.UserMapper;
import teamproject.bloom.model.User;
import teamproject.bloom.repository.user.UserRepository;
import teamproject.bloom.security.JwtUtil;
import teamproject.bloom.service.ShoppingCartService;
import teamproject.bloom.service.UserService;

@Service
@RequiredArgsConstructor
@Transactional
public class UserServiceImpl implements UserService {
    public static final String BEARER = "Bearer ";
    private final UserRepository userRepository;
    private final UserMapper userMapper;
    private final ShoppingCartService shoppingCartService;
    private final JwtUtil jwtUtil;

    @Override
    public UserLoginResponseDto register(String userName, String bearerToken) {
        if (userRepository.existsByUserName(userName)) {
            return userMapper.toResponseDto(getToken(bearerToken));
        }
        userName = UUID.randomUUID().toString();
        User user = userMapper.createUser(userName);
        user = userRepository.save(user);
        shoppingCartService.createShoppingCart(user);
        String token = jwtUtil.generatedToken(userName);
        return userMapper.toResponseDto(token);
    }

    @Override
    public String getUserName(Authentication authentication) {
        if (authentication != null) {
            return (String) authentication.getPrincipal();
        }
        return null;
    }

    @Override
    public String getToken(String bearerToken) {
        if (StringUtils.hasText(bearerToken) && bearerToken.startsWith(BEARER)) {
            return bearerToken.substring(BEARER.length());
        }
        return null;
    }
}
