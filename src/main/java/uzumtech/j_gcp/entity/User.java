package uzumtech.j_gcp.entity;

import jakarta.persistence.*;
import lombok.*;
import uzumtech.j_gcp.constant.enums.DocumentType;
import uzumtech.j_gcp.constant.enums.Gender;

import java.time.LocalDate;

@Entity
@Table(name = "users")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder

public class User {
    //создание айди
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    //ФИО
    private String fullName;

    //Адресс
    private String address;

    //уникальная почта
    @Column(nullable = false,  unique = true)
    private String email;

    //пол
    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private Gender gender;
    //тел. номер(string потому что никаких операций не проводиться, просто храним номер)
    @Column(name = "phone_number", nullable = false)
    private String phoneNumber;

    //ссылка на фото
    private String photoUrl;

    //возраст
    private Integer age;

    //пинфл должно быть минимум и максимум 14 знаков
    @Column(nullable = false,   unique = true, length = 14)
    private String pinfl;

    //тип документа(айди,паспорт, метрка, права)
    @Enumerated(EnumType.STRING)
    private DocumentType documentType;

    //дата выдачи
    private LocalDate issueDate;

    //срок истечения
    private LocalDate expirationDate;

    //гражданство
    private String citizenship;

    //дата смерти
    private LocalDate deathDate;
}
