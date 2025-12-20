package uzumtech.j_gcp.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import uzumtech.j_gcp.constant.DocumentType;
import uzumtech.j_gcp.entity.User;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {

    Optional<User> findByPinfl(String pinfl);

    // Поиск по ФИО с пагинацией (исправлено IgnoreClass -> IgnoreCase)
    Page<User> findAllByFullNameContainingIgnoreCase(String fullName, Pageable pageable);

    // Список для поиска без пагинации
    List<User> findAllByFullNameContainingIgnoreCase(String fullName);

    // Статус жизни: если deathDate == null, значит жив
    List<User> findAllByDeathDateIsNull();      // Живые
    List<User> findAllByDeathDateIsNotNull();   // Мертвые

    long countByDeathDateIsNull();
    long countByDeathDateIsNotNull();

    // Поиск по документам
    List<User> findAllByExpiryDateBefore(LocalDate date);
    List<User> findAllByExpiryDateBetween(LocalDate start, LocalDate end);
    List<User> findAllByDocumentType(DocumentType documentType);

    // Живые с просроченными документами
    List<User> findAllByDeathDateIsNullAndExpiryDateBefore(LocalDate date);
}