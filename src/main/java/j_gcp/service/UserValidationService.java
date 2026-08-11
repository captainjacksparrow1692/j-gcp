package j_gcp.service;

import j_gcp.dto.request.UserRequestDto;

public interface UserValidationService {

    void validateUniqueness(UserRequestDto requestDto);
}