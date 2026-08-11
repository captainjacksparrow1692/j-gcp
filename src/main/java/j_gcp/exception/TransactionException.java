package j_gcp.exception;

import org.springframework.http.HttpStatus;
import j_gcp.constant.enums.ErrorType;

import static j_gcp.constant.enums.Error.DATABASE_CONNECTION_ERROR;

public class TransactionException extends BusinessException{
    public TransactionException(int status, String message) {
            super(DATABASE_CONNECTION_ERROR.getCode(), message, ErrorType.INTERNAL, HttpStatus.valueOf(status));
    }
}
