package uzumtech.j_gcp.controller;

import jakarta.validation.Valid;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import uzumtech.j_gcp.dto.request.MarkDeadRequestDto;
import uzumtech.j_gcp.dto.request.UserRequestDto;
import uzumtech.j_gcp.dto.response.MarkDeadResponseDto;
import uzumtech.j_gcp.dto.response.UserResponseDto;
import uzumtech.j_gcp.service.UserService;

@Slf4j
@RestController
@RequestMapping("/api/v1/users") // Добавили версию API v1 (хорошая практика)
@RequiredArgsConstructor
@Validated
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class UserController {

    UserService userService;

    @PostMapping
    public ResponseEntity<UserResponseDto> createUser(@RequestBody @Valid UserRequestDto userRequestDto) {
        log.info("REST request to save User : {}", userRequestDto.pinfl());
        return new ResponseEntity<>(userService.createUser(userRequestDto), HttpStatus.CREATED);
    }

    @GetMapping("/{id}")
    public ResponseEntity<UserResponseDto> getUserById(@PathVariable Long id) {
        log.debug("REST request to get User by id : {}", id);
        return ResponseEntity.ok(userService.getUserById(id));
    }

    @GetMapping("/pinfl/{pinfl}")
    public ResponseEntity<UserResponseDto> getUserByPinfl(@PathVariable String pinfl) {
        log.debug("REST request to get User by PINFL : {}", pinfl);
        return ResponseEntity.ok(userService.getUserByUserPinfl(pinfl));
    }

    @GetMapping
    public ResponseEntity<Page<UserResponseDto>> getAllUsers(@PageableDefault(size = 20) Pageable pageable) {
        log.debug("REST request to get all Users");
        return ResponseEntity.ok(userService.getAllUsers(pageable));
    }

    @PostMapping("/{id}/mark-dead")
    public ResponseEntity<MarkDeadResponseDto> markUserAsDead(
            @PathVariable Long id,
            @RequestBody @Valid MarkDeadRequestDto request) {
        log.info("REST request to mark User as dead, id: {}", id);
        var deathDate = request.deathDate();
        return ResponseEntity.ok(userService.markUserAsDead(id, deathDate));
    }

    @GetMapping("/count")
    public ResponseEntity<Long> getUsersCountByStatus(@RequestParam UserService.Status status) {
        log.debug("REST request to get users count by status: {}", status);
        return ResponseEntity.ok(userService.getUsersCountByStatus(status));
    }

    @GetMapping("/documents/expired")
    public ResponseEntity<Page<UserResponseDto>> getExpired(@PageableDefault(size = 20) Pageable pageable) {
        log.debug("REST request to get users with expired documents");
        return ResponseEntity.ok(userService.getUsersWithExpiredDocuments(pageable));
    }
}