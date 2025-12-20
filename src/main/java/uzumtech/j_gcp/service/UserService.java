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
    UserResponseDto createUser(UserRequestDto userRequestDto);
    Page<UserResponseDto> getAllUsers(PageRequest pageRequest);
    UserResponseDto getUserById(Long id);
    UserResponseDto getUserByUserPinfl(String pinfl);

    boolean isUserAlive(Long id);
    MarkDeadResponseDto markUserAsDead(Long id, LocalDate deathDate);

    //поиски юзеров
    List<UserResponseDto> searchUsersByName(String fullName);

    List<UserResponseDto> getAllAliveUsers();
    List<UserResponseDto> getAllDeadUsers();
    //подсчет юзеров
    long getAllDeadUsersCount();
    long getAllAliveUsersCount();

    //по документам
    List<UserResponseDto> getUsersWithExpiredDocuments();

    List<UserResponseDto> getUsersWithDocumentsExpiringBetween(LocalDate start, LocalDate end);

    List<UserResponseDto> getUsersByDocumentType(DocumentType documentType);

    List<UserResponseDto> getUsersByDocumentType(org.w3c.dom.DocumentType documentType);

    //микс запросы
    List<UserResponseDto> getAliveUsersWithExpiredDocuments();
}
