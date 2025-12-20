package uzumtech.j_gcp.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import uzumtech.j_gcp.constant.DocumentType;
import uzumtech.j_gcp.dto.request.UserRequestDto;
import uzumtech.j_gcp.dto.response.MarkDeadResponseDto;
import uzumtech.j_gcp.dto.response.UserResponseDto;
import uzumtech.j_gcp.service.UserService;

import java.time.LocalDate;
import java.util.List;

@RestController
@RequestMapping("/api/v1/users")
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;

    // --- ОСНОВНЫЕ ОПЕРАЦИИ (CRUD) ---

    @PostMapping
    public ResponseEntity<UserResponseDto> createUser(@RequestBody UserRequestDto requestDto) {
        return new ResponseEntity<>(userService.createUser(requestDto), HttpStatus.CREATED);
    }

    @GetMapping
    public ResponseEntity<Page<UserResponseDto>> getAllUsers(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {
        return ResponseEntity.ok(userService.getAllUsers(PageRequest.of(page, size)));
    }

    @GetMapping("/{id}")
    public ResponseEntity<UserResponseDto> getUserById(@PathVariable Long id) {
        return ResponseEntity.ok(userService.getUserById(id));
    }

    @GetMapping("/pinfl/{pinfl}")
    public ResponseEntity<UserResponseDto> getUserByPinfl(@PathVariable String pinfl) {
        return ResponseEntity.ok(userService.getUserByUserPinfl(pinfl));
    }

    // --- ЛОГИКА СМЕРТИ ---

    @PatchMapping("/{id}/mark-dead")
    public ResponseEntity<MarkDeadResponseDto> markAsDead(
            @PathVariable Long id,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate deathDate) {
        return ResponseEntity.ok(userService.markUserAsDead(id, deathDate));
    }

    @GetMapping("/{id}/is-alive")
    public ResponseEntity<Boolean> isAlive(@PathVariable Long id) {
        return ResponseEntity.ok(userService.isUserAlive(id));
    }

    // --- ПОИСК И ФИЛЬТРЫ ---

    @GetMapping("/search")
    public ResponseEntity<List<UserResponseDto>> searchByName(@RequestParam String name) {
        return ResponseEntity.ok(userService.searchUsersByName(name));
    }

    @GetMapping("/filter/alive")
    public ResponseEntity<List<UserResponseDto>> getAlive() {
        return ResponseEntity.ok(userService.getAllAliveUsers());
    }

    @GetMapping("/filter/dead")
    public ResponseEntity<List<UserResponseDto>> getDead() {
        return ResponseEntity.ok(userService.getAllDeadUsers());
    }

    // --- ДОКУМЕНТЫ ---

    @GetMapping("/documents/expired")
    public ResponseEntity<List<UserResponseDto>> getWithExpiredDocs() {
        return ResponseEntity.ok(userService.getUsersWithExpiredDocuments());
    }

    @GetMapping("/documents/type")
    public ResponseEntity<List<UserResponseDto>> getByDocType(@RequestParam DocumentType type) {
        return ResponseEntity.ok(userService.getUsersByDocumentType(type));
    }

    @GetMapping("/documents/expiring-between")
    public ResponseEntity<List<UserResponseDto>> getExpiringBetween(
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate start,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate end) {
        return ResponseEntity.ok(userService.getUsersWithDocumentsExpiringBetween(start, end));
    }

    // --- СТАТИСТИКА ---

    @GetMapping("/stats/count-alive")
    public ResponseEntity<Long> countAlive() {
        return ResponseEntity.ok(userService.getAllAliveUsersCount());
    }

    @GetMapping("/stats/count-dead")
    public ResponseEntity<Long> countDead() {
        return ResponseEntity.ok(userService.getAllDeadUsersCount());
    }
}