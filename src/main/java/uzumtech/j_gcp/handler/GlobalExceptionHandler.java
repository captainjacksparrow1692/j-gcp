package uzumtech.j_gcp.handler;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.context.request.WebRequest;
import uzumtech.j_gcp.dto.response.ErrorResponse;

import java.time.LocalDateTime;

@RestControllerAdvice
public class GlobalExceptionHandler {
    //сгруппировать все ошибки через бизнес ошибку, +системные ошибки(под кривые запросы), дописать ошибки
    // 404 NOT FOUND
    @ExceptionHandler(ResourceNotFoundException.class)
    public ResponseEntity<ErrorResponse> handleResourceNotFound(
            ResourceNotFoundException ex, WebRequest request) {

        return buildError(ex.getMessage(), request, HttpStatus.NOT_FOUND);
    }

    // 409 CONFLICT
    @ExceptionHandler(UserAlreadyExistsException.class)
    public ResponseEntity<ErrorResponse> handleUserExists(
            UserAlreadyExistsException ex, WebRequest request) {

        return buildError(ex.getMessage(), request, HttpStatus.CONFLICT);
    }

    // 400 BAD REQUEST
    @ExceptionHandler(InvalidBusinessLogicException.class)
    public ResponseEntity<ErrorResponse> handleBusinessLogic(
            InvalidBusinessLogicException ex, WebRequest request) {

        return buildError(ex.getMessage(), request, HttpStatus.BAD_REQUEST);
    }

    // 500 INTERNAL SERVER ERROR
    @ExceptionHandler(Exception.class)
    public ResponseEntity<ErrorResponse> handleGlobalException(
            Exception ex, WebRequest request) {

        return buildError(
                "Internal Server Error",
                request,
                HttpStatus.INTERNAL_SERVER_ERROR
        );
    }

    // Общий метод построения ошибки
    private ResponseEntity<ErrorResponse> buildError(
            String message, WebRequest request, HttpStatus status) {

        ErrorResponse error = ErrorResponse.builder()
                .timestamp(LocalDateTime.now())
                .message(message)
                .details(request.getDescription(false))
                .statusCode(status.value())
                .build();

        return new ResponseEntity<>(error, status);
    }
}
