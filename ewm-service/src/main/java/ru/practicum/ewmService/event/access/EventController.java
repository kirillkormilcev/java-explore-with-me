package ru.practicum.ewmService.event.access;

import lombok.AccessLevel;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.practicum.ewmService.common.stats.CommonStatsClient;
import ru.practicum.ewmService.common.stats.MyFeignClient;
import ru.practicum.ewmService.event.EventRepository;
import ru.practicum.ewmService.event.dto.EventFullDto;
import ru.practicum.ewmService.event.dto.EventShortDto;
import ru.practicum.ewmService.event.model.repository.EventParameters;

import javax.servlet.http.HttpServletRequest;
import javax.validation.constraints.Positive;
import javax.validation.constraints.PositiveOrZero;
import java.util.List;

@Slf4j
@RestController("PublicEventController")
@RequestMapping(path = "/events")
@FieldDefaults(level = AccessLevel.PRIVATE)
@Validated
public class EventController extends CommonStatsClient {
    final EventService eventService;

    public EventController(MyFeignClient feignClient, EventRepository eventRepository, EventService eventService) {
        super(feignClient, eventRepository);
        this.eventService = eventService;
    }

    @GetMapping
    public ResponseEntity<List<EventShortDto>> getEvents(
            @RequestParam(name = "text", required = false) String text,
            @RequestParam(name = "categories", required = false)List<Long> categories,
            @RequestParam(name = "paid", required = false) String paid,
            @RequestParam(name = "rangeStart", required = false) String rangeStart,
            @RequestParam(name = "rangeEnd", required = false) String rangeEnd,
            @RequestParam(name = "onlyAvailable", required = false, defaultValue = "false") String onlyAvailable,
            @RequestParam(name = "sort", required = false, defaultValue = "EVENT_DATE") String sort,
            @PositiveOrZero @RequestParam(name = "from", required = false, defaultValue = "0") Integer from,
            @Positive @RequestParam(name = "size", required = false, defaultValue = "10") Integer size,
            HttpServletRequest request) {
        log.info("Обработка эндпойнта GET/events?text=" + text + "&categories=" + categories + "&paid=" + paid +
                "&rangeStart=" + rangeStart + "&rangeEnd=" + rangeEnd + "&onlyAvailable=" + onlyAvailable + "&sort=" +
                sort + "&from=" + from + "size=" + size + ".");
        sendStats(request);
        EventParameters eventParameters = EventParameters.builder()
                .text(text)
                .categories(categories)
                .paid(paid)
                .rangeStartText(rangeStart)
                .rangeEndText(rangeEnd)
                .onlyAvailable(onlyAvailable)
                .sort(sort)
                .from(from)
                .size(size)
                .build();
        return new ResponseEntity<>(eventService.getEvents(eventParameters), HttpStatus.OK);
    }

    @GetMapping("/{eventId}")
    public ResponseEntity<EventFullDto> getEvent(
            @PathVariable long eventId,
            HttpServletRequest request) {
        log.info("Обработка эндпойнта GET/events/" + eventId + ".");
        sendStats(request);
        return new ResponseEntity<>(eventService.getEventById(eventId), HttpStatus.OK);
    }
}
