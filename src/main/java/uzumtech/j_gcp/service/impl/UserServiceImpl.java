package uzumtech.j_gcp.service.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.w3c.dom.DocumentType; // Внимание: обычно DocumentType — это ваш Enum, а не интерфейс w3c
import uzumtech.j_gcp.dto.request.UserRequestDto;
import uzumtech.j_gcp.dto.response.MarkDeadResponseDto;
import uzumtech.j_gcp.dto.response.UserResponseDto;
import uzumtech.j_gcp.exception.UserNotFoundException; // Предполагаемое исключение
import uzumtech.j_gcp.mapper.UserMapper; // Предполагаемый маппер
import uzumtech.j_gcp.entity.User;
import uzumtech.j_gcp.repository.UserRepository;
import uzumtech.j_gcp.service.UserService;

import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;

import static sun.awt.image.MultiResolutionCachedImage.map;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {
    private final UserRepository userRepository;
    private final UserMapper userMapper;

    @Override
    @Transactional
    public UserResponseDto createUser(UserRequestDto userRequestDto) {
        User user = userMapper.toEntity(userRequestDto);
        User savedUser = userRepository.save(user);
        return userMapper.toDto(savedUser);
    }

    @Override
    public Page<UserResponseDto> getAllUsers(PageRequest pageRequest) {
        return userRepository.findAll(pageRequest)
                .map(userMapper::toResponseDto);
    }

    @Override
    public UserResponseDto getUserById(Long id) {
        return userRepository.findById(id)
                .map(userRepository::toDto)
                .orElseThrow(()-> new UserNotFoundException);
    }

}
