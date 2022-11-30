package ru.practicum.ewmService.event.compilation.access;

import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.ewmService.common.PageRequestModified;
import ru.practicum.ewmService.event.EventRepository;
import ru.practicum.ewmService.event.compilation.CompilationMapper;
import ru.practicum.ewmService.event.compilation.CompilationRepository;
import ru.practicum.ewmService.event.compilation.access.common.CommonService;
import ru.practicum.ewmService.event.compilation.dto.CompilationDto;

import java.util.List;
import java.util.stream.Collectors;

@Service("PublicCompilationService")
@Transactional(readOnly = true)
public class CompilationService extends CommonService {

    public CompilationService(CompilationRepository compilationRepository, EventRepository eventRepository) {
        super(compilationRepository, eventRepository);
    }

    public List<CompilationDto> getEvents(Boolean pinned, Integer from, Integer size) {
        PageRequest pageRequest = new PageRequestModified(from, size, Sort.by("title"));
        if (pinned == null) {
            return compilationRepository.findAll(pageRequest)
                    .stream().map(CompilationMapper::toCompilationDto).collect(Collectors.toList());
        } else {
            return compilationRepository.findAllByPinnedIs(pinned)
                    .stream().map(CompilationMapper::toCompilationDto).collect(Collectors.toList());
        }
    }

    public CompilationDto getCompilation(long compId) {
        return CompilationMapper.toCompilationDto(findCompilationById(compId));
    }
}
