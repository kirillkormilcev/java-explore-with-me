package ru.practicum.ewmService.event.compilation.access;

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.practicum.ewmService.event.compilation.dto.CompilationDto;

import javax.validation.constraints.Positive;
import javax.validation.constraints.PositiveOrZero;
import java.util.List;

@Slf4j
@RestController("PublicCompilationController")
@RequestMapping(path = "/compilations")
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
@Validated
public class CompilationController {
    final CompilationService compilationService;

    @GetMapping
    public ResponseEntity<List<CompilationDto>> getCompilations(
            @RequestParam(name = "pinned", required = false) Boolean pinned,
            @PositiveOrZero @RequestParam(name = "from", required = false, defaultValue = "0") Integer from,
            @Positive @RequestParam(name = "size", required = false, defaultValue = "10") Integer size) {
        log.info("Обработка эндпойнта GET/compilations?pinned=" + pinned + "&from=" + from + "&size=" + size + ".");
        return new ResponseEntity<>(compilationService.getEvents(pinned, from, size), HttpStatus.OK);
    }

    @GetMapping("/{compId}")
    public ResponseEntity<CompilationDto> getCompilation(
            @PathVariable long compId) {
        log.info("Обработка эндпойнта GET/compilations/" + compId + ".");
        return new ResponseEntity<>(compilationService.getCompilation(compId), HttpStatus.OK);
    }
}
