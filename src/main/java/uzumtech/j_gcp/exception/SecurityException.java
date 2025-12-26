package uzumtech.j_gcp.exception;

import org.springframework.http.HttpStatus;
import uzumtech.j_gcp.constant.enums.ErrorType;

import static uzumtech.j_gcp.constant.enums.Error.UNAUTHORIZED;

public class SecurityException extends BusinessException{
    public SecurityException(int status, String message) {
        super(UNAUTHORIZED.getCode(), message, ErrorType.INTERNAL, HttpStatus.valueOf(status));
    }
}
