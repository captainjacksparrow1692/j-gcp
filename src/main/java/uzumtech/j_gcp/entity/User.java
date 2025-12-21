package uzumtech.j_gcp.entity;

import jakarta.persistence.*;
import lombok.*;
import uzumtech.j_gcp.constant.DocumentType;

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
    @Column(nullable = false)
    private String fullName;

    //Адресс
    @Column(nullable = false)
    private String address;

    //уникальная почта
    @Column(nullable = false,  unique = true)
    private String email;

    //тел. номер(string потому что никаких операций не проводиться, просто храним номер)
    @Column(nullable = false)
    private String phoneNumber;

    //ссылка на фото
    @Column(name = "photo_url")
    private String photoUrl;

    //возраст
    @Column(nullable = false)
    private Integer age;

    //пинфл должно быть минимум и максимум 14 знаков
    @Column(nullable = false,   unique = true, length = 14)
    private String pinfl;

    //тип документа(айди,паспорт, метрка, права)
    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private DocumentType documentType;

    //дата выдачи
    @Column(nullable = false)
    private LocalDate issueDate;

    //срок истечения
    @Column(nullable = false)
    private LocalDate expiryDate;

    //гражданство
    @Column(nullable = false)
    private String citizenship;

    //дата смерти
    @Column(name = "death_date")
    private LocalDate deathDate;
}
