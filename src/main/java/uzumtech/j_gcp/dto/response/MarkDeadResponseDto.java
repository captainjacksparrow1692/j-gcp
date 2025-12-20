package uzumtech.j_gcp.dto.response;

import lombok.Builder;

import java.time.LocalDate;

@Builder
public record MarkDeadResponseDto(
        //id пользователя
        Long id,
        //имя пользователя
        String fullName,
        // дата смерти пользователя
        LocalDate deathDate
) {
}
