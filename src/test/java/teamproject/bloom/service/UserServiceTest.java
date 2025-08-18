package teamproject.bloom.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;
import static teamproject.bloom.service.impl.UserServiceImpl.BEARER;
import static teamproject.bloom.util.UserTestUtil.mapTokenToUserLoginResponseDto;
import static teamproject.bloom.util.UserTestUtil.user;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.core.Authentication;
import teamproject.bloom.dto.user.UserLoginResponseDto;
import teamproject.bloom.mapper.UserMapper;
import teamproject.bloom.model.User;
import teamproject.bloom.repository.user.UserRepository;
import teamproject.bloom.security.JwtUtil;
import teamproject.bloom.service.impl.UserServiceImpl;

@ExtendWith(MockitoExtension.class)
public class UserServiceTest {
    @InjectMocks
    private UserServiceImpl userService;
    @Mock
    private UserRepository userRepository;
    @Mock
    private UserMapper userMapper;
    @Mock
    private ShoppingCartService shoppingCartService;
    @Mock
    private JwtUtil jwtUtil;

    @Test
    @DisplayName("Verify method register with correct data. User isn`t exists")
    public void register_CorrectDataUserNoExists_ReturnDto() {
        User user = user(1L);
        String token = "g4m5g94.4f45g.f45f3ff";
        UserLoginResponseDto expected = mapTokenToUserLoginResponseDto(token);

        when(userRepository.existsByUserName(anyString())).thenReturn(false);
        when(userMapper.createUser(anyString())).thenReturn(user);
        when(userRepository.save(user)).thenReturn(user);
        when(jwtUtil.generatedToken(anyString())).thenReturn(token);
        when(userMapper.toResponseDto(token)).thenReturn(expected);
        UserLoginResponseDto actual = userService.register(anyString(), token);

        assertEquals(expected, actual);
    }

    @Test
    @DisplayName("Verify method register with correct data. User is exists")
    public void register_CorrectDataUserExists_ReturnDto() {
        String token = "g4m5g94.4f45g.f45f3ff";
        String bearerToken = BEARER + "g4m5g94.4f45g.f45f3ff";
        UserLoginResponseDto expected = mapTokenToUserLoginResponseDto(token);

        when(userRepository.existsByUserName(anyString())).thenReturn(true);
        when(userMapper.toResponseDto(token)).thenReturn(expected);
        UserLoginResponseDto actual = userService.register(anyString(), bearerToken);

        assertEquals(expected, actual);
    }

    @Test
    @DisplayName("Verify method getUserName with correct data")
    public void getUserName_CorrectData_ReturnString() {
        String userName = "UserName";
        Authentication authentication = mock(Authentication.class);

        when(authentication.getPrincipal()).thenReturn(userName);
        String actual = userService.getUserName(authentication);

        assertEquals(userName, actual);
    }

    @Test
    @DisplayName("Verify method getUserName with incorrect data")
    public void getUserName_IncorrectData_ReturnNull() {
        String actual = userService.getUserName(null);

        assertNull(actual);
    }

    @Test
    @DisplayName("Verify method getToken with incorrect data. Token Null")
    public void getToken_IncorrectDataTokenNull_ReturnNull() {
        String userName = "UserName";
        String token = "";

        when(userRepository.existsByUserName(userName)).thenReturn(true);
        UserLoginResponseDto actual = userService.register(userName, token);

        assertNull(actual);
    }

    @Test
    @DisplayName("Verify method getToken with incorrect data. Token start no bearer")
    public void getToken_IncorrectDataStartNonBearer_ReturnNull() {
        String userName = "UserName";
        String token = "ew2fe4wc.cwe2f2cw.cew3cf34cc";

        when(userRepository.existsByUserName(userName)).thenReturn(true);
        UserLoginResponseDto actual = userService.register(userName, token);

        assertNull(actual);
    }
}
