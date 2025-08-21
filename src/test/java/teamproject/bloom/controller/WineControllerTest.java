package teamproject.bloom.controller;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.springframework.security.test.web.servlet.setup.SecurityMockMvcConfigurers.springSecurity;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static teamproject.bloom.util.WineTestUtil.listWineResponseDtos;
import static teamproject.bloom.util.WineTestUtil.wineResponseDto;

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
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;
import teamproject.bloom.dto.wine.WineResponseDto;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@AutoConfigureMockMvc
@Sql(scripts = {"classpath:db/grape/add-grapes-to-grapes-table.sql",
        "classpath:db/region/add-regions-to-regions-table.sql",
        "classpath:db/wine/add-wines-to-wines-table.sql"},
        executionPhase = Sql.ExecutionPhase.BEFORE_TEST_CLASS)
@Sql(scripts = {"classpath:db/grape/delete-grapes-from-grapes-table.sql",
        "classpath:db/region/delete-regions-from-regions.sql",
        "classpath:db/wine/delete-wines-from-wines-table.sql"},
        executionPhase = Sql.ExecutionPhase.AFTER_TEST_CLASS)
public class WineControllerTest {
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
    @DisplayName("Verify method getAll with correct data")
    public void getAll_CorrectData_ReturnPageDto() throws Exception {
        MvcResult result = mockMvc.perform(
                        get("/wines")
                                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andReturn();

        JsonNode root = objectMapper.readTree(result.getResponse().getContentAsString());
        List<WineResponseDto> actual = objectMapper.readValue(root.get("content").toString(),
                new TypeReference<>() {
                });

        List<WineResponseDto> expected = listWineResponseDtos();
        assertEquals(expected, actual);
    }

    @Test
    @DisplayName("Verify method getById with correct data")
    public void getById_CorrectData_ReturnDto() throws Exception {
        MvcResult result = mockMvc.perform(
                        get("/wines/2")
                                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andReturn();
        WineResponseDto actual = objectMapper.readValue(
                result.getResponse().getContentAsString(), WineResponseDto.class);

        WineResponseDto expected = wineResponseDto();
        assertEquals(expected, actual);
    }

    @Test
    @DisplayName("Verify method getById with incorrect data. A wine isn`t exists")
    public void getById_IncorrectData_ReturnStatus() throws Exception {
        mockMvc.perform(
                        get("/wines/21")
                                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound());
    }

    @Test
    @DisplayName("Verify method search with correct data")
    public void search_CorrectData_ReturnDto() throws Exception {
        MvcResult result = mockMvc.perform(
                        get("/wines/search?title=2")
                                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andReturn();

        JsonNode root = objectMapper.readTree(result.getResponse().getContentAsString());
        List<WineResponseDto> actual = objectMapper.readValue(root.get("content").toString(),
                new TypeReference<>() {
                });
        WineResponseDto expected = wineResponseDto();
        assertEquals(1, actual.size());
        assertEquals(expected, actual.get(0));
    }

    @Test
    @DisplayName("Verify method search with incorrect data. A wine isn`t exist")
    public void search_IncorrectData_ReturnDto() throws Exception {
        MvcResult result = mockMvc.perform(
                        get("/wines/search?title=21")
                                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andReturn();

        JsonNode root = objectMapper.readTree(result.getResponse().getContentAsString());
        List<WineResponseDto> actual = objectMapper.readValue(root.get("content").toString(),
                new TypeReference<>() {
                });
        assertEquals(0, actual.size());
    }
}
