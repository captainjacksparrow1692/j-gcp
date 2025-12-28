package uzumtech.j_gcp.exception;

import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import uzumtech.j_gcp.constant.enums.ErrorType;

import static uzumtech.j_gcp.constant.enums.Error.HTTP_CLIENT_ERROR_CODE;

public class HttpClientException extends BusinessException {

    public HttpClientException(String message, HttpStatusCode status) {
        super(HTTP_CLIENT_ERROR_CODE.getCode(), message, ErrorType.EXTERNAL, HttpStatus.valueOf(status.value()));
    }
}