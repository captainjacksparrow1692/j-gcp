package uzumtech.j_gcp.dto.request;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.PastOrPresent;
import lombok.Builder;

import java.time.LocalDate;

@Builder
public record MarkDeadRequestDto (

        // Дата смерти пользователя
        @NotNull(message = "Дата смерти обязательна")
        @PastOrPresent(message = "Дата смерти должна быть в прошлом или настоящем")
        LocalDate deathDate) {
}
