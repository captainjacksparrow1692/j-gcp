package j_gcp.dto.response;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.*;
import j_gcp.constant.enums.DocumentType;
import j_gcp.constant.enums.Gender;
import java.time.LocalDate;

@JsonFormat(pattern = "yyyy-MM-dd")
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
        String photoUrl,
        DocumentType documentType,
        LocalDate issueDate,
        LocalDate expirationDate,
        String citizenship,
        LocalDate deathDate
) {
}