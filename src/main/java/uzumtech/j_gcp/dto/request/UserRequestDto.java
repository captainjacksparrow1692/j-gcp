package uzumtech.j_gcp.dto.request;

import jakarta.validation.constraints.*;
import lombok.*;
import uzumtech.j_gcp.constant.DocumentType;

import java.time.LocalDate;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class UserRequestDto {
    @NotBlank(message = "Имя обязательно")
    private String name;

    @NotBlank(message = "Адрес обязателен")
    private String address;

    @Email(message = "Некорректный email")
    @NotBlank
    private String email;

    @NotBlank
    private String phoneNumber;

    private String photoUrl;

    @Min(0) @Max(150)
    private Integer age;

    @Size(min = 14, max = 14, message = "ПИНФЛ должен состоять из 14 символов")
    @NotBlank
    private String pinfl;

    private DocumentType documentType;

    private LocalDate issueDate;

    @Future(message = "Срок действия документа должен быть в будущем")
    private LocalDate expiryDate;

    @NotBlank
    private String citizenship;
}