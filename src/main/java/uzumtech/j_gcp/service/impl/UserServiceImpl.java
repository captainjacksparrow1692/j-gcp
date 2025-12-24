package uzumtech.j_gcp.service.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import uzumtech.j_gcp.constant.enums.DocumentType;
import uzumtech.j_gcp.dto.request.UserRequestDto;
import uzumtech.j_gcp.dto.response.MarkDeadResponseDto;
import uzumtech.j_gcp.dto.response.UserResponseDto;
import uzumtech.j_gcp.entity.User;
import uzumtech.j_gcp.repository.UserRepository;
import uzumtech.j_gcp.service.UserService;

import java.time.LocalDate;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;
    private static final String USER_NOT_FOUND = "User not found";

    // CRUD
    @Override
    public UserResponseDto createUser(UserRequestDto dto) {
        if (userRepository.existsByPinfl(dto.getPinfl())) {
            throw new RuntimeException("User with this PINFL already exists");
        }

        User user = new User();
        user.setFullName(dto.getFullName());
        user.setPinfl(dto.getPinfl());
        user.setEmail(dto.getEmail());
        user.setPhoneNumber(dto.getPhoneNumber());
        user.setDocumentType(dto.getDocumentType());
        user.setExpiryDate(dto.getExpiryDate());
        user.setDeathDate(dto.getDeathDate());
        user.setAddress(dto.getAddress());
        user.setCitizenship(dto.getCitizenship());
        user.setAge(dto.getAge());
        user.setPhotoUrl(dto.getPhotoUrl());
        user.setIssueDate(dto.getIssueDate());

        return toDto(userRepository.save(user));
    }

    @Override
    @Transactional(readOnly = true)
    public Page<UserResponseDto> getAllUsers(Pageable pageable) {
        return userRepository.findAll(pageable).map(this::toDto);
    }

    @Override
    @Transactional(readOnly = true)
    public UserResponseDto getUserById(Long id) {
        return userRepository.findById(id)
                .map(this::toDto)
                .orElseThrow(() -> new RuntimeException(USER_NOT_FOUND));
    }

    @Override
    @Transactional(readOnly = true)
    public UserResponseDto getUserByUserPinfl(String pinfl) {
        return userRepository.findByPinfl(pinfl)
                .map(this::toDto)
                .orElseThrow(() -> new RuntimeException(USER_NOT_FOUND));
    }

    // Статус жизни
    @Override
    @Transactional(readOnly = true)
    public boolean isUserAlive(Long id) {
        return userRepository.findById(id)
                .map(user -> user.getDeathDate() == null)
                .orElse(false);
    }

    @Override
    @Transactional
    public MarkDeadResponseDto markUserAsDead(Long id, LocalDate deathDate) {
        User user = userRepository.findById(id)
                .orElseThrow(() -> new RuntimeException(USER_NOT_FOUND));

        user.setDeathDate(deathDate);
        userRepository.save(user);

        return MarkDeadResponseDto.builder()
                .userId(id)
                .pinfl(user.getPinfl())
                .deathDate(deathDate)
                .build();
    }

    // Поиск и фильтрация с правильной пагинацией на уровне БД
    @Override
    @Transactional(readOnly = true)
    public Page<UserResponseDto> searchUsersByName(String fullName, Pageable pageable) {
        return userRepository.findAllByFullNameContainingIgnoreCase(fullName, pageable)
                .map(this::toDto);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<UserResponseDto> getAllAliveUsers(Pageable pageable) {
        return userRepository.findAllByDeathDateIsNull(pageable)
                .map(this::toDto);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<UserResponseDto> getAllDeadUsers(Pageable pageable) {
        return userRepository.findAllByDeathDateIsNotNull(pageable)
                .map(this::toDto);
    }

    // Статистика
    @Override
    @Transactional(readOnly = true)
    public long getUsersCountByStatus(Status status) {
        return status == Status.ALIVE
                ? userRepository.countByDeathDateIsNull()
                : userRepository.countByDeathDateIsNotNull();
    }

    // Работа с документами
    @Override
    @Transactional(readOnly = true)
    public Page<UserResponseDto> getUsersWithExpiredDocuments(Pageable pageable) {
        return userRepository.findAllByExpiryDateBefore(LocalDate.now(), pageable)
                .map(this::toDto);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<UserResponseDto> getUsersWithDocumentsExpiringBetween(LocalDate start, LocalDate end, Pageable pageable) {
        return userRepository.findAllByExpiryDateBetween(start, end, pageable)
                .map(this::toDto);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<UserResponseDto> getUsersByDocumentType(DocumentType documentType, Pageable pageable) {
        return userRepository.findAllByDocumentType(documentType, pageable)
                .map(this::toDto);
    }

    // Сложные запросы
    @Override
    @Transactional(readOnly = true)
    public Page<UserResponseDto> getAliveUsersWithExpiredDocuments(Pageable pageable) {
        return userRepository.findAllByDeathDateIsNullAndExpiryDateBefore(LocalDate.now(), pageable)
                .map(this::toDto);
    }

    // Вспомогательный метод маппинга
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
}