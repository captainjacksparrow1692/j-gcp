package j_gcp.exception;

import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import j_gcp.constant.enums.ErrorType;

import static j_gcp.constant.enums.Error.INTERNAL_SERVICE_ERROR;

public class HttpServerException extends BusinessException {

    public HttpServerException(String message, HttpStatusCode status) {
        super(
                INTERNAL_SERVICE_ERROR.getCode(),
                message,
                ErrorType.EXTERNAL,
                HttpStatus.valueOf(status.value())
        );
    }
}