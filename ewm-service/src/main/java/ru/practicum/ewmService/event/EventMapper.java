package ru.practicum.ewmService.event;

import ru.practicum.ewmService.event.category.CategoryMapper;
import ru.practicum.ewmService.event.category.model.Category;
import ru.practicum.ewmService.event.dto.*;
import ru.practicum.ewmService.event.model.Event;
import ru.practicum.ewmService.user.UserMapper;
import ru.practicum.ewmService.user.model.User;

public class EventMapper {

    public static EventShortDto toEventShortDto(Event event) {
        return EventShortDto.builder()
                .id(event.getId())
                .annotation(event.getAnnotation())
                .category(CategoryMapper.toCategoryDto(event.getCategory()))
                .confirmedRequests(event.getConfirmedRequests())
                .eventDate(event.getEventDate())
                .initiator(UserMapper.toUserShortDto(event.getInitiator()))
                .paid(event.getPaid())
                .title(event.getTitle())
                .views(event.getViews())
                .build();
    }

    public static EventFullDto toEventFullDto(Event event) {
        return EventFullDto.builder()
                .id(event.getId())
                .annotation(event.getAnnotation())
                .category(CategoryMapper.toCategoryDto(event.getCategory()))
                .confirmedRequests(event.getConfirmedRequests())
                .createdOn(event.getCreatedOn())
                .description(event.getDescription())
                .eventDate(event.getEventDate())
                .initiator(UserMapper.toUserShortDto(event.getInitiator()))
                .location(event.getLocation())
                .paid(event.getPaid())
                .participantLimit(event.getParticipantLimit())
                .publishedOn(event.getPublishedOn())
                .requestModeration(event.getRequestModeration())
                .state(event.getState())
                .title(event.getTitle())
                .views(event.getViews())
                .build();
    }

    public static Event toEventFromEventUpdateDto(EventUpdateDto updateDto) {
        return Event.builder()
                .id(updateDto.getEventId())
                .annotation(updateDto.getAnnotation())
                .description(updateDto.getDescription())
                .eventDate(updateDto.getEventDate())
                .paid(updateDto.getPaid())
                .participantLimit(updateDto.getParticipantLimit())
                .title(updateDto.getTitle())
                .build();
    }

    public static Event toEventFromEventAdminUpdateDto(EventAdminUpdateDto updateDto) {
        return Event.builder()
                .annotation(updateDto.getAnnotation())
                .description(updateDto.getDescription())
                .eventDate(updateDto.getEventDate())
                .location(updateDto.getLocation())
                .paid(updateDto.getPaid())
                .participantLimit(updateDto.getParticipantLimit())
                .title(updateDto.getTitle())
                .build();
    }

    public static Event toEventFromEventNewDto(EventNewDto eventNewDto) {
        return Event.builder()
                .annotation(eventNewDto.getAnnotation())
                .description(eventNewDto.getDescription())
                .eventDate(eventNewDto.getEventDate())
                .location(eventNewDto.getLocation())
                .paid(eventNewDto.getPaid())
                .participantLimit(eventNewDto.getParticipantLimit())
                .requestModeration(eventNewDto.getRequestModeration())
                .title(eventNewDto.getTitle())
                .build();
    }

    public static void updateNotNullFields(Event event, Event eventFromRepository) {
        if (event.getAnnotation() != null) {
            eventFromRepository.setAnnotation(event.getAnnotation());
        }
        if (event.getDescription() != null) {
            eventFromRepository.setDescription(event.getDescription());
        }
        if (event.getEventDate() != null) {
            eventFromRepository.setEventDate(event.getEventDate());
        }
        if (event.getLocation() != null) {
            eventFromRepository.setLocation(event.getLocation());
        }
        if (event.getPaid() != null) {
            eventFromRepository.setPaid(event.getPaid());
        }
        if (event.getParticipantLimit() != null) {
            eventFromRepository.setParticipantLimit(event.getParticipantLimit());
        }
        if (event.getTitle() != null) {
            eventFromRepository.setTitle(event.getTitle());
        }
    }

    public static void setCategory(Event event, Category category) {
        event.setCategory(category);
    }

    public static void setInitiator(Event event, User initiator) {
        event.setInitiator(initiator);
    }
}