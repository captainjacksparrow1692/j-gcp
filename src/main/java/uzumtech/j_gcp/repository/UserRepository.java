package uzumtech.j_gcp.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import uzumtech.j_gcp.constant.DocumentType;
import uzumtech.j_gcp.entity.User;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {
    // 1. простые поиски по полям
    Optional<User> findByPinfl(String pinfl);

    // 2. поиск по именам (регистронезависимый)
    Page<User> FindByNameContainingIgnoreClass(String name);

    // 3. статут жизни
    List<User> FindAllByDeathDateIsAlive();
    List<User> FindAllByDeathDateIsDead();

    long countAllByDeathDateIsAlive();
    long countAllByDeathDateIsDead();

    // 4. поиск по документам
    // Просроченные документы: дата истечения < текущей даты
    List<User> findAllByExpiryDateBefore(LocalDate date);

    // Документы, истекающие в диапазоне
    List<User> findAllByExpiryDateBetween(LocalDate start, LocalDate end);

    // По типу документа
    List<User> findAllByDocumentType(DocumentType documentType);

    // 5. Микс-запрос: Живые пользователи с просроченными документами
    List<User> findAllByDeathDateIsNullAndExpiryDateBefore(LocalDate date);
}
