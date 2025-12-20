package uzumtech.j_gcp.dto.response;

import lombok.*;
import uzumtech.j_gcp.constant.DocumentType;

import java.time.LocalDate;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class UserResponseDto {
    private Long id;
    private String name;
    private String address;
    private String email;
    private String phoneNumber;
    private String photoUrl;
    private Integer age;
    private String pinfl;
    private DocumentType documentType;
    private LocalDate issueDate;
    private LocalDate expiryDate;
    private String citizenship;
    private LocalDate deathDate; // null, если пользователь жив

    // Вспомогательное поле для фронтенда
    public boolean isAlive() {
        return deathDate == null;
    }
}