package uzumtech.j_gcp.service.impl;

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import uzumtech.j_gcp.component.GcpAdapter;
import uzumtech.j_gcp.constant.enums.DocumentType;
import uzumtech.j_gcp.dto.request.UserRequestDto;
import uzumtech.j_gcp.dto.response.MarkDeadResponseDto;
import uzumtech.j_gcp.dto.response.UserResponseDto;
import uzumtech.j_gcp.entity.User;
import uzumtech.j_gcp.mapper.UserMapper;
import uzumtech.j_gcp.repository.UserRepository;
import uzumtech.j_gcp.service.UserService;

import java.time.LocalDate;

import static uzumtech.j_gcp.constant.Constant.USER_NOT_FOUND;

@Slf4j
@Service
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class UserServiceImpl implements UserService {

    UserRepository userRepository;
    UserMapper userMapper;
    GcpAdapter gcpAdapter;

    @Override
    public UserResponseDto createUser(UserRequestDto dto) {
        if (userRepository.existsByPinfl(dto.pinfl())) {
            throw new RuntimeException("User with this PINFL already exists");
        }
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
    public UserResponseDto getUserById(Long id) {
        return userRepository.findById(id)
                .map(userMapper::toResponseDto)
                .orElseThrow(() -> new RuntimeException(USER_NOT_FOUND));
    }

    @Override
    public UserResponseDto getUserByUserPinfl(String pinfl) {
        return userRepository.findByPinfl(pinfl)
                .map(userMapper::toResponseDto)
                .orElseThrow(() -> new RuntimeException(USER_NOT_FOUND));
    }

    @Override
    public boolean isUserAlive(Long id) {
        return userRepository.findById(id)
                .map(user -> user.getDeathDate() == null)
                .orElse(false);
    }

    @Override
    @Transactional // Обязательно, так как меняем состояние сущности (dirty checking)
    public MarkDeadResponseDto markUserAsDead(Long id, LocalDate deathDate) {
        User user = userRepository.findById(id)
                .orElseThrow(() -> new RuntimeException(USER_NOT_FOUND));

        user.setDeathDate(deathDate);
        // userRepository.save(user) здесь не обязателен из-за @Transactional
        return userMapper.toMarkDeadResponseDto(user);
    }

    @Override
    public Page<UserResponseDto> searchUsersByName(String fullName, Pageable pageable) {
        return userRepository.findAllByFullNameContainingIgnoreCase(fullName, pageable)
                .map(userMapper::toResponseDto);
    }

    @Override
    public Page<UserResponseDto> getAllAliveUsers(Pageable pageable) {
        return userRepository.findAllByDeathDateIsNull(pageable)
                .map(userMapper::toResponseDto);
    }

    @Override
    public Page<UserResponseDto> getAllDeadUsers(Pageable pageable) {
        return userRepository.findAllByDeathDateIsNotNull(pageable)
                .map(userMapper::toResponseDto);
    }

    @Override
    public long getUsersCountByStatus(Status status) {
        return 0; // Реализовать по необходимости
    }

    @Override
    public Page<UserResponseDto> getUsersWithExpiredDocuments(Pageable pageable) {
        return userRepository.findAllByExpiryDateBefore(LocalDate.now(), pageable)
                .map(userMapper::toResponseDto);
    }

    @Override
    public Page<UserResponseDto> getUsersWithDocumentsExpiringBetween(LocalDate start, LocalDate end, Pageable pageable) {
        return userRepository.findAllByExpiryDateBetween(start, end, pageable)
                .map(userMapper::toResponseDto);
    }

    @Override
    public Page<UserResponseDto> getUsersByDocumentType(DocumentType documentType, Pageable pageable) {
        return userRepository.findAllByDocumentType(documentType, pageable)
                .map(userMapper::toResponseDto);
    }

    @Override
    public Page<UserResponseDto> getAliveUsersWithExpiredDocuments(Pageable pageable) {
        return userRepository.findAllByDeathDateIsNullAndExpiryDateBefore(LocalDate.now(), pageable)
                .map(userMapper::toResponseDto);
    }
}