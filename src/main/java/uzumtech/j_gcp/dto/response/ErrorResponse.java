package uzumtech.j_gcp.dto.response;

import lombok.Builder;
import uzumtech.j_gcp.constant.enums.ErrorType;

import java.time.LocalDateTime;

@Builder
public record ErrorResponse(
        LocalDateTime timestamp,
        String message,
        String details,
        int statusCode,
        ErrorType errorType
) {}