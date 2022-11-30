package ru.practicum.ewmService.request.access.user;

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.practicum.ewmService.request.dto.RequestDto;

import java.util.List;

@Slf4j
@RestController("PrivateRequestController")
@RequestMapping(path = "/users/{userId}/requests")
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
@Validated
public class RequestController {
    final RequestService requestService;

    @GetMapping
    public ResponseEntity<List<RequestDto>> getRequestsByUserInAnotherEvents(
            @PathVariable long userId) {
        log.info("Обработка эндпойнта GET/users/" + userId + "/requests.");
        return new ResponseEntity<>(requestService.getRequestsByUserInAnotherEvents(userId), HttpStatus.OK);
    }

    @PostMapping
    public ResponseEntity<RequestDto> createRequestByUser(
            @PathVariable long userId,
            @RequestParam(name = "eventId") long eventId) {
        log.info("Обработка эндпойнта POST/users/" + userId + "/requests?eventId=" + eventId + ".");
        return new ResponseEntity<>(requestService.createRequestByUser(userId, eventId), HttpStatus.OK);
    }

    @PatchMapping("/{requestId}/cancel")
    public ResponseEntity<RequestDto> cancelOwnRequestByUser(
            @PathVariable long userId,
            @PathVariable long requestId) {
        log.info("Обработка эндпойнта PATCH/users/" + userId + "/requests/" + requestId + "/cancel.");
        return new ResponseEntity<>(requestService.cancelOwnRequestByUser(userId, requestId), HttpStatus.OK);
    }
}
