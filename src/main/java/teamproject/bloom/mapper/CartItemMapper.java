package teamproject.bloom.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import teamproject.bloom.config.MapperConfig;
import teamproject.bloom.dto.cartitem.CartItemDto;
import teamproject.bloom.dto.cartitem.CartItemRequestDto;
import teamproject.bloom.model.CartItem;

@Mapper(config = MapperConfig.class, uses = WineMapper.class)
public interface CartItemMapper {
    @Mapping(target = "wine", source = "wineId", qualifiedByName = "wineById")
    CartItem toModel(CartItemRequestDto itemDto);

    @Mapping(target = "wineId", source = "wine.id")
    @Mapping(target = "variety", source = "wine.variety")
    @Mapping(target = "price", source = "wine.price")
    CartItemDto toDto(CartItem cartItem);
}
