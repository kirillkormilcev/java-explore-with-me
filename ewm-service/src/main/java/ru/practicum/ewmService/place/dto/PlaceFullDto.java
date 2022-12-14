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
public class PlaceFullDto {
    long id;
    String name;
    String description;
    PlaceFullDto.PlaceType placeType;
    Location location;
    Float radius;
    Boolean available;

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
