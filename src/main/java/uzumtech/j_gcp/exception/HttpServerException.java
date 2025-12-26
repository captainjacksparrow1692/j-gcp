package uzumtech.j_gcp.exception;

import org.springframework.http.HttpStatus;
import uzumtech.j_gcp.constant.enums.ErrorType;

import static uzumtech.j_gcp.constant.enums.Error.INTERNAL_SERVICE_ERROR;

public class HttpServerException extends BusinessException{
    public HttpServerException(String message, HttpStatus status) {
        super(INTERNAL_SERVICE_ERROR.getCode(), message, ErrorType.INTERNAL, HttpStatus.valueOf(status.value()));
    }
}
