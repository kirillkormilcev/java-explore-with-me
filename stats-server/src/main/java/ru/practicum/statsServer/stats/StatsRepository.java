package ru.practicum.statsServer.stats;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import ru.practicum.statsServer.stats.model.EndpointHit;
import ru.practicum.statsServer.stats.dto.ViewStatsDto;

import java.time.LocalDateTime;
import java.util.List;

public interface StatsRepository extends JpaRepository<EndpointHit, Long> {

    @Query("select new ru.practicum.statsServer.stats.dto.ViewStatsDto(e.app, e.uri, count(distinct e.ip)) " +
            "from EndpointHit e " +
            "where e.timestamp between ?2 and ?3 " +
            "and e.uri in ?1 " +
            "group by e.app, e.uri")
    List<ViewStatsDto> findViewsOfUrisFromUniqueIp(List<String> uris, LocalDateTime start, LocalDateTime end);

    @Query("select new ru.practicum.statsServer.stats.dto.ViewStatsDto(e.app, e.uri, count(e.ip)) " +
            "from EndpointHit e " +
            "where e.timestamp between ?2 and ?3 " +
            "and e.uri in ?1 " +
            "group by e.app, e.uri")
    List<ViewStatsDto> findViewsOfUrisFromNotUniqueIp(List<String> uris, LocalDateTime start, LocalDateTime end);
}
