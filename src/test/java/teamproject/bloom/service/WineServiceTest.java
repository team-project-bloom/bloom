package teamproject.bloom.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;
import static teamproject.bloom.util.WineTestUtil.createParamDto;
import static teamproject.bloom.util.WineTestUtil.createWine;
import static teamproject.bloom.util.WineTestUtil.mapWineToWineResponseDto;

import java.util.List;
import java.util.Optional;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import teamproject.bloom.dto.wine.WineResponseDto;
import teamproject.bloom.dto.wine.WineSearchParametersDto;
import teamproject.bloom.exception.unchecked.EntityNotFoundException;
import teamproject.bloom.mapper.WineMapper;
import teamproject.bloom.model.Wine;
import teamproject.bloom.repository.wine.WineRepository;
import teamproject.bloom.repository.wine.WineSpecificationBuilder;
import teamproject.bloom.service.impl.WineServiceImpl;

@ExtendWith(MockitoExtension.class)
public class WineServiceTest {
    @InjectMocks
    private WineServiceImpl wineService;
    @Mock
    private WineRepository wineRepository;
    @Mock
    private WineMapper wineMapper;
    @Mock
    private WineSpecificationBuilder wineSpecificationBuilder;
    @Mock
    private Specification specification;

    @Test
    @DisplayName("Verify method getAll with correct data")
    public void getAll_CorrectData_ReturnPageDto() {
        Wine wine = createWine(1L, "Wine");
        WineResponseDto expected = mapWineToWineResponseDto(wine);
        Pageable pageable = PageRequest.of(0, 10);
        List<Wine> wines = List.of(wine);
        PageImpl<Wine> winePage = new PageImpl<>(wines, pageable, wines.size());

        when(wineRepository.findAll(pageable)).thenReturn(winePage);
        when(wineMapper.toDto(wine)).thenReturn(expected);
        Page<WineResponseDto> pages = wineService.getAll(pageable);
        List<WineResponseDto> actual = pages.get().toList();

        assertEquals(1, actual.size());
        assertEquals(expected, actual.get(0));
    }

    @Test
    @DisplayName("Verify method getWineById with correct data")
    public void getWineById_CorrectData_ReturnDto() {
        Wine wine = createWine(1L, "Wine");
        WineResponseDto expected = mapWineToWineResponseDto(wine);

        when(wineRepository.findById(wine.getId())).thenReturn(Optional.of(wine));
        when(wineMapper.toDto(wine)).thenReturn(expected);
        WineResponseDto actual = wineService.getWineById(wine.getId());

        assertEquals(expected, actual);
    }

    @Test
    @DisplayName("""
            Verify method getWineById with incorrect data.
             A wine by id isn`t exist
            """)
    public void getWineById_IncorrectData_ReturnStatus() {
        Wine wine = createWine(1L, "Wine");

        when(wineRepository.findById(1L)).thenReturn(Optional.empty());
        Exception actual = assertThrows(EntityNotFoundException.class,
                () -> wineService.getWineById(wine.getId()));

        String expected = "Can`t find wine by id " + wine.getId();
        assertEquals(expected, actual.getMessage());
    }

    @Test
    @DisplayName("Verify method search with correct data")
    public void search_CorrectData_ReturnDto() {
        WineSearchParametersDto params = createParamDto();
        specification = mock(Specification.class);
        Wine wine = createWine(1L, "Wine");
        Pageable pageable = PageRequest.of(0, 20);
        WineResponseDto expected = mapWineToWineResponseDto(wine);
        List<Wine> wines = List.of(wine);
        PageImpl<Wine> winePage = new PageImpl<>(wines, pageable, wines.size());

        when(wineSpecificationBuilder.build(params)).thenReturn(specification);
        when(wineRepository.findAll(specification, pageable)).thenReturn(winePage);
        when(wineMapper.toDto(wine)).thenReturn(expected);
        Page<WineResponseDto> pages = wineService.search(params, pageable);
        List<WineResponseDto> actual = pages.get().toList();

        assertEquals(1, actual.size());
        assertEquals(expected, actual.get(0));
    }
}
