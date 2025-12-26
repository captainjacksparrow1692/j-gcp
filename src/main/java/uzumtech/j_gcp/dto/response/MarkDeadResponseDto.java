package uzumtech.j_gcp.dto.response;

import lombok.*;
import uzumtech.j_gcp.constant.enums.LifeStatus;

import java.time.LocalDate;

@Builder
public record MarkDeadResponseDto(
        Long userId,
        String pinfl,
        LocalDate deathDate,
        LifeStatus status
) {
}