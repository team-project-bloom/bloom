package teamproject.bloom.service.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import teamproject.bloom.dto.wine.WineResponseDto;
import teamproject.bloom.dto.wine.WineSearchParametersDto;
import teamproject.bloom.exception.unchecked.EntityNotFoundException;
import teamproject.bloom.mapper.WineMapper;
import teamproject.bloom.model.Wine;
import teamproject.bloom.repository.wine.WineRepository;
import teamproject.bloom.repository.wine.WineSpecificationBuilder;
import teamproject.bloom.service.WineService;

@Service
@RequiredArgsConstructor
public class WineServiceImpl implements WineService {
    private final WineRepository wineRepository;
    private final WineMapper wineMapper;
    private final WineSpecificationBuilder wineSpecificationBuilder;

    @Override
    public Page<WineResponseDto> getAll(Pageable pageable) {
        return wineRepository.findAll(pageable).map(wineMapper::toDto);
    }

    @Override
    public WineResponseDto getWineById(Long id) {
        return wineMapper.toDto(findWineById(id));
    }

    @Override
    public Page<WineResponseDto> search(WineSearchParametersDto params,
                                        Pageable pageable) {
        Specification<Wine> wineSpecification = wineSpecificationBuilder.build(params);
        return wineRepository.findAll(wineSpecification, pageable)
                .map(wineMapper::toDto);
    }

    private Wine findWineById(Long id) {
        return wineRepository.findById(id).orElseThrow(
                () -> new EntityNotFoundException("Can`t find wine by id " + id)
        );
    }
}
