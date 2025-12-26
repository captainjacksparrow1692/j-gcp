package uzumtech.j_gcp.exception;

import org.springframework.http.HttpStatus;
import uzumtech.j_gcp.constant.enums.ErrorType;

import static uzumtech.j_gcp.constant.enums.Error.USER_NOT_FOUND;

public class UserNotFoundException extends BusinessException {
    public UserNotFoundException(int status, String message) {
        super(USER_NOT_FOUND.getCode(), message, ErrorType.INTERNAL, HttpStatus.valueOf(status));
    }
}
