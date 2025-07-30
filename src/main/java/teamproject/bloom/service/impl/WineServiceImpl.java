package teamproject.bloom.service.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import teamproject.bloom.dto.WineResponseDto;
import teamproject.bloom.mapper.WineMapper;
import teamproject.bloom.repository.WineRepository;
import teamproject.bloom.service.WineService;

@Service
@RequiredArgsConstructor
public class WineServiceImpl implements WineService {
    private final WineRepository wineRepository;
    private final WineMapper wineMapper;

    @Override
    public Page<WineResponseDto> getAll(Pageable pageable) {
        return wineRepository.findAll(pageable).map(wineMapper::toDto);
    }
}
