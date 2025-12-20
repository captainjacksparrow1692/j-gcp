package uzumtech.j_gcp.dto.request;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.PastOrPresent;
import lombok.Builder;

import java.time.LocalDate;

@Builder
public record MarkDeadRequestDto (
        // дата смерти пользователя
        @NotNull(message = "Death date is required")
        @PastOrPresent(message = "Death date must be in the past or present")
        LocalDate deathDate) {
}
