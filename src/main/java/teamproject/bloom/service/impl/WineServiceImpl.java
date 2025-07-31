package teamproject.bloom.service.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import teamproject.bloom.dto.wine.WineResponseDto;
import teamproject.bloom.dto.wine.WineWithAllParamsDto;
import teamproject.bloom.exception.EntityNotFoundException;
import teamproject.bloom.mapper.WineMapper;
import teamproject.bloom.model.Wine;
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

    @Override
    public WineWithAllParamsDto getWineById(Long id) {
        return wineMapper.toDtoWithAllParams(findWineById(id));
    }

    private Wine findWineById(Long id) {
        return wineRepository.findById(id).orElseThrow(
                () -> new EntityNotFoundException("Can`t find wine by id " + id)
        );
    }
}
