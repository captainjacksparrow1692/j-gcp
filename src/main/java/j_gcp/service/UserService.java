package j_gcp.service;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import j_gcp.constant.enums.DocumentType;
import j_gcp.constant.enums.Gender;
import j_gcp.dto.request.UserRequestDto;
import j_gcp.dto.response.MarkDeadResponseDto;
import j_gcp.dto.response.UserResponseDto;

import java.time.LocalDate;
public interface UserService {

    //CRUD
    UserResponseDto createUser(UserRequestDto userRequestDto);

    Page<UserResponseDto> getAllUsers(Pageable pageable);

    UserResponseDto getUserById(Long id);

    UserResponseDto getUserByUserPinfl(String pinfl);

    UserResponseDto getUserByEmail(String email);

    Page<UserResponseDto> getUserByCitizenship(String citizenship, Pageable pageable);

    //Статус жизни
    boolean isUserAlive(Long id);

    MarkDeadResponseDto markUserAsDead(Long id, LocalDate deathDate);

    // Поиск
    Page<UserResponseDto> searchUsersByName(String fullName, Pageable pageable);

    Page<UserResponseDto> getAllAliveUsers(Pageable pageable);

    Page<UserResponseDto> getAllDeadUsers(Pageable pageable);

    //Статистика
    enum Status {
        ALIVE, DEAD
    }

    long countUsersByCitizenship(String citizenship);

    long countByGender(Gender gender);

    long getUsersCountByStatus(Status status);

    //Работа с документами
    Page<UserResponseDto> getUsersWithExpiredDocuments(Pageable pageable);

    Page<UserResponseDto> getUsersWithDocumentsExpiringBetween(LocalDate start, LocalDate end, Pageable pageable);

    Page<UserResponseDto> getUsersByDocumentType(DocumentType documentType, Pageable pageable);

    // Сложные запросы
    Page<UserResponseDto> getAliveUsersWithExpiredDocuments(Pageable pageable);
}
