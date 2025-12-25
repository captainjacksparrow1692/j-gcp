package uzumtech.j_gcp.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import uzumtech.j_gcp.constant.enums.DocumentType;
import uzumtech.j_gcp.entity.User;

import java.time.LocalDate;
import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Long> {

    // Поиск по ПИНФЛ (возвращает Optional, так как поле уникальное)
    @Query("select u from User u where u.pinfl = :pinfl")
    Optional<User> findByPinfl(String pinfl);

    // Поиск по Email
    @Query("select u from User u where u.email = :email")
    Optional<User> findByEmail(@Param("email") String email);

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
    @Query("select count(u) from User u where u.deathDate is null")
    long countByDeathDateIsNull();
    @Query("select count(u) from User u where u.deathDate is not null")
    long countByDeathDateIsNotNull();

    // Истекшие документы (ИЗМЕНЕНО на Page)
    @Query("select u from User u where u.expiryDate < :date")
     Page<User> findAllByExpiryDateBefore(@Param("date") LocalDate date, Pageable pageable);

    // Документы, истекающие в периоде (ИЗМЕНЕНО на Page)
    @Query("select u from User u where u.expiryDate between :start and :end")
    Page<User> findAllByExpiryDateBetween(@Param("start")LocalDate start,@Param("end") LocalDate end, Pageable pageable);

    // Живые с истекшими документами (ИЗМЕНЕНО на Page)
    @Query("select u from User u where u.deathDate is null and u.expiryDate < :date")
    Page<User> findAllByDeathDateIsNullAndExpiryDateBefore(@Param("date")LocalDate date, Pageable pageable);

    // По типу документа (ИЗМЕНЕНО на Page)
    @Query("select u from User u where u.documentType = :documentType")
    Page<User> findAllByDocumentType(@Param("documentType")DocumentType documentType, Pageable pageable);

    // Проверки существования
    @Query("select count(u) > 0 from User u where u.pinfl = :pinfl")
    boolean existsByPinfl(@Param("pinfl")String pinfl);
    @Query("select count(u) > 0 from User u where u.email = :email")
    boolean existsByEmail(@Param("email")String email);
    @Query("select count(u) > 0 from User u where u.phoneNumber = :phoneNumber")
    boolean existsByPhoneNumber(@Param("phoneNumber")String phoneNumber);
}