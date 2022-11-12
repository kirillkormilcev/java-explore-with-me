package ru.practicum.statsServer.stats;

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.statsServer.stats.dto.ViewStatsDto;
import ru.practicum.statsServer.stats.dto.EndpointHitDto;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.ArrayList;
import java.util.List;

@Slf4j
@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class StatsService {
    final StatsRepository statsRepository;

    @Transactional
    public void createHit(EndpointHitDto endpointHitDto) {
        statsRepository.save(StatsMapper.toEndpointHit(endpointHitDto));
    }

    public List<ViewStatsDto> getStats(String startText, String endText, List<String> uris, Boolean unique) throws DateTimeParseException {
        LocalDateTime start = null;
        LocalDateTime end = null;
        try {
            start = LocalDateTime.parse(startText, DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));
            end = LocalDateTime.parse(endText, DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));
        } catch (DateTimeParseException e) {
            log.info("Ошибка при обработке параметров start=" + startText + " и end=" + endText + "в запросе: " +
                    "GET/stats");
        }
        if (uris == null || uris.isEmpty()) {
            return new ArrayList<>();
        } else {
            if (unique) {
                return statsRepository.findViewsOfUrisFromUniqueIp(uris, start, end);
            } else {
                return statsRepository.findViewsOfUrisFromNotUniqueIp(uris, start, end);
            }
        }
    }
}
