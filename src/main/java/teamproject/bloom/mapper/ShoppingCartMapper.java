package teamproject.bloom.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import teamproject.bloom.config.MapperConfig;
import teamproject.bloom.dto.shoppingcart.ShoppingCartResponseDto;
import teamproject.bloom.model.ShoppingCart;

@Mapper(config = MapperConfig.class, uses = CartItemMapper.class)
public interface ShoppingCartMapper {
    @Mapping(target = "userId", source = "user.id")
    @Mapping(target = "itemCartDtos", source = "cartItems")
    ShoppingCartResponseDto toDto(ShoppingCart shoppingCart);
}
