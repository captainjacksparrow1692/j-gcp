package uzumtech.j_gcp.mapper;

import org.springframework.stereotype.Component;
import uzumtech.j_gcp.dto.request.UserRequestDto;
import uzumtech.j_gcp.dto.response.MarkDeadResponseDto;
import uzumtech.j_gcp.dto.response.UserResponseDto;
import uzumtech.j_gcp.entity.User;

@Component
public class UserMapper {

    public User toEntity(UserRequestDto requestDto) {
        if (requestDto == null) {
            return null;
        }

        return User.builder()
                .fullName(requestDto.getFullName()) // ЭТОЙ СТРОЧКИ НЕ ХВАТАЛО
                .address(requestDto.getAddress())
                .pinfl(requestDto.getPinfl())
                .phoneNumber(requestDto.getPhoneNumber())
                .email(requestDto.getEmail())
                .age(requestDto.getAge())
                .photoUrl(requestDto.getPhotoUrl())
                .documentType(requestDto.getDocumentType())
                .issueDate(requestDto.getIssueDate())
                .expiryDate(requestDto.getExpiryDate())
                .citizenship(requestDto.getCitizenship())
                .deathDate(requestDto.getDeathDate())
                .build();
    }

    public UserResponseDto toResponseDto(User user) {
        if (user == null) {
            return null;
        }

        return UserResponseDto.builder()
                .id(user.getId())
                .fullName(user.getFullName())
                .address(user.getAddress())
                .phoneNumber(user.getPhoneNumber())
                .email(user.getEmail())
                .photoUrl(user.getPhotoUrl())
                .pinfl(user.getPinfl())
                .age(user.getAge())
                .documentType(user.getDocumentType())
                .issueDate(user.getIssueDate())
                .expiryDate(user.getExpiryDate())
                .citizenship(user.getCitizenship())
                .deathDate(user.getDeathDate())
                .build();
    }

    public MarkDeadResponseDto toMarkDeadResponseDto(User user) {
        if (user == null) {
            return null;
        }

        return MarkDeadResponseDto.builder()
                .userId(user.getId())
                .pinfl(user.getPinfl())
                .deathDate(user.getDeathDate())
                // Если в DTO есть поле status, добавь его здесь:
                // .status(user.getDeathDate() != null ? "DECEASED" : "ALIVE")
                .build();
    }
}