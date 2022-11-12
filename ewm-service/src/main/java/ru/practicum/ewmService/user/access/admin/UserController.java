package ru.practicum.ewmService.user.access.admin;

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.practicum.ewmService.user.dto.UserDto;
import ru.practicum.ewmService.user.dto.UserNewDto;

import javax.validation.constraints.Positive;
import javax.validation.constraints.PositiveOrZero;
import java.util.List;

@Slf4j
@RestController
@RequestMapping(path = "/admin/users")
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
@Validated
public class UserController {
    final UserService userService;

    @GetMapping
    public ResponseEntity<List<UserDto>> getUsersByAdmin(
            @RequestParam(name = "ids", required = false) List<Long> ids,
            @PositiveOrZero @RequestParam(name = "from", required = false, defaultValue = "0") Integer from,
            @Positive @RequestParam(name = "size", required = false, defaultValue = "10") Integer size) {
        log.info("Обработка эндпойнта GET/admin/users&ids=" + ids + "&from=" + from + "&size=" + size + ".");
        return new ResponseEntity<>(userService.getEventsByAdmin(ids, from, size), HttpStatus.OK);
    }

    @PostMapping
    public ResponseEntity<UserDto> createUserByAdmin(
            @Validated @RequestBody UserNewDto userNewDto) {
        log.info("Обработка эндпойнта POST/admin/users.(body: UserNewDto)");
        return new ResponseEntity<>(userService.createUserByAdmin(userNewDto), HttpStatus.OK);
    }

    @DeleteMapping("/{userId}")
    public HttpStatus deleteUserByAdmin(
            @PathVariable Long userId) {
        log.info("Обработка эндпойнта DELETE/admin/users/" + userId + ".");
        userService.deleteUserByAdmin(userId);
        return HttpStatus.OK;
    }
}
