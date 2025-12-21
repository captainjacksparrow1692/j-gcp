package uzumtech.j_gcp.dto.response;

import lombok.*;
import uzumtech.j_gcp.constant.enums.LifeStatus;

import java.time.LocalDate;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class MarkDeadResponseDto {
    //id пользователя
    private Long userId;
    //пинфл
    private String pinfl;
    //дата смерти
    private LocalDate deathDate;
    //статус операции
    private LifeStatus status;
}