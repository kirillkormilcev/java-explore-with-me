package ru.practicum.ewmService.event.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.*;
import lombok.experimental.FieldDefaults;
import ru.practicum.ewmService.event.category.dto.CategoryDto;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.time.LocalDateTime;

@Getter
@Setter
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
@AllArgsConstructor(access = AccessLevel.PROTECTED)
@NoArgsConstructor(access = AccessLevel.PUBLIC)
@ToString
public class EventShortDto {
    long id;
    String annotation;
    CategoryDto category;
    int confirmedRequests;
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss")
    LocalDateTime eventDate;
    EventShortDto.User initiator;
    Boolean paid;
    String title;
    long views;

    @Getter
    @Setter
    @FieldDefaults(level = AccessLevel.PRIVATE)
    @AllArgsConstructor(access = AccessLevel.PUBLIC)
    @NoArgsConstructor(access = AccessLevel.PUBLIC)
    @ToString
    public class User {
        @NotNull
        long id;
        @NotNull
        @NotBlank
        String name;
    }
}
