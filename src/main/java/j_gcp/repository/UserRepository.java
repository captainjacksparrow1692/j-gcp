package j_gcp.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import j_gcp.constant.enums.DocumentType;
import j_gcp.constant.enums.Gender;
import j_gcp.entity.User;

import java.time.LocalDate;
import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Long> {

    // Поиск по ПИНФЛ (возвращает Optional, так как поле уникальное)
    Optional<User> findByPinfl(String pinfl);

    // Поиск по Email
    Optional<User> findByEmail(String email);

    // Поиск по ФИО с игнорированием регистра (Like search)
    @Query("select u from User u where lower(u.fullName) like lower(concat('%', :fullName, '%'))")
    Page<User> findAllByFullNameContainingIgnoreCase(@Param("fullName") String fullName, Pageable pageable);

    // Поиск живых пользователей (ИЗМЕНЕНО на Page)
    @Query("select u from User u where u.deathDate is null")
    Page<User> findAllByDeathDateIsNull(Pageable pageable);

    // Поиск умерших пользователей (ИЗМЕНЕНО на Page)
    @Query("select u from User u where u.deathDate is not null")
    Page<User> findAllByDeathDateIsNotNull(Pageable pageable);

    // Подсчет (остается как есть)
    long countByDeathDateIsNull();
    long countByDeathDateIsNotNull();

    // Истекшие документы (ИЗМЕНЕНО на Page)
    @Query("select u from User u where u.expirationDate < :date")
     Page<User> findAllByExpirationDateBefore(@Param("date") LocalDate date, Pageable pageable);

    // Документы, истекающие в периоде (ИЗМЕНЕНО на Page)
    @Query("select u from User u where u.expirationDate between :start and :end")
    Page<User> findAllByExpirationDateBetween(@Param("start")LocalDate start,@Param("end") LocalDate end, Pageable pageable);

    // Живые с истекшими документами (ИЗМЕНЕНО на Page)
    @Query("select u from User u where u.deathDate is null and u.expirationDate < :date")
    Page<User> findAllByDeathDateIsNullAndExpirationDateBefore(@Param("date")LocalDate date, Pageable pageable);

    // По типу документа (ИЗМЕНЕНО на Page)
    Page<User> findAllByDocumentType(@Param("documentType")DocumentType documentType, Pageable pageable);

    // Подсчет по полу
    @Query("SELECT COUNT(u) FROM User u WHERE u.gender = :gender")
    long countByGender(@Param("gender") Gender gender);

    // Поиск и подсчет по гражданству
    Page<User> findByCitizenship(String citizenship, Pageable pageable);

    long countByCitizenship(String citizenship);

    // Проверки существования
    boolean existsByPinfl(String pinfl);
    boolean existsByEmail(String email);
    boolean existsByPhoneNumber(String phoneNumber);
}