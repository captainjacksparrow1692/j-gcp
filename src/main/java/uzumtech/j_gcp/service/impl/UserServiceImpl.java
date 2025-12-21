package uzumtech.j_gcp.service.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import uzumtech.j_gcp.constant.DocumentType;
import uzumtech.j_gcp.dto.request.UserRequestDto;
import uzumtech.j_gcp.dto.response.MarkDeadResponseDto;
import uzumtech.j_gcp.dto.response.UserResponseDto;
import uzumtech.j_gcp.entity.User;
import uzumtech.j_gcp.exception.InvalidBusinessLogicException;
import uzumtech.j_gcp.exception.ResourceNotFoundException;
import uzumtech.j_gcp.mapper.UserMapper;
import uzumtech.j_gcp.repository.UserRepository;
import uzumtech.j_gcp.service.UserService;
import uzumtech.j_gcp.service.UserValidationService;

import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;
    private final UserMapper userMapper;
    private final UserValidationService userValidationService;

    //CRUD
    @Override
    @Transactional
    public UserResponseDto createUser(UserRequestDto userRequestDto) {
        userValidationService.validateUniqueness(userRequestDto);
        User user = userMapper.toEntity(userRequestDto);
        return userMapper.toResponseDto(userRepository.save(user));
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
                .orElseThrow(() -> new ResourceNotFoundException("Пользователь с ID " + id + " не найден"));
    }

    @Override
    public UserResponseDto getUserByUserPinfl(String pinfl) {
        return userRepository.findByPinfl(pinfl)
                .map(userMapper::toResponseDto)
                .orElseThrow(() -> new ResourceNotFoundException("Пользователь с ПИНФЛ " + pinfl + " не найден"));
    }

    //Статус жизни
    @Override
    public boolean isUserAlive(Long id) {
        User user = userRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Пользователь не найден"));
        return user.getDeathDate() == null;
    }

    @Override
    @Transactional
    public MarkDeadResponseDto markUserAsDead(Long id, LocalDate deathDate) {
        User user = userRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Пользователь не найден"));

        if (deathDate != null && deathDate.isAfter(LocalDate.now())) {
            throw new InvalidBusinessLogicException("Дата смерти не может быть в будущем");
        }

        user.setDeathDate(deathDate);
        User savedUser = userRepository.save(user);

        return MarkDeadResponseDto.builder()
                .userId(savedUser.getId())
                .deathDate(savedUser.getDeathDate())
                .status("DECEASED")
                .build();
    }

    //Поиск
    @Override
    public List<UserResponseDto> searchUsersByName(String fullName) {
        return userRepository.findAllByFullNameContainingIgnoreCase(fullName)
                .stream()
                .map(userMapper::toResponseDto)
                .collect(Collectors.toList());
    }

    @Override
    public List<UserResponseDto> getAllAliveUsers() {
        return userRepository.findAllByDeathDateIsNull()
                .stream()
                .map(userMapper::toResponseDto)
                .collect(Collectors.toList());
    }

    @Override
    public List<UserResponseDto> getAllDeadUsers() {
        return userRepository.findAllByDeathDateIsNotNull()
                .stream()
                .map(userMapper::toResponseDto)
                .collect(Collectors.toList());
    }

    //Статистика
    @Override
    public long getAllDeadUsersCount() {
        return userRepository.countByDeathDateIsNotNull();
    }

    @Override
    public long getAllAliveUsersCount() {
        return userRepository.countByDeathDateIsNull();
    }

    //Работа с документами
    @Override
    public List<UserResponseDto> getUsersWithExpiredDocuments() {
        return userRepository.findAllByExpiryDateBefore(LocalDate.now())
                .stream()
                .map(userMapper::toResponseDto)
                .collect(Collectors.toList());
    }

    @Override
    public List<UserResponseDto> getUsersWithDocumentsExpiringBetween(LocalDate start, LocalDate end) {
        return userRepository.findAllByExpiryDateBetween(start, end)
                .stream()
                .map(userMapper::toResponseDto)
                .collect(Collectors.toList());
    }

    @Override
    public List<UserResponseDto> getUsersByDocumentType(DocumentType documentType) {
        return userRepository.findAllByDocumentType(documentType)
                .stream()
                .map(userMapper::toResponseDto)
                .collect(Collectors.toList());
    }

    @Override
    public List<UserResponseDto> getUsersByDocumentType(org.w3c.dom.DocumentType documentType) {
        DocumentType type = DocumentType.valueOf(documentType.toString());
        return getUsersByDocumentType(type);
    }

    //Сложные запросы
    @Override
    public List<UserResponseDto> getAliveUsersWithExpiredDocuments() {
        return userRepository.findAllByDeathDateIsNullAndExpiryDateBefore(LocalDate.now())
                .stream()
                .map(userMapper::toResponseDto)
                .collect(Collectors.toList());
    }
}
