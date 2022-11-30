package ru.practicum.statsServer.stats.dto;

import lombok.*;
import lombok.experimental.FieldDefaults;

@Getter
@Setter
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class ViewStatsDto {
    String app;
    String uri;
    long hits;
}
