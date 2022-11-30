package ru.practicum.ewmService.event.compilation.access.admin;

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.practicum.ewmService.event.compilation.dto.CompilationDto;
import ru.practicum.ewmService.event.compilation.dto.CompilationNewDto;

import javax.validation.constraints.Positive;

@Slf4j
@RestController("AdminCompilationController")
@RequestMapping(path = "/admin/compilations")
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
@Validated
public class CompilationController {
    final CompilationService compilationService;

    @PostMapping
    public ResponseEntity<CompilationDto> createCompilationByAdmin(
            @Validated @RequestBody CompilationNewDto compilationNewDto) {
        log.info("Обработка эндпойнта POST/admin/compilation.(body: compilationNewDto)");
        return new ResponseEntity<>(compilationService.createCompilationByAdmin(compilationNewDto), HttpStatus.OK);
    }

    @DeleteMapping("/{compId}")
    public HttpStatus deleteCompilationById(
            @PathVariable long compId) {
        log.info("Обработка эндпойнта DELETE/admin/compilations/" + compId + ".");
        compilationService.deleteCompilationById(compId);
        return HttpStatus.OK;
    }

    @DeleteMapping("/{compId}/events/{eventId}")
    public HttpStatus deleteEventFromCompilationByAdmin(
            @PathVariable long compId,
            @PathVariable long eventId) {
        log.info("Обработка эндпойнта DELETE/admin/compilations/" + compId + "/events/" + eventId + ".");
        compilationService.deleteEventFromCompilationByAdmin(compId, eventId);
        return HttpStatus.OK;
    }

    @PatchMapping("/{compId}/events/{eventId}")
    public HttpStatus addEventToCompilationByAdmin(
            @PathVariable long compId,
            @PathVariable long eventId) {
        log.info("Обработка эндпойнта PATCH/admin/compilations/" + compId + "/events/" + eventId + ".");
        compilationService.addEventToCompilationByAdmin(compId, eventId);
        return HttpStatus.OK;
    }

    @DeleteMapping("/{compId}/pin")
    public HttpStatus unpinCompilationByAdmin(
            @Positive @PathVariable long compId) {
        log.info("Обработка эндпойнта DELETE/admin/compilations/" + compId + "/pin.");
        compilationService.unpinCompilationByAdmin(compId);
        return HttpStatus.OK;
    }

    @PatchMapping("/{compId}/pin")
    public HttpStatus pinCompilationByAdmin(
            @PathVariable long compId) {
        log.info("Обработка эндпойнта PATCH/admin/compilations/" + compId + "/pin.");
        compilationService.pinCompilationByAdmin(compId);
        return HttpStatus.OK;
    }
}
