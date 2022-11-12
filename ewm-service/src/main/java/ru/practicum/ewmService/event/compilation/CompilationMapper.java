package ru.practicum.ewmService.event.compilation;

import ru.practicum.ewmService.event.EventMapper;
import ru.practicum.ewmService.event.compilation.dto.CompilationDto;
import ru.practicum.ewmService.event.compilation.dto.CompilationNewDto;
import ru.practicum.ewmService.event.compilation.model.Compilation;

import java.util.stream.Collectors;

public class CompilationMapper {

    public static Compilation toCompilation(CompilationNewDto compilationNewDto) {
        return Compilation.builder()
                .pinned(compilationNewDto.getPinned())
                .title(compilationNewDto.getTitle())
                .build();
    }

    public static CompilationDto toCompilationDto(Compilation compilation) {
        return CompilationDto.builder()
                .id(compilation.getId())
                .events(compilation.getEvents()
                        .stream().map(EventMapper::toEventShortDto).collect(Collectors.toList()))
                .pinned(compilation.getPinned())
                .title(compilation.getTitle())
                .build();
    }
}
