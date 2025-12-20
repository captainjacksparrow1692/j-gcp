package uzumtech.j_gcp.service.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import uzumtech.j_gcp.dto.request.UserRequestDto;
import uzumtech.j_gcp.repository.UserRepository;
import uzumtech.j_gcp.service.UserValidationService;

@Service
@RequiredArgsConstructor
public class UserValidationServiceImpl implements UserValidationService {

    private final UserRepository userRepository;

    // проверяет уникальность email, pinfl и номера телефона перед созданием нового пользователя
    @Override
    @Transactional(readOnly = true)
    public void validateUniqueness(UserRequestDto requestDto) {
        if (userRepository.existsByPinfl(requestDto.pinfl())) {
            throw new ConflictException("User with this PINFL already exists");
        }

        if (userRepository.existsByEmail(requestDto.email())) {
            throw new ConflictException("User with this email already exists");
        }

        if (userRepository.existsByPhoneNumber(requestDto.phoneNumber())) {
            throw new ConflictException("User with this phone number already exists");
        }
    }
}
