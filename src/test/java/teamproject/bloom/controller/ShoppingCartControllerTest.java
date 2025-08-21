package teamproject.bloom.controller;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.springframework.security.test.web.servlet.setup.SecurityMockMvcConfigurers.springSecurity;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static teamproject.bloom.util.ShoppingCartTestUtil.cartItemRequestDto;
import static teamproject.bloom.util.ShoppingCartTestUtil.cartItemUpdateDto;
import static teamproject.bloom.util.ShoppingCartTestUtil.emptyShoppingCart;
import static teamproject.bloom.util.ShoppingCartTestUtil.mapCartToCartDto;
import static teamproject.bloom.util.ShoppingCartTestUtil.shoppingCart;
import static teamproject.bloom.util.UserTestUtil.authentication;
import static teamproject.bloom.util.UserTestUtil.user;
import static teamproject.bloom.util.WineTestUtil.wine;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;
import teamproject.bloom.dto.cartitem.CartItemRequestDto;
import teamproject.bloom.dto.cartitem.CartItemUpdateDto;
import teamproject.bloom.dto.shoppingcart.ShoppingCartResponseDto;
import teamproject.bloom.model.ShoppingCart;
import teamproject.bloom.model.User;
import teamproject.bloom.model.Wine;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@AutoConfigureMockMvc
@Sql(scripts = {"classpath:db/grape/add-grapes-to-grapes-table.sql",
        "classpath:db/region/add-regions-to-regions-table.sql",
        "classpath:db/wine/add-wines-to-wines-table.sql",
        "classpath:db/user/add-user-to-users-table.sql",
        "classpath:db/shoppingcart/add-shopping_carts-to-shopping_carts-table.sql"},
        executionPhase = Sql.ExecutionPhase.BEFORE_TEST_CLASS)
@Sql(scripts = {"classpath:db/grape/delete-grapes-from-grapes-table.sql",
        "classpath:db/region/delete-regions-from-regions.sql",
        "classpath:db/wine/delete-wines-from-wines-table.sql",
        "classpath:db/user/delete-user-from-users-table.sql",
        "classpath:db/shoppingcart/delete-shopping_carts-from-shopping_carts-table.sql"},
        executionPhase = Sql.ExecutionPhase.AFTER_TEST_CLASS)
public class ShoppingCartControllerTest {
    protected static MockMvc mockMvc;
    @Autowired
    private ObjectMapper objectMapper;

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
    @DisplayName("Verify method addItem with correct data")
    @Sql(scripts = {"classpath:db/cartitem/delete-cart_items-from-cart_items-table.sql"},
            executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
    public void addItem_CorrectData_ReturnDto() throws Exception {
        User user = user(1L, "UserName");
        Authentication authentication = authentication(user);
        SecurityContext context = SecurityContextHolder.createEmptyContext();
        context.setAuthentication(authentication);
        SecurityContextHolder.setContext(context);
        Wine wine = wine(1L, "Wine1");
        int quantity = 3;
        CartItemRequestDto cartItemRequestDto = cartItemRequestDto(wine.getId(), quantity);
        String jsonRequest = objectMapper.writeValueAsString(cartItemRequestDto);

        MvcResult result = mockMvc.perform(
                        post("/cart")
                                .content(jsonRequest)
                                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isCreated())
                .andReturn();
        ShoppingCartResponseDto actual = objectMapper.readValue(
                result.getResponse().getContentAsString(), ShoppingCartResponseDto.class);

        ShoppingCart cart = shoppingCart(1L, wine, quantity, user);
        ShoppingCartResponseDto expected = mapCartToCartDto(cart);
        assertEquals(expected, actual);
    }

    @Test
    @DisplayName("Verify method addItem with incorrect data. The user isn`t exist")
    @Sql(scripts = {"classpath:db/cartitem/delete-cart_items-from-cart_items-table.sql"},
            executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
    public void addItem_IncorrectDataUserNonExist_ReturnStatus() throws Exception {
        User user = user(3L, "UserName3");
        Authentication authentication = authentication(user);
        SecurityContext context = SecurityContextHolder.createEmptyContext();
        context.setAuthentication(authentication);
        SecurityContextHolder.setContext(context);
        Wine wine = wine(1L, "Wine1");
        CartItemRequestDto cartItemRequestDto = cartItemRequestDto(wine.getId(), 3);
        String jsonRequest = objectMapper.writeValueAsString(cartItemRequestDto);

        mockMvc.perform(
                        post("/cart")
                                .content(jsonRequest)
                                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound());
    }

    @Test
    @DisplayName("Verify method addItem with incorrect data. The wine isn`t exist")
    @Sql(scripts = {"classpath:db/cartitem/delete-cart_items-from-cart_items-table.sql"},
            executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
    public void addItem_IncorrectDataWineNonExist_ReturnStatus() throws Exception {
        User user = user(1L, "UserName1");
        Authentication authentication = authentication(user);
        SecurityContext context = SecurityContextHolder.createEmptyContext();
        context.setAuthentication(authentication);
        SecurityContextHolder.setContext(context);
        Wine wine = wine(7L, "Wine7");
        CartItemRequestDto cartItemRequestDto = cartItemRequestDto(wine.getId(), 3);
        String jsonRequest = objectMapper.writeValueAsString(cartItemRequestDto);

        mockMvc.perform(
                        post("/cart")
                                .content(jsonRequest)
                                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound());
    }

    @Test
    @DisplayName("Verify method getAllUserFavoriteItems with correct data")
    @Sql(scripts = {"classpath:db/cartitem/add-cart_items-to-cart_items-table.sql"},
            executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
    @Sql(scripts = {"classpath:db/cartitem/delete-cart_items-from-cart_items-table.sql"},
            executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
    public void getAllUserFavoriteItems_CorrectData_ReturnDto() throws Exception {
        User user = user(1L, "UserName");
        Authentication authentication = authentication(user);
        SecurityContext context = SecurityContextHolder.createEmptyContext();
        context.setAuthentication(authentication);
        SecurityContextHolder.setContext(context);
        Wine wine = wine(1L, "Wine1");
        ShoppingCart cart = shoppingCart(1L, wine, 3, user);

        MvcResult result = mockMvc.perform(
                        get("/cart")
                                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andReturn();
        ShoppingCartResponseDto actual = objectMapper.readValue(
                result.getResponse().getContentAsString(), ShoppingCartResponseDto.class);

        ShoppingCartResponseDto expected = mapCartToCartDto(cart);
        assertEquals(expected, actual);
    }

    @Test
    @DisplayName("Verify method getAllUserFavoriteItems with incorrect data. The user isn`t exist")
    @Sql(scripts = {"classpath:db/cartitem/delete-cart_items-from-cart_items-table.sql"},
            executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
    public void getAllUserFavoriteItems_IncorrectDataUserNonExist_ReturnStatus() throws Exception {
        User user = user(3L, "UserName3");
        Authentication authentication = authentication(user);
        SecurityContext context = SecurityContextHolder.createEmptyContext();
        context.setAuthentication(authentication);
        SecurityContextHolder.setContext(context);

        mockMvc.perform(
                        get("/cart")
                                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound());
    }

    @Test
    @DisplayName("Verify method updateCartItem with correct data")
    @Sql(scripts = {"classpath:db/cartitem/add-cart_items-to-cart_items-table.sql"},
            executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
    @Sql(scripts = {"classpath:db/cartitem/delete-cart_items-from-cart_items-table.sql"},
            executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
    public void updateCartItem_CorrectData_ReturnDto() throws Exception {
        User user = user(1L, "UserName");
        Authentication authentication = authentication(user);
        SecurityContext context = SecurityContextHolder.createEmptyContext();
        context.setAuthentication(authentication);
        SecurityContextHolder.setContext(context);
        int quantity = 7;
        CartItemUpdateDto cartItemUpdateDto = cartItemUpdateDto(quantity);
        String jsonRequest = objectMapper.writeValueAsString(cartItemUpdateDto);
        Wine wine = wine(1L, "Wine1");
        ShoppingCart cart = shoppingCart(1L, wine, quantity, user);

        MvcResult result = mockMvc.perform(
                        put("/cart/items/1")
                                .content(jsonRequest)
                                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andReturn();
        ShoppingCartResponseDto actual = objectMapper.readValue(
                result.getResponse().getContentAsString(), ShoppingCartResponseDto.class);

        ShoppingCartResponseDto expected = mapCartToCartDto(cart);
        assertEquals(expected, actual);
    }

    @Test
    @DisplayName("Verify method updateCartItem with incorrect data. The user isn`t exist")
    @Sql(scripts = {"classpath:db/cartitem/add-cart_items-to-cart_items-table.sql"},
            executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
    @Sql(scripts = {"classpath:db/cartitem/delete-cart_items-from-cart_items-table.sql"},
            executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
    public void updateCartItem_IncorrectDataUserNonExist_ReturnStatus() throws Exception {
        User user = user(3L, "UserName3");
        Authentication authentication = authentication(user);
        SecurityContext context = SecurityContextHolder.createEmptyContext();
        context.setAuthentication(authentication);
        SecurityContextHolder.setContext(context);
        int quantity = 7;
        CartItemUpdateDto cartItemUpdateDto = cartItemUpdateDto(quantity);
        String jsonRequest = objectMapper.writeValueAsString(cartItemUpdateDto);

        mockMvc.perform(
                        put("/cart/items/1")
                                .content(jsonRequest)
                                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound());
    }

    @Test
    @DisplayName("Verify method updateCartItem with incorrect data. The item isn`t exist")
    @Sql(scripts = {"classpath:db/cartitem/add-cart_items-to-cart_items-table.sql"},
            executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
    @Sql(scripts = {"classpath:db/cartitem/delete-cart_items-from-cart_items-table.sql"},
            executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
    public void updateCartItem_IncorrectDataItemNonExist_ReturnStatus() throws Exception {
        User user = user(1L, "UserName");
        Authentication authentication = authentication(user);
        SecurityContext context = SecurityContextHolder.createEmptyContext();
        context.setAuthentication(authentication);
        SecurityContextHolder.setContext(context);
        int quantity = 7;
        CartItemUpdateDto cartItemUpdateDto = cartItemUpdateDto(quantity);
        String jsonRequest = objectMapper.writeValueAsString(cartItemUpdateDto);

        mockMvc.perform(
                        put("/cart/items/33")
                                .content(jsonRequest)
                                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound());
    }

    @Test
    @DisplayName("Verify method deleteCartItem with correct data")
    @Sql(scripts = {"classpath:db/cartitem/add-cart_items-to-cart_items-table.sql"},
            executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
    @Sql(scripts = {"classpath:db/cartitem/delete-cart_items-from-cart_items-table.sql"},
            executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
    public void deleteCartItem_CorrectData_ReturnStatus() throws Exception {
        User user = user(1L, "UserName");
        Authentication authentication = authentication(user);
        SecurityContext context = SecurityContextHolder.createEmptyContext();
        context.setAuthentication(authentication);
        SecurityContextHolder.setContext(context);

        mockMvc.perform(
                        delete("/cart/1")
                                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNoContent());
        MvcResult result = mockMvc.perform(
                        get("/cart")
                                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andReturn();
        ShoppingCartResponseDto actual = objectMapper.readValue(
                result.getResponse().getContentAsString(), ShoppingCartResponseDto.class);

        ShoppingCart cart = emptyShoppingCart(1L, user);
        ShoppingCartResponseDto expected = mapCartToCartDto(cart);
        assertEquals(expected, actual);
    }

    @Test
    @DisplayName("Verify method deleteCartItem with incorrect data. The user isn`t exist")
    @Sql(scripts = {"classpath:db/cartitem/add-cart_items-to-cart_items-table.sql"},
            executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
    @Sql(scripts = {"classpath:db/cartitem/delete-cart_items-from-cart_items-table.sql"},
            executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
    public void deleteCartItem_IncorrectDataUserNonExist_ReturnStatus() throws Exception {
        User user = user(3L, "UserName3");
        Authentication authentication = authentication(user);
        SecurityContext context = SecurityContextHolder.createEmptyContext();
        context.setAuthentication(authentication);
        SecurityContextHolder.setContext(context);

        mockMvc.perform(
                        delete("/cart/1")
                                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound());
    }

    @Test
    @DisplayName("Verify method deleteCartItem with incorrect data. The item isn`t exist")
    public void deleteCartItem_IncorrectDataItemNonExist_ReturnStatus() throws Exception {
        User user = user(1L, "UserName");
        Authentication authentication = authentication(user);
        SecurityContext context = SecurityContextHolder.createEmptyContext();
        context.setAuthentication(authentication);
        SecurityContextHolder.setContext(context);

        mockMvc.perform(
                        delete("/cart/1")
                                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound());
    }
}

