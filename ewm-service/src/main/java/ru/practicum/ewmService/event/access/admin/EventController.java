package ru.practicum.ewmService.event.access.admin;

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.practicum.ewmService.event.dto.EventAdminUpdateDto;
import ru.practicum.ewmService.event.dto.EventFullDto;
import ru.practicum.ewmService.event.model.State;
import ru.practicum.ewmService.event.model.repository.EventParameters;

import javax.validation.constraints.Positive;
import javax.validation.constraints.PositiveOrZero;
import java.util.List;

@Slf4j
@RestController("AdminEventController")
@RequestMapping(path = "/admin/events")
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
@Validated
public class EventController {
    final EventService eventService;

    @GetMapping
    public ResponseEntity<List<EventFullDto>> getEvents(
            @RequestParam(name = "users", required = false) List<Long> users,
            @RequestParam(name = "states", required = false) List<State> states,
            @RequestParam(name = "categories", required = false) List<Long> categories,
            @RequestParam(name = "rangeStart", required = false) String rangeStart,
            @RequestParam(name = "rangeEnd", required = false) String rangeEnd,
            @PositiveOrZero @RequestParam(name = "from", required = false, defaultValue = "0") Integer from,
            @Positive @RequestParam(name = "size", required = false, defaultValue = "10") Integer size) {
        log.info("Обработка эндпойнта GET/events?users=" + users + "&states=" + states + "&categories=" + categories +
                "&rangeStart=" + rangeStart + "&rangeEnd=" + rangeEnd + "&from=" + from + "size=" + size + ".");
        EventParameters eventParameters = EventParameters.builder()
                .users(users)
                .states(states)
                .categories(categories)
                .rangeStartText(rangeStart)
                .rangeEndText(rangeEnd)
                .from(from)
                .size(size)
                .build();
        return new ResponseEntity<>(eventService.getEvents(eventParameters), HttpStatus.OK);
    }

    @PutMapping("/{eventId}")
    public ResponseEntity<EventFullDto> updateEventByAdmin(
            @PathVariable long eventId,
            @Validated @RequestBody EventAdminUpdateDto eventAdminUpdateDto) {
        log.info("Обработка эндпойнта PUT/admin/events/" + eventId + ".(body: eventAdminUpdateDto)");
        return new ResponseEntity<>(eventService.updateEventByAdmin(eventId, eventAdminUpdateDto), HttpStatus.OK);
    }

    @PatchMapping("/{eventId}/publish")
    public ResponseEntity<EventFullDto> publishEventByAdmin(
            @PathVariable long eventId) {
        log.info("Обработка эндпойнта PATCH/admin/events/" + eventId + "/publish.");
        return new ResponseEntity<>(eventService.publishEventByAdmin(eventId), HttpStatus.OK);
    }

    @PatchMapping("/{eventId}/reject")
    public ResponseEntity<EventFullDto> rejectEventByAdmin(
            @PathVariable long eventId) {
        log.info("Обработка эндпойнта PATCH/admin/events/" + eventId + "/reject.");
        return new ResponseEntity<>(eventService.rejectEventByAdmin(eventId), HttpStatus.OK);
    }
}
