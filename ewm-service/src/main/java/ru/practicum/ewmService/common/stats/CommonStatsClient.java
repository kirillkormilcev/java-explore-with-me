package ru.practicum.ewmService.common.stats;

import lombok.*;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import ru.practicum.ewmService.common.stats.dto.EndpointHitDto;
import ru.practicum.ewmService.common.stats.dto.ViewStatsDto;
import ru.practicum.ewmService.event.EventRepository;
import ru.practicum.ewmService.event.model.Event;

import javax.servlet.http.HttpServletRequest;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Slf4j
@FieldDefaults(level = AccessLevel.PROTECTED)
@RequiredArgsConstructor
public class CommonStatsClient {
    final MyFeignClient feignClient;
    final EventRepository eventRepository;

    protected void sendStats(HttpServletRequest request) {
        feignClient.postHit(EndpointHitDto.builder()
                .app("ewm-main-service")
                .uri(request.getRequestURI())
                .ip(request.getRemoteAddr())
                .timestamp(LocalDateTime.now())
                .build());
    }

    protected List<ViewStatsDto> getStats(List<Event> events) {
        LocalDateTime start = LocalDateTime.now();
        List<String> uris = new ArrayList<>();
        for (Event event: events) {
            uris.add("/events/" + event.getId());
            if (event.getCreatedOn().isBefore(start)) {
                start = event.getCreatedOn();
            }
        }
        LocalDateTime end = LocalDateTime.now();
        return feignClient.getStats(
                start.format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")),
                end.format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")),
                uris,
                true);
    }

    protected void updateViewsOfEvents(List<Event> events) {
        List<ViewStatsDto> views = getStats(events);
        Map<Long, Event> eventsMap = new HashMap<>();
        for (Event event: events) {
            eventsMap.put(event.getId(), event);
        }
        for (ViewStatsDto view: views) {
            String uri = view.getUri();
            System.out.println(uri);
            long eventId = Long.parseLong(uri.substring(uri.lastIndexOf("/") + 1));
            Event event = eventsMap.get(eventId);
            event.setViews(view.getHits());
        }
    }

    @Scheduled(fixedDelay = 60000, initialDelay = 60000)
    public void scheduledUpdateViewsOfEvents() {
        log.info("Обновление просмотров событий по расписанию.");
        updateViewsOfEvents(eventRepository.findAll());
    }
}
