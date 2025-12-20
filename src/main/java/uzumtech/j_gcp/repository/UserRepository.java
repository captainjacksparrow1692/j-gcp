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

    Page<User> findAllByFullNameContainingIgnoreCase(String fullName, Pageable pageable);
    List<User> findAllByFullNameContainingIgnoreCase(String fullName);

    List<User> findAllByDeathDateIsNull();
    List<User> findAllByDeathDateIsNotNull();

    long countByDeathDateIsNull();
    long countByDeathDateIsNotNull();

    List<User> findAllByExpiryDateBefore(LocalDate date);
    List<User> findAllByExpiryDateBetween(LocalDate start, LocalDate end);
    List<User> findAllByDocumentType(DocumentType documentType);

    List<User> findAllByDeathDateIsNullAndExpiryDateBefore(LocalDate date);

    // --- ДОПИСАТЬ СЮДА ---

    // Проверка существования для валидации уникальности
    boolean existsByPinfl(String pinfl);

    boolean existsByEmail(String email);

    boolean existsByPhoneNumber(String phoneNumber);
}