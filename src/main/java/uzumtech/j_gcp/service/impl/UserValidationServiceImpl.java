package uzumtech.j_gcp.service.impl;

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.stereotype.Service;
import uzumtech.j_gcp.dto.request.UserRequestDto;
import uzumtech.j_gcp.repository.UserRepository;
import uzumtech.j_gcp.service.UserValidationService;

import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class UserValidationServiceImpl implements UserValidationService {

    UserRepository userRepository;

    @Override
    public void validateUniqueness(UserRequestDto requestDto) {
        List<String> errors = new ArrayList<>();

        String pinfl = requestDto.pinfl();
        String email = requestDto.email();
        String phone = requestDto.phoneNumber();

        if (pinfl != null && userRepository.existsByPinfl(pinfl)) {
            errors.add(String.format("Пользователь с ПИНФЛ %s уже существует", pinfl));
        }

        if (email != null && userRepository.existsByEmail(email)) {
            errors.add(String.format("Пользователь с email %s уже существует", email));
        }

        if (phone != null && userRepository.existsByPhoneNumber(phone)) {
            errors.add(String.format("Пользователь с номером телефона %s уже существует", phone));
        }

        if (!errors.isEmpty()) {
            throw new IllegalArgumentException(String.join("; ", errors));
        }
    }
}