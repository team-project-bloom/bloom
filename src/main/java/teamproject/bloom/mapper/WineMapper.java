package teamproject.bloom.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import teamproject.bloom.config.MapperConfig;
import teamproject.bloom.dto.wine.WineResponseDto;
import teamproject.bloom.dto.wine.WineWithAllParamsDto;
import teamproject.bloom.model.Wine;

@Mapper(config = MapperConfig.class)
public interface WineMapper {

    @Mapping(target = "regionId", source = "region.id")
    WineResponseDto toDto(Wine wine);

    @Mapping(target = "grapeId", source = "grape.id")
    @Mapping(target = "regionId", source = "region.id")
    WineWithAllParamsDto toDtoWithAllParams(Wine wine);
}
