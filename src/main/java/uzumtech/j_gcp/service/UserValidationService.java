package uzumtech.j_gcp.service;

import uzumtech.j_gcp.dto.request.UserRequestDto;

public interface UserValidationService {

    void validateUniqueness(UserRequestDto requestDto);
}