package uzumtech.j_gcp.handler;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import uzumtech.j_gcp.constant.enums.Error;
import uzumtech.j_gcp.constant.enums.ErrorType;
import uzumtech.j_gcp.dto.response.ErrorResponse;
import uzumtech.j_gcp.exception.BusinessException;

import java.time.LocalDateTime;
import java.util.stream.Collectors;

@Slf4j
@RestControllerAdvice
public class GlobalExceptionHandler {

    // 1. Кастомные бизнес-ошибки
    @ExceptionHandler(BusinessException.class)
    public ResponseEntity<ErrorResponse> handleBusiness(BusinessException ex) {
        log.error("Business error: {}", ex.getMessage());
        return buildResponse(ex.getHttpStatus(), ex.getCode(), ex.getMessage(), null, ex.getErrorType());
    }

    // 2. Ошибки валидации (аннотации @Valid)
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ErrorResponse> handleValidation(MethodArgumentNotValidException ex) {
        String details = ex.getBindingResult().getFieldErrors().stream()
                .map(f -> f.getField() + ": " + f.getDefaultMessage())
                .collect(Collectors.joining(", "));

        return buildResponse(
                HttpStatus.BAD_REQUEST,
                Error.VALIDATION_FAILED.getCode(),
                Error.VALIDATION_FAILED.getMessage(),
                details,
                ErrorType.VALIDATION
        );
    }

    // 3. Общий перехватчик (Safety Net)
    @ExceptionHandler(Exception.class)
    public ResponseEntity<ErrorResponse> handleAll(Exception ex) {
        log.error("Unexpected error: ", ex);
        return buildResponse(
                HttpStatus.INTERNAL_SERVER_ERROR,
                Error.INTERNAL_SERVICE_ERROR.getCode(),
                Error.INTERNAL_SERVICE_ERROR.getMessage(),
                ex.getMessage(),
                ErrorType.INTERNAL
        );
    }

    private ResponseEntity<ErrorResponse> buildResponse(
            HttpStatus status,
            int code,
            String message,
            String details,
            ErrorType type) {

        return ResponseEntity.status(status).body(
                ErrorResponse.builder()
                        .timestamp(LocalDateTime.now())
                        .statusCode(code)
                        .message(message)
                        .details(details)
                        .errorType(type)
                        .build()
        );
    }
}