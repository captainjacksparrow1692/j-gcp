package uzumtech.j_gcp.dto.response;

import lombok.*;
import java.time.LocalDate;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class MarkDeadResponseDto {
    private Long userId;
    private String pinfl;
    private LocalDate deathDate;
    private String status; // Например, "UPDATED" или "SUCCESS"
}