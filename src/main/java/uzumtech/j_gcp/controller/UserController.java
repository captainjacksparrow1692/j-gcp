package uzumtech.j_gcp.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
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

@RestController
@RequestMapping("/api/v1/users")
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;

    //CRUD

    @PostMapping
    public ResponseEntity<UserResponseDto> createUser(@RequestBody UserRequestDto requestDto) {
        return new ResponseEntity<>(userService.createUser(requestDto), HttpStatus.CREATED);
    }

    @GetMapping
    public ResponseEntity<Page<UserResponseDto>> getAllUsers(Pageable pageable) {
        return ResponseEntity.ok(userService.getAllUsers(pageable));
    }

    @GetMapping("/{id}")
    public ResponseEntity<UserResponseDto> getUserById(@PathVariable Long id) {
        return ResponseEntity.ok(userService.getUserById(id));
    }

    @GetMapping("/pinfl/{pinfl}")
    public ResponseEntity<UserResponseDto> getUserByPinfl(@PathVariable String pinfl) {
        return ResponseEntity.ok(userService.getUserByUserPinfl(pinfl));
    }

    //Статус жизни

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

    //Поиск и фильтры

    @GetMapping("/search")
    public ResponseEntity<Page<UserResponseDto>> searchByName(
            @RequestParam String name,
            Pageable pageable) {
        return ResponseEntity.ok(userService.searchUsersByName(name, pageable));
    }

    @GetMapping("/filter/status")
    public ResponseEntity<Page<UserResponseDto>> filterByStatus(
            @RequestParam String status,
            Pageable pageable) {
        if ("ALIVE".equalsIgnoreCase(status)) {
            return ResponseEntity.ok(userService.getAllAliveUsers(pageable));
        } else if ("DEAD".equalsIgnoreCase(status)) {
            return ResponseEntity.ok(userService.getAllDeadUsers(pageable));
        } else {
            return ResponseEntity.badRequest().build();
        }
    }

    //Работа с документами

    @GetMapping("/documents/expired")
    public ResponseEntity<Page<UserResponseDto>> getWithExpiredDocs(Pageable pageable) {
        return ResponseEntity.ok(userService.getUsersWithExpiredDocuments(pageable));
    }

    @GetMapping("/documents/type/{type}")
    public ResponseEntity<Page<UserResponseDto>> getByDocType(
            @PathVariable DocumentType type,
            Pageable pageable) {
        return ResponseEntity.ok(userService.getUsersByDocumentType(type, pageable));
    }

    @GetMapping("/documents/expiring-between")
    public ResponseEntity<Page<UserResponseDto>> getExpiringBetween(
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate start,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate end,
            Pageable pageable) {
        return ResponseEntity.ok(userService.getUsersWithDocumentsExpiringBetween(start, end, pageable));
    }

    //Статистика

    @GetMapping("/stats/count-alive")
    public ResponseEntity<Long> countAlive() {
        return ResponseEntity.ok(userService.getAllAliveUsers());
    }

    @GetMapping("/stats/count-dead")
    public ResponseEntity<Long> countDead() {
        return ResponseEntity.ok(userService.getAllDeadUsers());
    }
}
