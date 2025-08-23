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
@Sql(scripts = {"classpath:db/wine/delete-wines-from-wines-table.sql",
        "classpath:db/grape/delete-grapes-from-grapes-table.sql",
        "classpath:db/region/delete-regions-from-regions.sql"},
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
    @DisplayName("Verify method search with correct data. A wine with title is exist")
    public void search_CorrectDataTitle_ReturnDto() throws Exception {
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
    @DisplayName("Verify method search with incorrect data. A wine with title isn`t exist")
    public void search_IncorrectDataTitle_ReturnDto() throws Exception {
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

    @Test
    @DisplayName("""
            Verify method search with correct data.
             The wine with price From To is exist
            """)
    public void search_CorrectDataPrice_ReturnDto() throws Exception {
        MvcResult result = mockMvc.perform(
                        get("/wines/search?priceFrom=20.00&priceTo=30.00")
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
    @DisplayName("Verify method search with correct data. A wine with priceFrom isn`t exist")
    public void search_CorrectDataPriceFrom_ReturnDto() throws Exception {
        MvcResult result = mockMvc.perform(
                        get("/wines/search?priceFrom=40.00")
                                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andReturn();

        JsonNode root = objectMapper.readTree(result.getResponse().getContentAsString());
        List<WineResponseDto> actual = objectMapper.readValue(root.get("content").toString(),
                new TypeReference<>() {
                });
        assertEquals(0, actual.size());
    }

    @Test
    @DisplayName("Verify method search with correct data. A wine with priceTo isn`t exist")
    public void search_CorrectDataPriceTo_ReturnDto() throws Exception {
        MvcResult result = mockMvc.perform(
                        get("/wines/search?priceTo=10.00")
                                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andReturn();

        JsonNode root = objectMapper.readTree(result.getResponse().getContentAsString());
        List<WineResponseDto> actual = objectMapper.readValue(root.get("content").toString(),
                new TypeReference<>() {
                });
        assertEquals(0, actual.size());
    }

    @Test
    @DisplayName("Verify method search with correct data. A wine with alcohol is exist")
    public void search_CorrectDataAlcohol_ReturnDto() throws Exception {
        MvcResult result = mockMvc.perform(
                        get("/wines/search?alcohol=2.20")
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
    @DisplayName("Verify method search with correct data. A wine with alcohol isn`t exist")
    public void search_IncorrectDataAlcohol_ReturnDto() throws Exception {
        MvcResult result = mockMvc.perform(
                        get("/wines/search?alcohol=99.00")
                                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andReturn();

        JsonNode root = objectMapper.readTree(result.getResponse().getContentAsString());
        List<WineResponseDto> actual = objectMapper.readValue(root.get("content").toString(),
                new TypeReference<>() {
                });
        assertEquals(0, actual.size());
    }

    @Test
    @DisplayName("Verify method search with correct data. A wine with variety is exist")
    public void search_CorrectDataVariety_ReturnDto() throws Exception {
        MvcResult result = mockMvc.perform(
                        get("/wines/search?variety=ORANGE")
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
    @DisplayName("Verify method search with correct data. A wine with variety isn`t exist")
    public void search_IncorrectDataVariety_ReturnDto() throws Exception {
        MvcResult result = mockMvc.perform(
                        get("/wines/search?variety=PET_NAT")
                                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andReturn();

        JsonNode root = objectMapper.readTree(result.getResponse().getContentAsString());
        List<WineResponseDto> actual = objectMapper.readValue(root.get("content").toString(),
                new TypeReference<>() {
                });
        assertEquals(0, actual.size());
    }

    @Test
    @DisplayName("Verify method search with correct data. A wine with value is exist")
    public void search_CorrectDataValue_ReturnDto() throws Exception {
        MvcResult result = mockMvc.perform(
                        get("/wines/search?value=VEGAN")
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
    @DisplayName("Verify method search with correct data. A wine with value isn`t exist")
    public void search_IncorrectDataValue_ReturnDto() throws Exception {
        MvcResult result = mockMvc.perform(
                        get("/wines/search?value=NATURAL")
                                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andReturn();

        JsonNode root = objectMapper.readTree(result.getResponse().getContentAsString());
        List<WineResponseDto> actual = objectMapper.readValue(root.get("content").toString(),
                new TypeReference<>() {
                });
        assertEquals(0, actual.size());
    }

    @Test
    @DisplayName("""
            Verify method search with correct data.
             The wine with vintage From To is exist
            """)
    public void search_CorrectDataVintage_ReturnDto() throws Exception {
        MvcResult result = mockMvc.perform(
                        get("/wines/search?vintageFrom=2021&vintageTo=2023")
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
    @DisplayName("Verify method search with correct data. A wine with vintageFrom isn`t exist")
    public void search_IncorrectDataVintageFrom_ReturnDto() throws Exception {
        MvcResult result = mockMvc.perform(
                        get("/wines/search?vintageFrom=2025")
                                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andReturn();

        JsonNode root = objectMapper.readTree(result.getResponse().getContentAsString());
        List<WineResponseDto> actual = objectMapper.readValue(root.get("content").toString(),
                new TypeReference<>() {
                });
        assertEquals(0, actual.size());
    }

    @Test
    @DisplayName("Verify method search with correct data. A wine with vintageTo isn`t exist")
    public void search_IncorrectDataVintageTo_ReturnDto() throws Exception {
        MvcResult result = mockMvc.perform(
                        get("/wines/search?vintageTo=2019")
                                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andReturn();

        JsonNode root = objectMapper.readTree(result.getResponse().getContentAsString());
        List<WineResponseDto> actual = objectMapper.readValue(root.get("content").toString(),
                new TypeReference<>() {
                });
        assertEquals(0, actual.size());
    }

    @Test
    @DisplayName("Verify method search with correct data. A wine with grape is exist")
    public void search_CorrectDataGrape_ReturnDto() throws Exception {
        MvcResult result = mockMvc.perform(
                        get("/wines/search?grape=Grape2")
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
    @DisplayName("Verify method search with correct data. A wine with grape isn`t exist")
    public void search_IncorrectDataGrape_ReturnDto() throws Exception {
        MvcResult result = mockMvc.perform(
                        get("/wines/search?grape=Grape5")
                                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andReturn();

        JsonNode root = objectMapper.readTree(result.getResponse().getContentAsString());
        List<WineResponseDto> actual = objectMapper.readValue(root.get("content").toString(),
                new TypeReference<>() {
                });
        assertEquals(0, actual.size());
    }

    @Test
    @DisplayName("Verify method search with correct data. A wine with region is exist")
    public void search_CorrectDataRegion_ReturnDto() throws Exception {
        MvcResult result = mockMvc.perform(
                        get("/wines/search?region=Region2")
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
    @DisplayName("Verify method search with correct data. A wine with region isn`t exist")
    public void search_IncorrectDataRegion_ReturnDto() throws Exception {
        MvcResult result = mockMvc.perform(
                        get("/wines/search?region=Region5")
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
