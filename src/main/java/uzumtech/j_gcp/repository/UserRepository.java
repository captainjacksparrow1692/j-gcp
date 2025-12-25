package uzumtech.j_gcp.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import uzumtech.j_gcp.constant.enums.DocumentType;
import uzumtech.j_gcp.entity.User;

import java.time.LocalDate;
import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Long> {

    // Поиск по пинфлу (пагинация обычно не нужна, так как это уникальное поле)
    Optional<User> findByPinfl(String pinfl);

    // Поиск по ФИО (уже было верно)
    Page<User> findAllByFullNameContainingIgnoreCase(String fullName, Pageable pageable);

    // Поиск живых пользователей (ИЗМЕНЕНО на Page)
    Page<User> findAllByDeathDateIsNull(Pageable pageable);

    // Поиск умерших пользователей (ИЗМЕНЕНО на Page)
    Page<User> findAllByDeathDateIsNotNull(Pageable pageable);

    // Подсчет (остается как есть)
    long countByDeathDateIsNull();
    long countByDeathDateIsNotNull();

    // Истекшие документы (ИЗМЕНЕНО на Page)
    Page<User> findAllByExpiryDateBefore(LocalDate date, Pageable pageable);

    // Документы, истекающие в периоде (ИЗМЕНЕНО на Page)
    Page<User> findAllByExpiryDateBetween(LocalDate start, LocalDate end, Pageable pageable);

    // Живые с истекшими документами (ИЗМЕНЕНО на Page)
    Page<User> findAllByDeathDateIsNullAndExpiryDateBefore(LocalDate date, Pageable pageable);

    // По типу документа (ИЗМЕНЕНО на Page)
    Page<User> findAllByDocumentType(DocumentType documentType, Pageable pageable);

    // Проверки существования
    boolean existsByPinfl(String pinfl);
    boolean existsByEmail(String email);
    boolean existsByPhoneNumber(String phoneNumber);
}