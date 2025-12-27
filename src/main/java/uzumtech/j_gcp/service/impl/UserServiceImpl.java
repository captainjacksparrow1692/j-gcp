package uzumtech.j_gcp.service.impl;

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import uzumtech.j_gcp.constant.enums.DocumentType;
import uzumtech.j_gcp.constant.enums.Gender;
import uzumtech.j_gcp.constant.enums.LifeStatus;
import uzumtech.j_gcp.dto.request.UserRequestDto;
import uzumtech.j_gcp.dto.response.MarkDeadResponseDto;
import uzumtech.j_gcp.dto.response.UserResponseDto;
import uzumtech.j_gcp.mapper.UserMapper;
import uzumtech.j_gcp.repository.UserRepository;
import uzumtech.j_gcp.service.UserService;

import java.time.LocalDate;
import java.util.NoSuchElementException;

@Slf4j
@Service
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class UserServiceImpl implements UserService {

    UserRepository userRepository;
    UserMapper userMapper;

    @Override
    @Transactional
    public UserResponseDto createUser(UserRequestDto userRequestDto) {
        log.info("Creating user with PINFL: {}", userRequestDto.pinfl());
        if (userRepository.existsByPinfl(userRequestDto.pinfl())) {
            throw new IllegalArgumentException("User with this PINFL already exists");
        }
        var entity = userMapper.toEntity(userRequestDto);
        var saved = userRepository.save(entity);
        return userMapper.toResponseDto(saved);
    }

    @Override
    public Page<UserResponseDto> getAllUsers(Pageable pageable) {
        return userRepository.findAll(pageable).map(userMapper::toResponseDto);
    }

    @Override
    public UserResponseDto getUserById(Long id) {
        var user = userRepository.findById(id)
                .orElseThrow(() -> new NoSuchElementException("User not found with id: " + id));
        return userMapper.toResponseDto(user);
    }

    @Override
    public UserResponseDto getUserByUserPinfl(String pinfl) {
        return userRepository.findByPinfl(pinfl)
                .map(userMapper::toResponseDto)
                .orElseThrow(() -> new NoSuchElementException("User not found with PINFL: " + pinfl));
    }

    @Override
    public UserResponseDto getUserByEmail(String email){
        return userRepository.findByEmail(email)
                .map(userMapper::toResponseDto)
                .orElseThrow(() -> new NoSuchElementException("User not found with email: " + email));
    }

    @Override
    public Page<UserResponseDto> getUserByCitizenship(String citizenship, Pageable pageable){
        return userRepository.findByCitizenship(citizenship, pageable)
                .map(userMapper::toResponseDto);
    }

    @Override
    public boolean isUserAlive(Long id) {
        var user = userRepository.findById(id)
                .orElseThrow(() -> new NoSuchElementException("User not found"));
        return user.getLifeStatus() == LifeStatus.ALIVE;
    }

    @Override
    @Transactional
    public MarkDeadResponseDto markUserAsDead(Long id, LocalDate deathDate) {
        var user = userRepository.findById(id)
                .orElseThrow(() -> new NoSuchElementException("User not found"));

        user.setLifeStatus(LifeStatus.DECEASED);
        user.setDeathDate(deathDate);

        var updated = userRepository.save(user);
        return userMapper.toMarkDeadResponseDto(updated);
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
    public long countUsersByCitizenship(String citizenship) {
        log.debug("Counting users for citizenship: {}", citizenship);
        return userRepository.countByCitizenship(citizenship);
    }

    @Override
    public long countByGender(Gender gender){
        log.debug("Counting users for gender: {}", gender);
        return userRepository.countByGender(gender);
    }

    @Override
    public long getUsersCountByStatus(Status status) {
        return status == Status.ALIVE ?
                userRepository.countByDeathDateIsNull() :
                userRepository.countByDeathDateIsNotNull();
    }

    @Override
    public Page<UserResponseDto> getUsersWithExpiredDocuments(Pageable pageable) {
        return userRepository.findAllByExpirationDateBefore(LocalDate.now(), pageable)
                .map(userMapper::toResponseDto);
    }

    @Override
    public Page<UserResponseDto> getUsersWithDocumentsExpiringBetween(LocalDate start, LocalDate end, Pageable pageable) {
        return userRepository.findAllByExpirationDateBetween(start, end, pageable)
                .map(userMapper::toResponseDto);
    }

    @Override
    public Page<UserResponseDto> getUsersByDocumentType(DocumentType documentType, Pageable pageable) {
        return userRepository.findAllByDocumentType(documentType, pageable)
                .map(userMapper::toResponseDto);
    }

    @Override
    public Page<UserResponseDto> getAliveUsersWithExpiredDocuments(Pageable pageable) {
        return userRepository.findAllByDeathDateIsNullAndExpirationDateBefore(LocalDate.now(), pageable)
                .map(userMapper::toResponseDto);
    }
}