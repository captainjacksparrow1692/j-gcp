package uzumtech.j_gcp.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import uzumtech.j_gcp.constant.enums.DocumentType;
import uzumtech.j_gcp.entity.User;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {
    //поиск по пинфлу
    Optional<User> findByPinfl(String pinfl);

    //поиск пользователей по ФИО (без учёта регистра) с пагинацией
    Page<User> findAllByFullNameContainingIgnoreCase(String fullName, Pageable pageable);

    //поиск всех живых пользователей (дата смерти отсутствует)
    List<User> findAllByDeathDateIsNull();

    //поиск всех умерших пользователей
    List<User> findAllByDeathDateIsNotNull();

    // посчитать количество живых и не живых пользователей
    long countByDeathDateIsNull();
    long countByDeathDateIsNotNull();

    //найти пользователей с истёкшим сроком действия документа
    List<User> findAllByExpiryDateBefore(LocalDate date);
    //найти пользователей с истёкающим сроком действия документа
    List<User> findAllByExpiryDateBetween(LocalDate start, LocalDate end);
    //найти живых пользователей с истёкшим сроком действия документа
    List<User> findAllByDeathDateIsNullAndExpiryDateBefore(LocalDate date);
    //найти пользователей по типу документа
    List<User> findAllByDocumentType(DocumentType documentType);

    // Проверка существования для валидации уникальности
    boolean existsByPinfl(String pinfl);
    boolean existsByEmail(String email);
    boolean existsByPhoneNumber(String phoneNumber);
}