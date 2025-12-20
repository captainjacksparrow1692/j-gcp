package uzumtech.j_gcp.service.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import uzumtech.j_gcp.dto.request.UserRequestDto;
import uzumtech.j_gcp.exception.UserAlreadyExistsException;
import uzumtech.j_gcp.repository.UserRepository;
import uzumtech.j_gcp.service.UserValidationService;

@Service
@RequiredArgsConstructor
public class UserValidationServiceImpl implements UserValidationService {

    private final UserRepository userRepository;

    @Override
    @Transactional(readOnly = true)
    public void validateUniqueness(UserRequestDto requestDto) {
        // Для обычного класса с @Getter используем get + имя поля

        if (userRepository.existsByPinfl(requestDto.getPinfl())) {
            throw new UserAlreadyExistsException("Пользователь с ПИНФЛ %s уже существует".formatted(requestDto.getPinfl()));
        }

        if (userRepository.existsByEmail(requestDto.getEmail())) {
            throw new UserAlreadyExistsException("Пользователь с email %s уже существует".formatted(requestDto.getEmail()));
        }

        if (userRepository.existsByPhoneNumber(requestDto.getPhoneNumber())) {
            throw new UserAlreadyExistsException("Пользователь с номером телефона %s уже существует".formatted(requestDto.getPhoneNumber()));
        }
    }
}