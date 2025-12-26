package uzumtech.j_gcp.dto.response;

import lombok.*;
import uzumtech.j_gcp.constant.enums.DocumentType;
import uzumtech.j_gcp.constant.enums.Gender;
import java.time.LocalDate;

@Builder
public record UserResponseDto(
        Long id,
        String fullName,
        Integer age,
        String pinfl,
        String phoneNumber,
        Gender gender,
        String email,
        String address,
        String PhotoUrl,
        DocumentType documentType,
        LocalDate issueDate,
        LocalDate expirationDate,
        String citizenship,
        LocalDate deathDate
) {
}