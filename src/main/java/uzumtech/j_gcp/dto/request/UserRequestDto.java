package uzumtech.j_gcp.dto.request;

import jakarta.validation.constraints.*;
import lombok.*;
import uzumtech.j_gcp.constant.DocumentType;

import java.time.LocalDate;

@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class UserRequestDto {
    // ФИО пользователя
    @NotBlank(message = "Имя обязательно")
    private String fullName;

    // Адрес проживания
    @NotBlank(message = "Адрес обязателен")
    private String address;

    // Email пользователя
    @Email(message = "Некорректный email")
    @NotBlank(message = "Email обязателен")
    private String email;

    // Номер телефона (международный формат)
    @NotBlank(message = "Номер телефона обязателен")
    @Pattern(
            regexp = "^\\+?[0-9]{9,15}$",
            message = "Номер телефона должен быть в международном формате"
    )
    private String phoneNumber;

    // Ссылка на фотографию
    private String photoUrl;

    // Возраст пользователя
    @Min(value = 0, message = "Возраст не может быть отрицательным")
    @Max(value = 150, message = "Возраст не может быть больше 150")
    private Integer age;

    // ПИНФЛ (14 символов)
    @Size(min = 14, max = 14, message = "ПИНФЛ должен состоять из 14 символов")
    @NotBlank(message = "ПИНФЛ обязателен")
    private String pinfl;

    // Тип документа
    @NotNull(message = "Тип документа обязателен")
    private DocumentType documentType;

    // Дата выдачи документа
    @PastOrPresent(message = "Дата выдачи не может быть в будущем")
    private LocalDate issueDate;

    // Дата окончания действия документа
    @Future(message = "Срок действия документа должен быть в будущем")
    private LocalDate expiryDate;

    // Гражданство
    @NotBlank(message = "Гражданство обязательно")
    private String citizenship;

    // Дата смерти (если есть)
    @PastOrPresent(message = "Дата смерти должна быть в прошлом или настоящем")
    private LocalDate deathDate;
}