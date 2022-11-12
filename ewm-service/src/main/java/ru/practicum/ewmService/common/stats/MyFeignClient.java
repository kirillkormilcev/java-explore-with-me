package ru.practicum.ewmService.common.stats;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import ru.practicum.ewmService.common.stats.dto.EndpointHitDto;
import ru.practicum.ewmService.common.stats.dto.ViewStatsDto;

import java.util.List;

@FeignClient(name = "myFeignClient", url = "${explore-with-me-stats-server.url}")
public interface MyFeignClient {

    @PostMapping("/hit")
    HttpStatus postHit(
            @RequestBody EndpointHitDto endpointHitDto);

    @GetMapping("/stats")
    List<ViewStatsDto> getStats(
            @RequestParam(name = "start") String start,
            @RequestParam(name = "end") String end,
            @RequestParam(name = "uris", required = false) List<String> uris,
            @RequestParam(name = "unique", required = false, defaultValue = "false") Boolean unique);
}
