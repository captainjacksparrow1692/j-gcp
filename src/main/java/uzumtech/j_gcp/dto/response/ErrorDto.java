package uzumtech.j_gcp.dto.response;

import lombok.Builder;

import java.time.LocalDateTime;

@Builder
public record ErrorDto(
        // сообщение об ошибке
        String message,
        // http статус код ошибки
        int status,
        // время возникновения ошибки
        LocalDateTime timestamp,
        // путь запроса, который вызвал ошибку
        String path
) {
}
