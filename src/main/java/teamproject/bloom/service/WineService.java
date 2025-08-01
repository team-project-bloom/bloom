package teamproject.bloom.service;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import teamproject.bloom.dto.wine.WineResponseDto;
import teamproject.bloom.dto.wine.WineResponseWithAllParamsDto;
import teamproject.bloom.dto.wine.WineSearchParametersDto;

public interface WineService {
    Page<WineResponseDto> getAll(Pageable pageable);

    WineResponseWithAllParamsDto getWineById(Long id);

    Page<WineResponseDto> search(WineSearchParametersDto wineSearchDto);
}
