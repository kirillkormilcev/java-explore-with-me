package ru.practicum.ewmService.event.compilation.access.common;

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import ru.practicum.ewmService.error.exception.NotFoundException;
import ru.practicum.ewmService.event.EventRepository;
import ru.practicum.ewmService.event.compilation.CompilationRepository;
import ru.practicum.ewmService.event.compilation.model.Compilation;
import ru.practicum.ewmService.event.model.Event;

@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PROTECTED)
public class CommonService {
    final CompilationRepository compilationRepository;
    final EventRepository eventRepository;

    protected Compilation findCompilationById(long compId) {
        return compilationRepository.findById(compId).orElseThrow(() ->
                new NotFoundException("Подборка с индексом " + compId + " не найдена в базе."));
    }

    protected Event findEventById(long eventId) {
        return eventRepository.findById(eventId).orElseThrow(() ->
                new NotFoundException("Событие с индексом " + eventId + " не найдено в базе."));
    }
}
