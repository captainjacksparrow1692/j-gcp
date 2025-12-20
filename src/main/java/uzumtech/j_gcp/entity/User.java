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
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String fullName;
    @Column(nullable = false)
    private String address;
    @Column(nullable = false,  unique = true)
    private String email;
    @Column(nullable = false)
    private String phoneNumber;
    @Column(name = "photo_url",nullable = true)
    private String photoUrl;
    @Column(nullable = false)
    private Integer age;
    @Column(nullable = false,   unique = true, length = 14)
    private String pinfl;
    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private DocumentType documentType;
    @Column(nullable = false)
    private LocalDate issueDate;
    @Column(nullable = false)
    private LocalDate expiryDate;
    @Column(nullable = false)
    private String citizenship;
    @Column(name = "death_date",nullable = true)
    private LocalDate deathDate;
}
