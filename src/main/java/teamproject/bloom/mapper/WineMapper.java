package teamproject.bloom.mapper;

import java.util.Optional;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Named;
import teamproject.bloom.config.MapperConfig;
import teamproject.bloom.dto.wine.WineResponseDto;
import teamproject.bloom.model.Grape;
import teamproject.bloom.model.Region;
import teamproject.bloom.model.Wine;

@Mapper(config = MapperConfig.class)
public interface WineMapper {

    @Mapping(target = "grape", source = "grape.name")
    @Mapping(target = "region", source = "region.name")
    WineResponseDto toDto(Wine wine);

    @Named("wineById")
    default Wine wineById(Long id) {
        return Optional.ofNullable(id)
                .map(Wine::new)
                .orElse(null);
    }

    @Named("regionByName")
    default String regionByName(Region region) {
        return Optional.ofNullable(region)
                .map(Region::getName)
                .orElse(null);
    }

    @Named("grapeByName")
    default String grapeByName(Grape grape) {
        return Optional.ofNullable(grape)
                .map(Grape::getName)
                .orElse(null);
    }
}
