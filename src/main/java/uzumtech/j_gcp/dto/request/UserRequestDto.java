package uzumtech.j_gcp.dto.request;

import jakarta.validation.constraints.*;
import lombok.*;
import uzumtech.j_gcp.constant.enums.DocumentType;
import uzumtech.j_gcp.constant.enums.Gender;

import java.time.LocalDate;

@Builder
public record UserRequestDto(
        @NotBlank(message = "Имя обязательно")
        String fullName,

        @NotNull(message = "Gender is required")
        Gender gender,

        @NotBlank(message = "Адрес обязателен")
        String address,

        @Email(message = "Некорректный email")
        @NotBlank(message = "Email обязателен")
        String email,

        @NotBlank(message = "Номер телефона обязателен")
        @Pattern(
                regexp = "^\\+\\d{9,15}$",
                message = "Номер телефона должен быть в международном формате"
        )
        String phoneNumber,

        @NotBlank(message = "Photo_URL обязателен")
        String photoUrl,

        @NotBlank(message = "Возраст обязателен")
        @Min(value = 0, message = "Возраст не может быть отрицательным")
        @Max(value = 150, message = "Возраст не может быть больше 150")
        Integer age,

        @Size(min = 14, max = 14, message = "ПИНФЛ должен состоять из 14 символов")
        @NotBlank(message = "ПИНФЛ обязателен")
        String pinfl,

        @NotNull(message = "Тип документа обязателен")
        DocumentType documentType,

        @PastOrPresent(message = "Дата выдачи не может быть в будущем")
        LocalDate issueDate,

        @Future(message = "Срок действия документа должен быть в будущем")
        LocalDate expiryDate,

        @NotBlank(message = "Гражданство обязательно")
        String citizenship,
        @PastOrPresent(message = "Дата смерти должна быть в прошлом или настоящем")
        LocalDate deathDate

) {}