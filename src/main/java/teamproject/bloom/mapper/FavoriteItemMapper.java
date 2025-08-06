package teamproject.bloom.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import teamproject.bloom.config.MapperConfig;
import teamproject.bloom.dto.favoriteitem.FavoriteItemResponseDto;
import teamproject.bloom.model.FavoriteItem;
import teamproject.bloom.model.User;
import teamproject.bloom.model.Wine;

@Mapper(config = MapperConfig.class)
public interface FavoriteItemMapper {
    @Mapping(target = "user", source = "user")
    @Mapping(target = "wine", source = "wine")
    @Mapping(target = "id", ignore = true)
    FavoriteItem toModel(User user, Wine wine);

    @Mapping(target = "userId", source = "user.id")
    @Mapping(target = "wineId", source = "wine.id")
    FavoriteItemResponseDto toResponseDto(FavoriteItem favoriteItem);

}
