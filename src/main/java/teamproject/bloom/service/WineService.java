package teamproject.bloom.service;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import teamproject.bloom.dto.WineResponseDto;

public interface WineService {
    Page<WineResponseDto> getAll(Pageable pageable);
}
