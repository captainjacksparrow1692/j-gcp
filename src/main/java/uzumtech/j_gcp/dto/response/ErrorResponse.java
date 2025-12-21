package uzumtech.j_gcp.dto.response;

import lombok.Builder;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
@Builder
public class ErrorResponse {

    // Время возникновения ошибки
    private final LocalDateTime timestamp;

    // Сообщение об ошибке
    private final String message;

    // Дополнительные детали ошибки
    private final String details;

    // HTTP статус-код
    private final int statusCode;
}