package teamproject.bloom.mapper;

import java.util.Optional;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Named;
import teamproject.bloom.config.MapperConfig;
import teamproject.bloom.dto.wine.WineResponseDto;
import teamproject.bloom.dto.wine.WineResponseWithAllParamsDto;
import teamproject.bloom.model.Wine;

@Mapper(config = MapperConfig.class)
public interface WineMapper {

    @Mapping(target = "regionId", source = "region.id")
    WineResponseDto toDto(Wine wine);

    @Mapping(target = "grapeId", source = "grape.id")
    @Mapping(target = "regionId", source = "region.id")
    WineResponseWithAllParamsDto toDtoWithAllParams(Wine wine);

    @Named("wineById")
    default Wine wineById(Long id) {
        return Optional.ofNullable(id)
                .map(Wine::new)
                .orElse(null);
    }
}
