package uzumtech.j_gcp.exception;

import org.springframework.http.HttpStatus;
import uzumtech.j_gcp.constant.enums.ErrorType;

import static uzumtech.j_gcp.constant.enums.Error.VALIDATION_FAILED;

public class ValidationException extends BusinessException{
    public ValidationException(String message, HttpStatus status) {
        super(VALIDATION_FAILED.getCode(), message, ErrorType.VALIDATION, HttpStatus.valueOf(status.value()));
    }
}
