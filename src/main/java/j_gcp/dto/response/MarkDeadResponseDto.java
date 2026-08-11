package j_gcp.dto.response;

import lombok.*;
import j_gcp.constant.enums.LifeStatus;

import java.time.LocalDate;

@Builder
public record MarkDeadResponseDto(
        Long userId,
        String pinfl,
        LocalDate deathDate,
        LifeStatus status
) {
}