package uzumtech.j_gcp.controller;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import uzumtech.j_gcp.constant.enums.DocumentType;
import uzumtech.j_gcp.dto.request.MarkDeadRequestDto;
import uzumtech.j_gcp.dto.request.UserRequestDto;
import uzumtech.j_gcp.dto.response.MarkDeadResponseDto;
import uzumtech.j_gcp.dto.response.UserResponseDto;
import uzumtech.j_gcp.service.UserService;

import jakarta.validation.Valid;
import java.time.LocalDate;

@RestController
@RequestMapping("/api/users")
@RequiredArgsConstructor
@Validated
public class UserController {

    private final UserService userService;

    //CRUD
    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public UserResponseDto createUser(@RequestBody @Valid UserRequestDto userRequestDto) {
        return userService.createUser(userRequestDto);
    }

    @GetMapping
    public Page<UserResponseDto> getAllUsers(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "100") int size) {

        return userService.getAllUsers(PageRequest.of(page, size));
    }

    @GetMapping("/{id}")
    public UserResponseDto getUserById(@PathVariable Long id) {
        return userService.getUserById(id);
    }

    @GetMapping("/pinfl/{pinfl}")
    public UserResponseDto getUserByPinfl(@PathVariable String pinfl) {
        return userService.getUserByUserPinfl(pinfl);
    }

    //Поиск
    @GetMapping("/search")
    public Page<UserResponseDto> searchUsersByName(@RequestParam String fullName, Pageable pageable) {
        return userService.searchUsersByName(fullName, pageable);
    }

    @GetMapping("/alive")
    public Page<UserResponseDto> getAllAliveUsers(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "100") int size) {

        return userService.getAllAliveUsers(PageRequest.of(page, size));
    }

    @GetMapping("/dead")
    public Page<UserResponseDto> getAllDeadUsers(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "100") int size) {

        return userService.getAllDeadUsers(PageRequest.of(page, size));
    }

    //Статус жизни
    @GetMapping("/{id}/alive")
    public boolean isUserAlive(@PathVariable Long id) {
        return userService.isUserAlive(id);
    }

    @PostMapping("/{id}/mark-dead")
    public MarkDeadResponseDto markUserAsDead(
            @PathVariable Long id,
            @RequestBody @Valid MarkDeadRequestDto request) {
        LocalDate deathDate = request.deathDate();
        return userService.markUserAsDead(id, deathDate);
    }

    //Статистика
    @GetMapping("/count")
    public long getUsersCountByStatus(@RequestParam UserService.Status status) {
        return userService.getUsersCountByStatus(status);
    }

    // Документы
    @GetMapping("/documents/expired")
    public Page<UserResponseDto> getUsersWithExpiredDocuments(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "100") int size) {
        return userService.getUsersWithExpiredDocuments(PageRequest.of(page, size));
    }

    @GetMapping("/documents/expiring")
    public Page<UserResponseDto> getUsersWithDocumentsExpiringBetween(
            @RequestParam LocalDate start,
            @RequestParam LocalDate end,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "100") int size) {
        return userService.getUsersWithDocumentsExpiringBetween(start, end, PageRequest.of(page, size));
    }

    @GetMapping("/documents/type/{type}")
    public Page<UserResponseDto> getUsersByDocumentType(
            @PathVariable DocumentType type,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "100") int size) {
        return userService.getUsersByDocumentType(type, PageRequest.of(page, size));
    }

    // Сложные запросы
    @GetMapping("/alive/expired-documents")
    public Page<UserResponseDto> getAliveUsersWithExpiredDocuments(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "100") int size) {
        return userService.getAliveUsersWithExpiredDocuments(PageRequest.of(page, size));
    }
}
