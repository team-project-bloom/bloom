package teamproject.bloom.service;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import teamproject.bloom.dto.wine.WineResponseDto;
import teamproject.bloom.dto.wine.WineResponseWithAllParamsDto;

public interface WineService {
    Page<WineResponseDto> getAll(Pageable pageable);

    WineResponseWithAllParamsDto getWineById(Long id);
}
