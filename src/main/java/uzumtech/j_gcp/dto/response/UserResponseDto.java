package uzumtech.j_gcp.dto.response;

import lombok.Builder;
import org.w3c.dom.DocumentType;

import java.time.LocalDate;

@Builder
public record UserResponseDto(
        //id пользователя
        Long id,
        //имя пользователя
        String fullName,
        //адрес проживания
        String address,
        //номер телефона
        String phoneNumber,
        //электронная почта
        String email,
        //ссылка на фотографию
        String photoUrl,
        //персональный идентификационный номер (14 символов)
        String pinfl,
        //возраст пользователя
        Integer age,
        //тип документа (паспорт, id-карта и др.)
        DocumentType documentType,
        //дата выдачи документа
        LocalDate issueDate,
        //дата окончания срока действия документа
        LocalDate expiryDate,
        //гражданство
        String citizenship,
        //дата смерти (если применимо)
        LocalDate deathDate
) {
}
