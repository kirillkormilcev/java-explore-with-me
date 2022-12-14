package ru.practicum.ewmService.place.dto;

import lombok.*;
import lombok.experimental.FieldDefaults;
import ru.practicum.ewmService.event.location.model.Location;
import ru.practicum.ewmService.place.model.PlaceType;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Positive;

@Getter
@Setter
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
@AllArgsConstructor(access = AccessLevel.PROTECTED)
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@ToString
public class PlaceNewDto {
    @NotNull
    @NotBlank
    String name;
    @NotBlank
    String description;
    @NotNull
    PlaceType placeType;
    @NotNull
    Location location;
    @NotNull
    @Positive
    Float radius;
}
