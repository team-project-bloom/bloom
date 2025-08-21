package teamproject.bloom.controller;

import static org.apache.commons.lang3.builder.EqualsBuilder.reflectionEquals;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.springframework.security.test.web.servlet.setup.SecurityMockMvcConfigurers.springSecurity;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static teamproject.bloom.util.FavoriteItemTestUtil.favoriteItem;
import static teamproject.bloom.util.FavoriteItemTestUtil.favoriteItemResponseDto;
import static teamproject.bloom.util.FavoriteItemTestUtil.favoriteWineRequestDto;
import static teamproject.bloom.util.FavoriteItemTestUtil.listFavoriteItemResponseDtos;
import static teamproject.bloom.util.FavoriteItemTestUtil.mapFavoriteItemToDto;
import static teamproject.bloom.util.UserTestUtil.authentication;
import static teamproject.bloom.util.UserTestUtil.user;
import static teamproject.bloom.util.WineTestUtil.wine;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.util.List;
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
import teamproject.bloom.dto.favoriteitem.FavoriteItemResponseDto;
import teamproject.bloom.dto.favoriteitem.FavoriteWineRequestDto;
import teamproject.bloom.model.FavoriteItem;
import teamproject.bloom.model.User;
import teamproject.bloom.model.Wine;

@Sql(scripts = {"classpath:db/grape/add-grapes-to-grapes-table.sql",
        "classpath:db/region/add-regions-to-regions-table.sql",
        "classpath:db/wine/add-wines-to-wines-table.sql"},
        executionPhase = Sql.ExecutionPhase.BEFORE_TEST_CLASS)
@Sql(scripts = {"classpath:db/grape/delete-grapes-from-grapes-table.sql",
        "classpath:db/region/delete-regions-from-regions.sql",
        "classpath:db/wine/delete-wines-from-wines-table.sql"},
        executionPhase = Sql.ExecutionPhase.AFTER_TEST_CLASS)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@AutoConfigureMockMvc
public class FavoriteItemControllerTest {
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
    @DisplayName("Verify method addFavoriteItem with correct data")
    @Sql(scripts = {"classpath:db/user/add-user-to-users-table.sql"},
            executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
    @Sql(scripts = {"classpath:db/favoriteitem/delete-favorite_items-from-favorite_items-table.sql",
            "classpath:db/user/delete-user-from-users-table.sql"},
            executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
    public void addFavoriteItem_CorrectData_ReturnDto() throws Exception {
        Wine wine = wine(1L, "Wine1");
        User user = user(1L, "userName");
        FavoriteItem favoriteItem = favoriteItem(1L, wine, user);
        FavoriteWineRequestDto favoriteWineRequestDto = favoriteWineRequestDto(wine.getId());
        String jsonRequest = objectMapper.writeValueAsString(favoriteWineRequestDto);
        Authentication authentication = authentication(user);
        SecurityContext context = SecurityContextHolder.createEmptyContext();
        context.setAuthentication(authentication);
        SecurityContextHolder.setContext(context);

        MvcResult result = mockMvc.perform(
                        post("/favorite/add")
                                .content(jsonRequest)
                                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isCreated())
                .andReturn();
        FavoriteItemResponseDto actual = objectMapper.readValue(
                result.getResponse().getContentAsString(), FavoriteItemResponseDto.class);

        FavoriteItemResponseDto expected = mapFavoriteItemToDto(favoriteItem);
        assertTrue(reflectionEquals(expected, actual, "id"));
    }

    @Test
    @DisplayName("Verify method addFavoriteItem with incorrect data. The user isn`t exist")
    public void addFavoriteItem_IncorrectDataUserNoExist_ReturnDto() throws Exception {
        User user = user(1L, "userName");
        FavoriteWineRequestDto favoriteWineRequestDto = favoriteWineRequestDto(1L);
        String jsonRequest = objectMapper.writeValueAsString(favoriteWineRequestDto);
        Authentication authentication = authentication(user);
        SecurityContext context = SecurityContextHolder.createEmptyContext();
        context.setAuthentication(authentication);
        SecurityContextHolder.setContext(context);

        mockMvc.perform(
                        post("/favorite/add")
                                .content(jsonRequest)
                                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound());
    }

    @Test
    @DisplayName("Verify method addFavoriteItem with incorrect data. The wine isn`t exist")
    @Sql(scripts = {"classpath:db/user/add-user-to-users-table.sql"},
            executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
    @Sql(scripts = {"classpath:db/user/delete-user-from-users-table.sql"},
            executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
    public void addFavoriteItem_IncorrectDataWineNoExist_ReturnStatus() throws Exception {
        Wine wine = wine(15L, "Wine1");
        User user = user(1L, "userName");
        FavoriteWineRequestDto favoriteWineRequestDto = favoriteWineRequestDto(wine.getId());
        String jsonRequest = objectMapper.writeValueAsString(favoriteWineRequestDto);
        Authentication authentication = authentication(user);
        SecurityContext context = SecurityContextHolder.createEmptyContext();
        context.setAuthentication(authentication);
        SecurityContextHolder.setContext(context);

        mockMvc.perform(
                        post("/favorite/add")
                                .content(jsonRequest)
                                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound());
    }

    @Test
    @DisplayName("Verify method addFavoriteItem with incorrect data. The item is exist")
    @Sql(scripts = {"classpath:db/user/add-user-to-users-table.sql",
            "classpath:db/favoriteitem/add-favorite_items-to-favorite_items-table.sql"},
            executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
    @Sql(scripts = {"classpath:db/user/delete-user-from-users-table.sql",
            "classpath:db/favoriteitem/delete-favorite_items-from-favorite_items-table.sql"},
            executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
    public void addFavoriteItem_IncorrectDataItemIsExist_ReturnStatus() throws Exception {
        Wine wine = wine(1L, "Wine1");
        User user = user(1L, "userName");
        FavoriteWineRequestDto favoriteWineRequestDto = favoriteWineRequestDto(wine.getId());
        String jsonRequest = objectMapper.writeValueAsString(favoriteWineRequestDto);
        Authentication authentication = authentication(user);
        SecurityContext context = SecurityContextHolder.createEmptyContext();
        context.setAuthentication(authentication);
        SecurityContextHolder.setContext(context);

        mockMvc.perform(
                        post("/favorite/add")
                                .content(jsonRequest)
                                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound());
    }

    @Test
    @DisplayName("Verify method getAllUserFavoriteItems with correct data")
    @Sql(scripts = {"classpath:db/user/add-user-to-users-table.sql",
            "classpath:db/favoriteitem/add-favorite_items-to-favorite_items-table.sql"},
            executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
    @Sql(scripts = {"classpath:db/user/delete-user-from-users-table.sql",
            "classpath:db/favoriteitem/delete-favorite_items-from-favorite_items-table.sql"},
            executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
    public void getAllUserFavoriteItems_CorrectDataItemIsExist_ReturnDto() throws Exception {
        User user = user(1L, "userName");
        Authentication authentication = authentication(user);
        SecurityContext context = SecurityContextHolder.createEmptyContext();
        context.setAuthentication(authentication);
        SecurityContextHolder.setContext(context);

        MvcResult result = mockMvc.perform(
                        get("/favorite/all")
                                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andReturn();

        JsonNode root = objectMapper.readTree(result.getResponse().getContentAsString());
        List<FavoriteItemResponseDto> actual =
                objectMapper.readValue(root.get("content").toString(),
                        new TypeReference<>() {
                        });

        List<FavoriteItemResponseDto> expected = listFavoriteItemResponseDtos();
        assertEquals(2, actual.size());
        assertEquals(expected, actual);
    }

    @Test
    @DisplayName("Verify method getAllUserFavoriteItems with incorrect data. The user isn`t exist")
    public void getAllUserFavoriteItems_IncorrectDataUserNonExist_ReturnStatus() throws Exception {
        User user = user(1L, "userName");
        Authentication authentication = authentication(user);
        SecurityContext context = SecurityContextHolder.createEmptyContext();
        context.setAuthentication(authentication);
        SecurityContextHolder.setContext(context);

        mockMvc.perform(
                        get("/favorite/all")
                                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound());
    }

    @Test
    @DisplayName("Verify method delete with correct data")
    @Sql(scripts = {"classpath:db/user/add-user-to-users-table.sql",
            "classpath:db/favoriteitem/add-favorite_items-to-favorite_items-table.sql"},
            executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
    @Sql(scripts = {"classpath:db/user/delete-user-from-users-table.sql",
            "classpath:db/favoriteitem/delete-favorite_items-from-favorite_items-table.sql"},
            executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
    public void delete_CorrectData_ReturnStatus() throws Exception {
        User user = user(1L, "userName");
        Authentication authentication = authentication(user);
        SecurityContext context = SecurityContextHolder.createEmptyContext();
        context.setAuthentication(authentication);
        SecurityContextHolder.setContext(context);

        mockMvc.perform(
                        delete("/favorite/1")
                                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNoContent());
        MvcResult result = mockMvc.perform(
                        get("/favorite/all")
                                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andReturn();
        JsonNode root = objectMapper.readTree(result.getResponse().getContentAsString());
        List<FavoriteItemResponseDto> actual =
                objectMapper.readValue(root.get("content").toString(),
                        new TypeReference<>() {
                        });

        FavoriteItemResponseDto expected = favoriteItemResponseDto();
        assertEquals(1, actual.size());
        assertEquals(expected, actual.get(0));
    }

    @Test
    @DisplayName("Verify method delete with incorrect data. The user isn`t owner the item")
    @Sql(scripts = {"classpath:db/user/add-user-to-users-table.sql",
            "classpath:db/favoriteitem/add-favorite_items-to-favorite_items-table.sql"},
            executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
    @Sql(scripts = {"classpath:db/user/delete-user-from-users-table.sql",
            "classpath:db/favoriteitem/delete-favorite_items-from-favorite_items-table.sql"},
            executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
    public void delete_IncorrectData_ReturnStatus() throws Exception {
        User user = user(2L, "userName2");
        Authentication authentication = authentication(user);
        SecurityContext context = SecurityContextHolder.createEmptyContext();
        context.setAuthentication(authentication);
        SecurityContextHolder.setContext(context);

        mockMvc.perform(
                        delete("/favorite/2")
                                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound());
    }

    @Test
    @DisplayName("Verify method delete with incorrect data. The user isn`t exist")
    public void delete_IncorrectDataUserNonExist_ReturnStatus() throws Exception {
        User user = user(1L, "userName");
        Authentication authentication = authentication(user);
        SecurityContext context = SecurityContextHolder.createEmptyContext();
        context.setAuthentication(authentication);
        SecurityContextHolder.setContext(context);

        mockMvc.perform(
                        delete("/favorite/1")
                                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound());
    }
}
