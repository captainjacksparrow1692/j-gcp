package uzumtech.j_gcp.dto.response;

import lombok.Builder;

import java.time.LocalDateTime;

@Builder
public record ErrorResponse(
        LocalDateTime timestamp,
        String message,
        String details,
        int statusCode
) {}