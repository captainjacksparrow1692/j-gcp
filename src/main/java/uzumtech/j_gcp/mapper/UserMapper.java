package uzumtech.j_gcp.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import uzumtech.j_gcp.constant.enums.LifeStatus;
import uzumtech.j_gcp.dto.request.UserRequestDto;
import uzumtech.j_gcp.dto.response.MarkDeadResponseDto;
import uzumtech.j_gcp.dto.response.UserResponseDto;
import uzumtech.j_gcp.entity.User;

// imports для использования LifeStatus внутри выражений java(...)
@Mapper(componentModel = "spring", imports = {LifeStatus.class})
public interface UserMapper {

    // 1. Из DTO в Сущность
    User toEntity(UserRequestDto requestDto);

    // 2. Из Сущности в основной DTO ответа
    UserResponseDto toResponseDto(User user);

    // 3. Маппинг для статуса смерти
    @Mapping(target = "userId", source = "id") // Разные имена полей
    @Mapping(target = "status", expression = "java(user.getDeathDate() != null ? LifeStatus.DECEASED : LifeStatus.ALIVE)")
    MarkDeadResponseDto toMarkDeadResponseDto(User user);
}