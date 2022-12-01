package ru.practicum.ewmService.event.model.repository;

import lombok.*;
import lombok.experimental.FieldDefaults;
import ru.practicum.ewmService.event.model.State;

import java.util.List;

@Getter
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
@AllArgsConstructor(access = AccessLevel.PROTECTED)
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class EventParameters {
    String text;
    List<Long> users;
    List<State> states;
    List<Long> categories;
    String paid;
    String rangeStartText;
    String rangeEndText;
    String onlyAvailable;
    String place;
    String sort;
    Integer from;
    Integer size;
}
