package j_gcp.controller;

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
import j_gcp.dto.request.MarkDeadRequestDto;
import j_gcp.dto.request.UserRequestDto;
import j_gcp.dto.response.MarkDeadResponseDto;
import j_gcp.dto.response.UserResponseDto;
import j_gcp.service.UserService;

@Slf4j
@RestController
@RequestMapping("/api/v1/users")
@RequiredArgsConstructor
@Validated
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class UserController {

    UserService userService;

    @PostMapping("/api/v1/users/create")
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

    @GetMapping("/search/email")
    public ResponseEntity<UserResponseDto> getUserByEmail(@RequestParam String email) {
        return ResponseEntity.ok(userService.getUserByEmail(email));
    }

    @GetMapping("/search/citizenship")
    public ResponseEntity<Page<UserResponseDto>> getUserByCitizenship(
            @RequestParam String citizenship,
            @PageableDefault Pageable pageable) {
        return ResponseEntity.ok(userService.getUserByCitizenship(citizenship, pageable));
    }

    @PostMapping("/{id}/mark-dead")
    public ResponseEntity<MarkDeadResponseDto> markUserAsDead(
            @PathVariable Long id,
            @RequestBody @Valid MarkDeadRequestDto request) {
        log.info("REST request to mark User as dead, id: {}", id);
        return ResponseEntity.ok(userService.markUserAsDead(id, request.deathDate()));
    }

    @GetMapping("/count")
    public ResponseEntity<Long> getUsersCountByStatus(@RequestParam String status) {
        log.debug("REST request to get users count by status: {}", status);
        return ResponseEntity.ok(userService.getUsersCountByStatus(UserService.Status.valueOf(status)));
    }

    @GetMapping("/expired")
    public ResponseEntity<Page<UserResponseDto>> getExpired(@PageableDefault(size = 20) Pageable pageable) {
        log.debug("REST request to get users with expired documents");
        return ResponseEntity.ok(userService.getUsersWithExpiredDocuments(pageable));
    }

    @GetMapping("/filter/alive")
    public ResponseEntity<Page<UserResponseDto>> getAlive(@PageableDefault Pageable pageable) {
        return ResponseEntity.ok(userService.getAllAliveUsers(pageable));
    }
}