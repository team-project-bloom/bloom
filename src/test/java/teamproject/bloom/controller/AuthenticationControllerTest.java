package teamproject.bloom.controller;

import static org.apache.commons.lang3.builder.EqualsBuilder.reflectionEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.when;
import static org.springframework.security.test.web.servlet.setup.SecurityMockMvcConfigurers.springSecurity;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static teamproject.bloom.service.impl.UserServiceImpl.BEARER;
import static teamproject.bloom.util.UserTestUtil.authentication;
import static teamproject.bloom.util.UserTestUtil.mapTokenToUserLoginResponseDto;
import static teamproject.bloom.util.UserTestUtil.user;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;
import teamproject.bloom.dto.user.UserLoginResponseDto;
import teamproject.bloom.mapper.UserMapper;
import teamproject.bloom.model.User;
import teamproject.bloom.repository.user.UserRepository;
import teamproject.bloom.security.JwtUtil;
import teamproject.bloom.service.ShoppingCartService;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@AutoConfigureMockMvc
public class AuthenticationControllerTest {
    protected static MockMvc mockMvc;
    @Autowired
    private ObjectMapper objectMapper;
    @MockBean
    private JwtUtil jwtUtil;
    @MockBean
    private UserRepository userRepository;
    @MockBean
    private UserMapper userMapper;
    @MockBean
    private ShoppingCartService shoppingCartService;

    @BeforeAll
    static void beforeAll(
            @Autowired WebApplicationContext applicationContext
    ) {
        mockMvc = MockMvcBuilders
                .webAppContextSetup(applicationContext)
                .apply(springSecurity())
                .build();
    }

    @Test
    @DisplayName("Verify method register with correct data. User isn`t exists")
    public void register_CorrectDataUserNonExists_ReturnDto() throws Exception {
        String token = BEARER + "fx34.cf3f3.c3fx3c";
        User user = user(1L, "userName");
        UserLoginResponseDto expected = mapTokenToUserLoginResponseDto(token);

        when(userMapper.createUser(anyString())).thenReturn(user);
        when(userRepository.save(user)).thenReturn(user);
        doNothing().when(shoppingCartService).createShoppingCart(any(User.class));
        when(jwtUtil.generatedToken(anyString())).thenReturn(token);
        when(userMapper.toResponseDto(token)).thenReturn(expected);
        MvcResult result = mockMvc.perform(
                        post("/auth/registration")
                                .header(HttpHeaders.AUTHORIZATION, "")
                                .contentType(MediaType.APPLICATION_JSON)
                ).andExpect(status().isCreated())
                .andReturn();

        UserLoginResponseDto actual = objectMapper.readValue(
                result.getResponse().getContentAsString(), UserLoginResponseDto.class);
        assertTrue(reflectionEquals(expected, actual));
    }

    @Test
    @DisplayName("Verify method register with correct data. User is exists")
    public void register_CorrectDataUserIsExists_ReturnDto() throws Exception {
        User user = user(1L, "userName");
        Authentication authentication = authentication(user);
        SecurityContext context = SecurityContextHolder.createEmptyContext();
        context.setAuthentication(authentication);
        SecurityContextHolder.setContext(context);
        String bearerToken = BEARER + "fx34.cf3f3.c3fx3c";
        String token = "fx34.cf3f3.c3fx3c";
        UserLoginResponseDto expected = mapTokenToUserLoginResponseDto(token);

        when(userRepository.existsByUserName(anyString())).thenReturn(true);
        when(userMapper.toResponseDto(token)).thenReturn(expected);
        MvcResult result = mockMvc.perform(
                        post("/auth/registration")
                                .header(HttpHeaders.AUTHORIZATION, bearerToken)
                                .contentType(MediaType.APPLICATION_JSON)
                ).andExpect(status().isCreated())
                .andReturn();

        UserLoginResponseDto actual = objectMapper.readValue(
                result.getResponse().getContentAsString(), UserLoginResponseDto.class);
        assertTrue(reflectionEquals(expected, actual));
    }
}
