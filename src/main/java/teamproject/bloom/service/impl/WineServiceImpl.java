package teamproject.bloom.service.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import teamproject.bloom.dto.wine.WineResponseDto;
import teamproject.bloom.dto.wine.WineResponseWithAllParamsDto;
import teamproject.bloom.dto.wine.WineSearchParametersDto;
import teamproject.bloom.exception.EntityNotFoundException;
import teamproject.bloom.mapper.WineMapper;
import teamproject.bloom.model.Wine;
import teamproject.bloom.repository.wine.WineRepository;
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
    public WineResponseWithAllParamsDto getWineById(Long id) {
        return wineMapper.toDtoWithAllParams(findWineById(id));
    }

    @Override
    public Page<WineResponseDto> search(WineSearchParametersDto wineSearchDto) {
        return null;
    }

    private Wine findWineById(Long id) {
        return wineRepository.findById(id).orElseThrow(
                () -> new EntityNotFoundException("Can`t find wine by id " + id)
        );
    }
}
