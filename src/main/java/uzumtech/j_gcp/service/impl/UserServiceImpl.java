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
import uzumtech.j_gcp.mapper.UserMapper;
import uzumtech.j_gcp.repository.UserRepository;
import uzumtech.j_gcp.service.UserService;

import java.time.LocalDate;

@Service
@RequiredArgsConstructor // Генерирует конструктор для финальных полей (injection)
@Transactional // По умолчанию все методы будут в транзакции
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;
    private final UserMapper userMapper;

    private static final String USER_NOT_FOUND = "User not found";

    // --- CRUD ---

    @Override
    public UserResponseDto createUser(UserRequestDto dto) {
        if (userRepository.existsByPinfl(dto.getPinfl())) {
            throw new RuntimeException("User with this PINFL already exists");
        }

        // Маппинг одной строкой вместо 15 сеттеров
        User user = userMapper.toEntity(dto);
        User savedUser = userRepository.save(user);

        return userMapper.toResponseDto(savedUser);
    }

    @Override
    public Page<UserResponseDto> getAllUsers(Pageable pageable) {
        return userRepository.findAll(pageable)
                .map(userMapper::toResponseDto);
    }

    @Override
    @Transactional(readOnly = true)
    public UserResponseDto getUserById(Long id) {
        return userRepository.findById(id)
                .map(userMapper::toResponseDto)
                .orElseThrow(() -> new RuntimeException(USER_NOT_FOUND));
    }

    @Override
    @Transactional(readOnly = true)
    public UserResponseDto getUserByUserPinfl(String pinfl) {
        return userRepository.findByPinfl(pinfl)
                .map(userMapper::toResponseDto)
                .orElseThrow(() -> new RuntimeException(USER_NOT_FOUND));
    }

    // --- СТАТУС ЖИЗНИ ---

    @Override
    @Transactional(readOnly = true)
    public boolean isUserAlive(Long id) {
        return userRepository.findById(id)
                .map(user -> user.getDeathDate() == null)
                .orElse(false);
    }

    @Override
    public MarkDeadResponseDto markUserAsDead(Long id, LocalDate deathDate) {
        User user = userRepository.findById(id)
                .orElseThrow(() -> new RuntimeException(USER_NOT_FOUND));

        user.setDeathDate(deathDate);

        return userMapper.toMarkDeadResponseDto(user);
    }

    // --- ПОИСК И ФИЛЬТРАЦИЯ ---

    @Override
    @Transactional(readOnly = true)
    public Page<UserResponseDto> searchUsersByName(String fullName, Pageable pageable) {
        return userRepository.findAllByFullNameContainingIgnoreCase(fullName, pageable)
                .map(userMapper::toResponseDto);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<UserResponseDto> getAllAliveUsers(Pageable pageable) {
        return userRepository.findAllByDeathDateIsNull(pageable)
                .map(userMapper::toResponseDto);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<UserResponseDto> getAllDeadUsers(Pageable pageable) {
        return userRepository.findAllByDeathDateIsNotNull(pageable)
                .map(userMapper::toResponseDto);
    }

    // --- СТАТИСТИКА ---
    //Сомнительный API с точки зрения бизнес логики
    @Override
    @Transactional(readOnly = true)
    public long getUsersCountByStatus(Status status) {
        return status == Status.ALIVE
                ? userRepository.countByDeathDateIsNull()
                : userRepository.countByDeathDateIsNotNull();
    }

    // --- РАБОТА С ДОКУМЕНТАМИ ---

    @Override
    @Transactional(readOnly = true)
    public Page<UserResponseDto> getUsersWithExpiredDocuments(Pageable pageable) {
        return userRepository.findAllByExpiryDateBefore(LocalDate.now(), pageable)
                .map(userMapper::toResponseDto);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<UserResponseDto> getUsersWithDocumentsExpiringBetween(LocalDate start, LocalDate end, Pageable pageable) {
        return userRepository.findAllByExpiryDateBetween(start, end, pageable)
                .map(userMapper::toResponseDto);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<UserResponseDto> getUsersByDocumentType(DocumentType documentType, Pageable pageable) {
        return userRepository.findAllByDocumentType(documentType, pageable)
                .map(userMapper::toResponseDto);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<UserResponseDto> getAliveUsersWithExpiredDocuments(Pageable pageable) {
        return userRepository.findAllByDeathDateIsNullAndExpiryDateBefore(LocalDate.now(), pageable)
                .map(userMapper::toResponseDto);
    }
}