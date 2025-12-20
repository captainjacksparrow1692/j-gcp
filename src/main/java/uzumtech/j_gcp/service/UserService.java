package uzumtech.j_gcp.service;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import uzumtech.j_gcp.constant.DocumentType;
import uzumtech.j_gcp.dto.request.UserRequestDto;
import uzumtech.j_gcp.dto.response.MarkDeadResponseDto;
import uzumtech.j_gcp.dto.response.UserResponseDto;

import java.time.LocalDate;
import java.util.List;

public interface UserService {

    // Основные операции CRUD
    UserResponseDto createUser(UserRequestDto userRequestDto);

    Page<UserResponseDto> getAllUsers(PageRequest pageRequest);

    UserResponseDto getUserById(Long id);

    UserResponseDto getUserByUserPinfl(String pinfl);

    // Логика статуса жизни
    boolean isUserAlive(Long id);

    MarkDeadResponseDto markUserAsDead(Long id, LocalDate deathDate);

    // Поисковые методы
    List<UserResponseDto> searchUsersByName(String fullName);

    List<UserResponseDto> getAllAliveUsers();

    List<UserResponseDto> getAllDeadUsers();

    // Методы подсчета (статистика)
    long getAllDeadUsersCount();

    long getAllAliveUsersCount();

    // Работа с документами
    List<UserResponseDto> getUsersWithExpiredDocuments();

    List<UserResponseDto> getUsersWithDocumentsExpiringBetween(LocalDate start, LocalDate end);

    // Поиск по типу документа (используем ваш Enum)
    List<UserResponseDto> getUsersByDocumentType(DocumentType documentType);

    // Метод для совместимости (если где-то еще используется системный DocumentType)
    List<UserResponseDto> getUsersByDocumentType(org.w3c.dom.DocumentType documentType);

    // Сложные запросы (микс)
    List<UserResponseDto> getAliveUsersWithExpiredDocuments();
}