package ru.practicum.statsServer.stats;

import ru.practicum.statsServer.stats.dto.EndpointHitDto;
import ru.practicum.statsServer.stats.model.EndpointHit;

public class StatsMapper {
    public static EndpointHit toEndpointHit(EndpointHitDto endpointHitDto) {
        return EndpointHit.builder()
                .app(endpointHitDto.getApp())
                .uri(endpointHitDto.getUri())
                .ip(endpointHitDto.getIp())
                .timestamp(endpointHitDto.getTimestamp())
                .build();
    }
}
