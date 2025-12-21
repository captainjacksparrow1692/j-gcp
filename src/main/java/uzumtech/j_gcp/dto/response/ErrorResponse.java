package uzumtech.j_gcp.dto.response;

import lombok.Builder;
import java.time.LocalDateTime;

@Builder
public class ErrorResponse {
    //Время возникновения ошибки
    private LocalDateTime timestamp;

    //Сообщение об ошибке
    private String message;

    //Дополнительные детали ошибки
    private String details;

    //HTTP статус-код
    private int statusCode;
}