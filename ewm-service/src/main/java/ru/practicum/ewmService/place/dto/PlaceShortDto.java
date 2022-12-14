package ru.practicum.ewmService.place.dto;

import lombok.*;
import lombok.experimental.FieldDefaults;
import ru.practicum.ewmService.event.location.model.Location;

@Getter
@Setter
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
@AllArgsConstructor(access = AccessLevel.PROTECTED)
@NoArgsConstructor(access = AccessLevel.PUBLIC)
@ToString
public class PlaceShortDto {
    String name;
    String description;
    PlaceShortDto.PlaceType placeType;
    Location location;
    Float radius;

    @Getter
    @Setter
    @FieldDefaults(level = AccessLevel.PRIVATE)
    @AllArgsConstructor(access = AccessLevel.PUBLIC)
    @NoArgsConstructor(access = AccessLevel.PUBLIC)
    @ToString
    public class PlaceType {
        String name;
    }
}
