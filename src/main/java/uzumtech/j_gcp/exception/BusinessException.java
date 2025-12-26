package uzumtech.j_gcp.exception;

import lombok.Getter;
import lombok.ToString;
import lombok.experimental.FieldDefaults;
import org.springframework.http.HttpStatus;
import uzumtech.j_gcp.constant.enums.ErrorType;

@Getter
@ToString
@FieldDefaults(level = lombok.AccessLevel.PRIVATE, makeFinal = true)
public class BusinessException extends RuntimeException {
    int code;
    String message;
    HttpStatus httpStatus;
    ErrorType errorType;

    public BusinessException(int code, String message, ErrorType errorType, HttpStatus status) {
        super(message);
        this.code = code;
        this.message = message;
        this.httpStatus = status;
        this.errorType = errorType;
    }
}
