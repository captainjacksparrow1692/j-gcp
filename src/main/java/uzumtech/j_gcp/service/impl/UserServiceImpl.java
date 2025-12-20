package uzumtech.j_gcp.service.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import uzumtech.j_gcp.constant.DocumentType;
import uzumtech.j_gcp.dto.request.UserRequestDto;
import uzumtech.j_gcp.dto.response.MarkDeadResponseDto;
import uzumtech.j_gcp.dto.response.UserResponseDto;
import uzumtech.j_gcp.entity.User;
import uzumtech.j_gcp.mapper.UserMapper;
import uzumtech.j_gcp.repository.UserRepository;
import uzumtech.j_gcp.service.UserService;

import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;
    private final UserMapper userMapper;

    @Override
    @Transactional
    public UserResponseDto createUser(UserRequestDto userRequestDto) {
        // Маппер использует getFullName(), getAddress() и т.д.
        User user = userMapper.toEntity(userRequestDto);
        return userMapper.toResponseDto(userRepository.save(user));
    }

    @Override
    public Page<UserResponseDto> getAllUsers(PageRequest pageRequest) {
        return userRepository.findAll(pageRequest)
                .map(userMapper::toResponseDto);
    }

    @Override
    public UserResponseDto getUserById(Long id) {
        return userRepository.findById(id)
                .map(userMapper::toResponseDto)
                .orElseThrow(() -> new RuntimeException("User not found: " + id));
    }

    @Override
    public UserResponseDto getUserByUserPinfl(String pinfl) {
        return userRepository.findByPinfl(pinfl)
                .map(userMapper::toResponseDto)
                .orElseThrow(() -> new RuntimeException("User not found with PINFL: " + pinfl));
    }

    @Override
    public boolean isUserAlive(Long id) {
        User user = userRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("User not found"));
        return user.getDeathDate() == null;
    }

    @Override
    @Transactional
    public MarkDeadResponseDto markUserAsDead(Long id, LocalDate deathDate) {
        User user = userRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("User not found"));

        user.setDeathDate(deathDate);
        User savedUser = userRepository.save(user);

        // Мапим в ваш MarkDeceasedResponseDto и перекладываем в MarkDeadResponseDto
        var result = userMapper.toMarkDeadResponseDto(savedUser);

        return MarkDeadResponseDto.builder()
                .userId(user.getId())
                .deathDate(user.getDeathDate())
                .build();
    }

    @Override
    public List<UserResponseDto> searchUsersByName(String fullName) {
        return userRepository.findAllByFullNameContainingIgnoreCase(fullName)
                .stream()
                .map(userMapper::toResponseDto)
                .collect(Collectors.toList());
    }

    @Override
    public List<UserResponseDto> getAllAliveUsers() {
        return userRepository.findAllByDeathDateIsNull().stream()
                .map(userMapper::toResponseDto)
                .collect(Collectors.toList());
    }

    @Override
    public List<UserResponseDto> getAllDeadUsers() {
        return userRepository.findAllByDeathDateIsNotNull().stream()
                .map(userMapper::toResponseDto)
                .collect(Collectors.toList());
    }

    @Override
    public long getAllDeadUsersCount() {
        return userRepository.countByDeathDateIsNotNull();
    }

    @Override
    public long getAllAliveUsersCount() {
        return userRepository.countByDeathDateIsNull();
    }

    @Override
    public List<UserResponseDto> getUsersWithExpiredDocuments() {
        return userRepository.findAllByExpiryDateBefore(LocalDate.now()).stream()
                .map(userMapper::toResponseDto)
                .collect(Collectors.toList());
    }

    @Override
    public List<UserResponseDto> getUsersWithDocumentsExpiringBetween(LocalDate start, LocalDate end) {
        return userRepository.findAllByExpiryDateBetween(start, end).stream()
                .map(userMapper::toResponseDto)
                .collect(Collectors.toList());
    }

    @Override
    public List<UserResponseDto> getUsersByDocumentType(DocumentType documentType) {
        return List.of();
    }

    @Override
    public List<UserResponseDto> getUsersByDocumentType(org.w3c.dom.DocumentType documentType) {
        // Конвертируем системный DocumentType в ваш Enum, если они не совпадают
        uzumtech.j_gcp.constant.DocumentType internalEnum =
                uzumtech.j_gcp.constant.DocumentType.valueOf(documentType.toString());

        return userRepository.findAllByDocumentType(internalEnum).stream()
                .map(userMapper::toResponseDto)
                .collect(Collectors.toList());
    }

    @Override
    public List<UserResponseDto> getAliveUsersWithExpiredDocuments() {
        return userRepository.findAllByDeathDateIsNullAndExpiryDateBefore(LocalDate.now()).stream()
                .map(userMapper::toResponseDto)
                .collect(Collectors.toList());
    }
}