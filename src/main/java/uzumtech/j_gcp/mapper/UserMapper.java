package uzumtech.j_gcp.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import uzumtech.j_gcp.constant.enums.LifeStatus;
import uzumtech.j_gcp.dto.request.UserRequestDto;
import uzumtech.j_gcp.dto.response.MarkDeadResponseDto;
import uzumtech.j_gcp.dto.response.UserResponseDto;
import uzumtech.j_gcp.entity.User;

@Mapper(componentModel = "spring", imports = {LifeStatus.class})
public interface UserMapper {

    // 1. Создание сущности из Request DTO (как в примере с заказом)
    @Mapping(target = "id", ignore = true)
    @Mapping(target = "deathDate", source = "deathDate")
    User toEntity(UserRequestDto requestDto);

    // 2. Основной маппинг в ответ (Entity -> ResponseDto)
    UserResponseDto toResponseDto(User user);

    // 3. Маппинг для регистрации смерти (Entity -> MarkDeadResponseDto)
    @Mapping(target = "userId", source = "id") // Маппим id из User в userId из DTO
    @Mapping(target = "status", expression = "java(user.getDeathDate() != null ? LifeStatus.DECEASED : LifeStatus.ALIVE)")
    MarkDeadResponseDto toMarkDeadResponseDto(User user);
}