package uzumtech.j_gcp.service.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import uzumtech.j_gcp.constant.DocumentType;
import uzumtech.j_gcp.dto.request.UserRequestDto;
import uzumtech.j_gcp.dto.response.MarkDeadResponseDto;
import uzumtech.j_gcp.dto.response.UserResponseDto;
import uzumtech.j_gcp.entity.User;
import uzumtech.j_gcp.repository.UserRepository;
import uzumtech.j_gcp.service.UserService;

import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;

    //CRUD
    @Override
    public UserResponseDto createUser(UserRequestDto userRequestDto) {
        if (userRepository.existsByPinfl(userRequestDto.getPinfl())) {
            throw new RuntimeException("User with this PINFL already exists");
        }
        User user = new User();
        user.setFullName(userRequestDto.getFullName());
        user.setPinfl(userRequestDto.getPinfl());
        user.setEmail(userRequestDto.getEmail());
        user.setPhoneNumber(userRequestDto.getPhoneNumber());
        user.setDocumentType(userRequestDto.getDocumentType());
        user.setExpiryDate(userRequestDto.getExpiryDate());
        user.setDeathDate(null); // по умолчанию жив
        user = userRepository.save(user);
        return toDto(user);
    }

    @Override
    public Page<UserResponseDto> getAllUsers(Pageable pageable) {
        Page<User> users = userRepository.findAll(pageable);
        return users.map(this::toDto);
    }

    @Override
    public UserResponseDto getUserById(Long id) {
        User user = userRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("User not found"));
        return toDto(user);
    }

    @Override
    public UserResponseDto getUserByUserPinfl(String pinfl) {
        User user = userRepository.findByPinfl(pinfl)
                .orElseThrow(() -> new RuntimeException("User not found"));
        return toDto(user);
    }

    //Статус жизни
    @Override
    public boolean isUserAlive(Long id) {
        return userRepository.findById(id)
                .map(user -> user.getDeathDate() == null)
                .orElse(false);
    }

    @Override
    public MarkDeadResponseDto markUserAsDead(Long id, LocalDate deathDate) {
        User user = userRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("User not found"));
        user.setDeathDate(deathDate);
        userRepository.save(user);

        return MarkDeadResponseDto.builder()
                .userId(id)
                .pinfl(user.getPinfl())
                .deathDate(deathDate)
                .build();
    }

    //Поиск
    @Override
    public Page<UserResponseDto> searchUsersByName(String fullName, Pageable pageable) {
        return userRepository.findAllByFullNameContainingIgnoreCase(fullName, pageable)
                .map(this::toDto);
    }

    @Override
    public Page<UserResponseDto> getAllAliveUsers(Pageable pageable) {
        List<User> users = userRepository.findAllByDeathDateIsNull();
        return listToPage(users, pageable);
    }

    @Override
    public Page<UserResponseDto> getAllDeadUsers(Pageable pageable) {
        List<User> users = userRepository.findAllByDeathDateIsNotNull();
        return listToPage(users, pageable);
    }

    //Статистика
    @Override
    public long getUsersCountByStatus(Status status) {
        return status == Status.ALIVE ? userRepository.countByDeathDateIsNull()
                : userRepository.countByDeathDateIsNotNull();
    }

    //Работа с документами
    @Override
    public Page<UserResponseDto> getUsersWithExpiredDocuments(Pageable pageable) {
        List<User> users = userRepository.findAllByExpiryDateBefore(LocalDate.now());
        return listToPage(users, pageable);
    }

    @Override
    public Page<UserResponseDto> getUsersWithDocumentsExpiringBetween(LocalDate start, LocalDate end, Pageable pageable) {
        List<User> users = userRepository.findAllByExpiryDateBetween(start, end);
        return listToPage(users, pageable);
    }

    @Override
    public Page<UserResponseDto> getUsersByDocumentType(DocumentType documentType, Pageable pageable) {
        List<User> users = userRepository.findAllByDocumentType(documentType);
        return listToPage(users, pageable);
    }

    @Override
    public Page<UserResponseDto> getUsersBySystemDocumentType(org.w3c.dom.DocumentType documentType, Pageable pageable) {
        // Заглушка, так как w3c DocumentType не используется в БД
        return Page.empty(pageable);
    }

    //Сложные запросы
    @Override
    public Page<UserResponseDto> getAliveUsersWithExpiredDocuments(Pageable pageable) {
        List<User> users = userRepository.findAllByDeathDateIsNullAndExpiryDateBefore(LocalDate.now());
        return listToPage(users, pageable);
    }

    //Вспомогательные методы
    private UserResponseDto toDto(User user) {
        return UserResponseDto.builder()
                .id(user.getId())
                .fullName(user.getFullName())
                .age(user.getAge())
                .pinfl(user.getPinfl())
                .email(user.getEmail())
                .phoneNumber(user.getPhoneNumber())
                .documentType(user.getDocumentType())
                .expiryDate(user.getExpiryDate())
                .deathDate(user.getDeathDate())
                .build();
    }

    private Page<UserResponseDto> listToPage(List<User> users, Pageable pageable) {
        int start = (int) pageable.getOffset();
        int end = Math.min((start + pageable.getPageSize()), users.size());
        List<UserResponseDto> dtos = users.subList(start, end).stream()
                .map(this::toDto)
                .collect(Collectors.toList());
        return new PageImpl<>(dtos, pageable, users.size());
    }
}
