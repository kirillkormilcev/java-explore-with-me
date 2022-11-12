package ru.practicum.ewmService.event.compilation.access.admin;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.ewmService.error.exception.CompilationValidationException;
import ru.practicum.ewmService.error.exception.NotFoundException;
import ru.practicum.ewmService.event.EventRepository;
import ru.practicum.ewmService.event.compilation.CompilationMapper;
import ru.practicum.ewmService.event.compilation.CompilationRepository;
import ru.practicum.ewmService.event.compilation.access.common.CommonService;
import ru.practicum.ewmService.event.compilation.dto.CompilationDto;
import ru.practicum.ewmService.event.compilation.dto.CompilationNewDto;
import ru.practicum.ewmService.event.compilation.model.Compilation;
import ru.practicum.ewmService.event.model.Event;

import java.util.List;

@Service("AdminCompilationService")
@Transactional(readOnly = true)
public class CompilationService extends CommonService {

    public CompilationService(CompilationRepository compilationRepository, EventRepository eventRepository) {
        super(compilationRepository, eventRepository);
    }

    @Transactional
    public CompilationDto createCompilationByAdmin(CompilationNewDto compilationNewDto) {
        List<Event> events = eventRepository.findAllByIdIn(compilationNewDto.getEvents());
        Compilation newCompilation = Compilation.builder()
                .events(events)
                .pinned(compilationNewDto.getPinned())
                .title(compilationNewDto.getTitle())
                .build();
        return CompilationMapper.toCompilationDto(compilationRepository.save(newCompilation));
    }

    @Transactional
    public void deleteCompilationById(long compId) {
        findCompilationById(compId);
        compilationRepository.deleteById(compId);
    }

    @Transactional
    public void deleteEventFromCompilationByAdmin(long compId, long eventId) {
        Compilation compilation = findCompilationById(compId);
        List<Event> events = compilation.getEvents();
        Event event = findEventById(eventId);
        if (!events.contains(event)) {
            throw new NotFoundException("Подборка с индексом " + compId + " не содержит события с индексом " + eventId + ".");
        }
        events.remove(event);
        compilation.setEvents(events);
        compilationRepository.save(compilation);
    }

    @Transactional
    public void addEventToCompilationByAdmin(long compId, long eventId) {
        Compilation compilation = findCompilationById(compId);
        List<Event> events = compilation.getEvents();
        Event event = findEventById(eventId);
        if (events.contains(event)) {
            throw new NotFoundException("Подборка с индексом " + compId + " уже содержит событие с индексом " + eventId + ".");
        }
        events.add(event);
        compilation.setEvents(events);
        compilationRepository.save(compilation);
    }

    @Transactional
    public void unpinCompilationByAdmin(long compId) {
        Compilation compilation = findCompilationById(compId);
        if (!compilation.getPinned()) {
            throw new CompilationValidationException("Подборка уже откреплена.");
        }
        compilation.setPinned(false);
        compilationRepository.save(compilation);
    }

    @Transactional
    public void pinCompilationByAdmin(long compId) {
        Compilation compilation = findCompilationById(compId);
        if (compilation.getPinned()) {
            throw new CompilationValidationException("Подборка уже закреплена.");
        }
        compilation.setPinned(true);
        compilationRepository.save(compilation);
    }
}
