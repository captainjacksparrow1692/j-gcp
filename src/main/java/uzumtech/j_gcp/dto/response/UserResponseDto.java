package uzumtech.j_gcp.dto.response;

import lombok.*;
import uzumtech.j_gcp.constant.DocumentType;

import java.time.LocalDate;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class UserResponseDto {
    //id пользователя
    private Long id;
    //ФИО
    private String fullName;
    //адрес
    private String address;
    //почта
    private String email;
    //тел.номер
    private String phoneNumber;
    //ссылка на фото
    private String photoUrl;
    //возраст
    private Integer age;
    //пинфл
    private String pinfl;
    //тип документа
    private DocumentType documentType;
    //дата выдачи
    private LocalDate issueDate;
    //срок истечения документа
    private LocalDate expiryDate;
    //гражданство
    private String citizenship;
    //дата смерти
    private LocalDate deathDate; // null, если пользователь жив
    // Вспомогательное поле для фронтенда
    public boolean isAlive() {
        return deathDate == null;
    }
}