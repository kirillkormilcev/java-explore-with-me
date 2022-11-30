package ru.practicum.ewmService.event.access.user;

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.practicum.ewmService.event.dto.EventFullDto;
import ru.practicum.ewmService.event.dto.EventNewDto;
import ru.practicum.ewmService.event.dto.EventShortDto;
import ru.practicum.ewmService.event.dto.EventUpdateDto;
import ru.practicum.ewmService.request.dto.RequestDto;

import javax.validation.constraints.Positive;
import javax.validation.constraints.PositiveOrZero;
import java.util.List;

@Slf4j
@RestController("PrivateEventController")
@RequestMapping(path = "/users/{userId}/events")
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
@Validated
public class EventController {
    final EventService eventService;

    @GetMapping
    public ResponseEntity<List<EventShortDto>> getEventsOfInitiator(
            @PathVariable long userId,
            @PositiveOrZero @RequestParam(name = "from", required = false, defaultValue = "0") Integer from,
            @Positive @RequestParam(name = "size", required = false, defaultValue = "10") Integer size) {
        log.info("Обработка эндпойнта GET/users/" + userId + "/events?from=" + from + "&size=" + size + ".");
        return new ResponseEntity<>(eventService.getEventsOfInitiator(userId, from, size), HttpStatus.OK);
    }

    @PatchMapping
    public ResponseEntity<EventFullDto> updateEventByInitiator(
            @PathVariable long userId,
            @Validated @RequestBody EventUpdateDto eventUpdateDto) {
        log.info("Обработка эндпойнта PATCH/users/" + userId + "/events.(body: eventUpdateDto)");
        return new ResponseEntity<>(eventService.updateEventByInitiator(userId, eventUpdateDto), HttpStatus.OK);
    }

    @PostMapping
    public ResponseEntity<EventFullDto> createEventByInitiator(
            @Positive @PathVariable long userId,
            @Validated @RequestBody EventNewDto eventNewDto) {
        log.info("Обработка эндпойнта POST/users/" + userId + "/events.(body: eventNewDto)");
        return new ResponseEntity<>(eventService.createEventByInitiator(userId, eventNewDto), HttpStatus.OK);
    }

    @GetMapping("/{eventId}")
    public ResponseEntity<EventFullDto> getEventByIdByInitiator(
            @PathVariable long userId,
            @PathVariable long eventId) {
        log.info("Обработка эндпойнта GET/users/" + userId + "/" + eventId + ".");
        return new ResponseEntity<>(eventService.getEventByInitiator(userId, eventId), HttpStatus.OK);
    }

    @PatchMapping("/{eventId}")
    public ResponseEntity<EventFullDto> cancelEventByInitiator(
            @PathVariable long userId,
            @PathVariable long eventId) {
        log.info("Обработка эндпойнта PATCH/users/" + userId + "/" + eventId + ".");
        return new ResponseEntity<>(eventService.cancelEventByInitiator(userId, eventId), HttpStatus.OK);
    }

    @GetMapping("/{eventId}/requests")
    public ResponseEntity<List<RequestDto>> getRequestsOfEventByInitiator(
            @PathVariable long userId,
            @PathVariable long eventId) {
        log.info("Обработка эндпойнта GET/users/" + userId + "/" + eventId + "requests.");
        return new ResponseEntity<>(eventService.getRequestsOfEventByInitiatorId(userId, eventId), HttpStatus.OK);
    }

    @PatchMapping("/{eventId}/requests/{reqId}/confirm")
    public ResponseEntity<RequestDto> confirmRequestByInitiatorOfEvent(
            @PathVariable long userId,
            @PathVariable long eventId,
            @PathVariable long reqId) {
        log.info("Обработка эндпойнта GET/users/" + userId + "/" + eventId + "requests/" + reqId + "/confirm.");
        return new ResponseEntity<>(eventService.confirmRequestByInitiatorOfEvent(userId, eventId, reqId), HttpStatus.OK);
    }

    @PatchMapping("/{eventId}/requests/{reqId}/reject")
    public ResponseEntity<RequestDto> rejectRequestByInitiatorOfEvent(
            @PathVariable long userId,
            @PathVariable long eventId,
            @PathVariable long reqId) {
        log.info("Обработка эндпойнта GET/users/" + userId + "/" + eventId + "requests/" + reqId + "/reject.");
        return new ResponseEntity<>(eventService.rejectRequestByInitiatorOfEvent(userId, eventId, reqId), HttpStatus.OK);
    }
}
