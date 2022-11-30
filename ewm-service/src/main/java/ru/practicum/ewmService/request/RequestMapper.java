package ru.practicum.ewmService.request;

import ru.practicum.ewmService.request.dto.RequestDto;
import ru.practicum.ewmService.request.model.Request;

import java.time.format.DateTimeFormatter;

public class RequestMapper {

    public static RequestDto toRequestDto(Request request) {
        return RequestDto.builder()
                .id(request.getId())
                .created(request.getCreated().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")))
                .event(request.getEvent().getId())
                .requester(request.getRequester().getId())
                .status(request.getStatus())
                .build();
    }
}
