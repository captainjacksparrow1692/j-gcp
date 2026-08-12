package j_gcp.dto.response;

import j_gcp.constant.enums.LifeStatus;
import lombok.Builder;

import java.time.LocalDate;

@Builder
public record MarkDeadResponseDto(
        Long userId,
        String pinfl,
        LocalDate deathDate,
        LifeStatus status
) {
}