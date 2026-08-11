package j_gcp.dto.request;

import com.fasterxml.jackson.annotation.JsonFormat;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.PastOrPresent;
import lombok.Builder;

import java.time.LocalDate;

@Builder
public record MarkDeadRequestDto (

        // Дата смерти пользователя
        @JsonFormat(pattern = "yyyy-MM-dd")
        @NotNull(message = "Дата смерти обязательна")
        @PastOrPresent(message = "Дата смерти должна быть в прошлом или настоящем")
        LocalDate deathDate) {
}
