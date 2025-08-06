package teamproject.bloom.mapper;

import org.mapstruct.Mapper;
import teamproject.bloom.config.MapperConfig;
import teamproject.bloom.dto.user.UserLoginResponseDto;
import teamproject.bloom.model.User;

@Mapper(config = MapperConfig.class)
public interface UserMapper {
    User createUser(String userName);

    UserLoginResponseDto toResponseDto(String token);
}
