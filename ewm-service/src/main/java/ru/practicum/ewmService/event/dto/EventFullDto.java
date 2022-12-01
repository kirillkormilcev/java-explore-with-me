package ru.practicum.ewmService.event.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.*;
import lombok.experimental.FieldDefaults;
import ru.practicum.ewmService.event.category.dto.CategoryDto;
import ru.practicum.ewmService.event.location.model.Location;
import ru.practicum.ewmService.event.model.State;
import ru.practicum.ewmService.user.dto.UserShortDto;

import java.time.LocalDateTime;
import java.util.Set;

@Getter
@Setter
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
@AllArgsConstructor(access = AccessLevel.PROTECTED)
@NoArgsConstructor(access = AccessLevel.PUBLIC)
@ToString
@JsonInclude(JsonInclude.Include.NON_NULL)
public class EventFullDto {
    long id;
    String annotation;
    CategoryDto category;
    int confirmedRequests;
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss")
    LocalDateTime createdOn;
    String description;
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss")
    LocalDateTime eventDate;
    UserShortDto initiator;
    Location location;
    Boolean paid;
    Integer participantLimit;
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss")
    LocalDateTime publishedOn;
    Boolean requestModeration;
    State state;
    String title;
    long views;
    Set<Place> places;

    @Getter
    @Setter
    @FieldDefaults(level = AccessLevel.PRIVATE)
    @AllArgsConstructor(access = AccessLevel.PUBLIC)
    @NoArgsConstructor(access = AccessLevel.PUBLIC)
    @ToString
    public class Place {
        String name;
    }
}
