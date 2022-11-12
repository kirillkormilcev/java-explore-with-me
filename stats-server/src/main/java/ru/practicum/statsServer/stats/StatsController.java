package ru.practicum.statsServer.stats;

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.practicum.statsServer.stats.dto.EndpointHitDto;
import ru.practicum.statsServer.stats.dto.ViewStatsDto;

import javax.validation.constraints.NotNull;
import java.util.List;

@Slf4j
@RestController
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
@Validated
public class StatsController {
    final StatsService statsService;

    @PostMapping("/hit")
    public HttpStatus createHit(
            @RequestBody EndpointHitDto endpointHitDto) {
        log.info("Обработка эндпойнта POST/hit/.(body: EndpointHitDto)");
        statsService.createHit(endpointHitDto);
        return HttpStatus.OK;
    }

    @GetMapping("/stats")
    public ResponseEntity<List<ViewStatsDto>> getStats(
            @NotNull @RequestParam(name = "start") String start,
            @NotNull @RequestParam(name = "end") String end,
            @RequestParam(name = "uris", required = false) List<String> uris,
            @RequestParam(name = "unique", required = false, defaultValue = "false") Boolean unique) {
        return new ResponseEntity<>(statsService.getStats(start, end, uris, unique), HttpStatus.OK);
    }
}
