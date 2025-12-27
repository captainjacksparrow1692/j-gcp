package uzumtech.j_gcp.service.impl;

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import uzumtech.j_gcp.constant.enums.DocumentType;
import uzumtech.j_gcp.dto.request.UserRequestDto;
import uzumtech.j_gcp.dto.response.MarkDeadResponseDto;
import uzumtech.j_gcp.dto.response.UserResponseDto;
import uzumtech.j_gcp.entity.User;
import uzumtech.j_gcp.exception.UserNotFoundException; // Предполагаемое исключение
import uzumtech.j_gcp.mapper.UserMapper;
import uzumtech.j_gcp.repository.UserRepository;
import uzumtech.j_gcp.service.UserService;

import java.time.LocalDate;

import static uzumtech.j_gcp.constant.Constant.USER_REDIS_KEYS; // Предполагаемая константа

@Slf4j
@Service
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class UserServiceImpl implements UserService {

    UserMapper userMapper;
    UserRepository userRepository;

    @Override
    @Transactional
    public UserResponseDto createUser(UserRequestDto userRequestDto) {
        log.info("Creating user with PINFL: {}",
                userRequestDto.pinfl());
        var entity = userMapper.toEntity(userRequestDto);
        var savedUser = userRepository.save(entity);
        return userMapper.toResponseDto(savedUser);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<UserResponseDto> getAllUsers(Pageable pageable) {
        return userRepository.findAll(pageable).map(userMapper::toResponseDto);
    }

    @Override
    @Cacheable(value = USER_REDIS_KEYS, key = "'user:' + #id", unless = "#result == null")
    @Transactional(readOnly = true)
    public UserResponseDto getUserById(Long id) {
        return userRepository.findById(id)
                .map(userMapper::toResponseDto)
                .orElseThrow(() -> new UserNotFoundException(id.toString()));
    }

    @Override
    @Transactional(readOnly = true)
    public UserResponseDto getUserByUserPinfl(String pinfl) {
        return userRepository.findByPinfl(pinfl)
                .map(userMapper::toResponseDto)
                .orElseThrow(() -> new UserNotFoundException(pinfl));
    }

    @Override
    @Transactional(readOnly = true)
    public boolean isUserAlive(Long id) {
        return userRepository.findById(id)
                .map(user -> !user.isDead())
                .orElseThrow(() -> new UserNotFoundException(id.toString()));
    }

    @Override
    @Transactional
    @CacheEvict(value = USER_REDIS_KEYS, key = "'user:' + #id")
    public MarkDeadResponseDto markUserAsDead(Long id, LocalDate deathDate) {
        var user = userRepository.findById(id)
                .orElseThrow(() -> new UserNotFoundException(id.toString()));

        user.setDead(true);
        user.setDeathDate(deathDate);

        log.info("User with id {} marked as dead on {}", id, deathDate);

        return MarkDeadResponseDto.builder()
                .id(user.getId())
                .deathDate(deathDate)
                .build();
    }

    @Override
    @Transactional(readOnly = true)
    public Page<UserResponseDto> searchUsersByName(String fullName, Pageable pageable) {
        return userRepository.findByFullNameContainingIgnoreCase(fullName, pageable)
                .map(userMapper::toDto);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<UserResponseDto> getAllAliveUsers(Pageable pageable) {
        return userRepository.findAllByIsDeadFalse(pageable)
                .map(userMapper::toDto);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<UserResponseDto> getAllDeadUsers(Pageable pageable) {
        return userRepository.findAllByIsDeadTrue(pageable)
                .map(userMapper::toDto);
    }

    @Override
    public long getUsersCountByStatus(Status status) {
        return status == Status.ALIVE
                ? userRepository.countByIsDeadFalse()
                : userRepository.countByIsDeadTrue();
    }

    @Override
    @Transactional(readOnly = true)
    public Page<UserResponseDto> getUsersWithExpiredDocuments(Pageable pageable) {
        return userRepository.findUsersWithExpiredDocuments(LocalDate.now(), pageable)
                .map(userMapper::toDto);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<UserResponseDto> getUsersWithDocumentsExpiringBetween(LocalDate start, LocalDate end, Pageable pageable) {
        return userRepository.findUsersWithDocumentsExpiringBetween(start, end, pageable)
                .map(userMapper::toDto);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<UserResponseDto> getUsersByDocumentType(DocumentType documentType, Pageable pageable) {
        return userRepository.findAllByDocumentsType(documentType, pageable)
                .map(userMapper::toDto);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<UserResponseDto> getAliveUsersWithExpiredDocuments(Pageable pageable) {
        return userRepository.findAliveUsersWithExpiredDocuments(LocalDate.now(), pageable)
                .map(userMapper::toDto);
    }
}