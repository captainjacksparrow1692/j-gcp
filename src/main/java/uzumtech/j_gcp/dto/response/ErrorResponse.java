package uzumtech.j_gcp.dto.response;

import lombok.AllArgsConstructor;
import lombok.Data;
import java.time.LocalDateTime;

@Data
@AllArgsConstructor
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