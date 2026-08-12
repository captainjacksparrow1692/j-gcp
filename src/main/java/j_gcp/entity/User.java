package j_gcp.entity;

import j_gcp.constant.enums.DocumentType;
import j_gcp.constant.enums.Gender;
import j_gcp.constant.enums.LifeStatus;
import j_gcp.constant.enums.Status;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;

@Entity
@Table(name = "users")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
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

    //телефонный номер(string потому что никаких операций не проводиться, просто храним номер)
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

    @Enumerated(EnumType.STRING)
    private Status status = Status.PENDING;

    @Enumerated(EnumType.STRING)
    private LifeStatus lifeStatus = LifeStatus.ALIVE;
}
