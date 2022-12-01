package ru.practicum.ewmService.place.access.admin;

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.practicum.ewmService.place.dto.PlaceFullDto;
import ru.practicum.ewmService.place.dto.PlaceNewDto;
import ru.practicum.ewmService.place.dto.PlaceUpdateDto;

import java.util.List;

@Slf4j
@RestController("AdminPlaceController")
@RequestMapping(path = "/admin/places")
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
@Validated
public class PlaceController {
    final PlaceService placeService;

    @GetMapping
    public ResponseEntity<List<PlaceFullDto>> getAllPlacesByAdmin(
            @RequestParam(name = "name", required = false) String name) {
        log.info("Обработка эндпойнта GET/admin/places?name=" + name + ".");
        return new ResponseEntity<>(placeService.getAllPlacesByAdmin(name), HttpStatus.OK);
    }

    @PostMapping
    public ResponseEntity<PlaceFullDto> createPlaceByAdmin(
            @Validated @RequestBody PlaceNewDto placeNewDto) {
        log.info("Обработка эндпойнта POST/admin/places/.(body: placeNewDto)");
        return new ResponseEntity<>(placeService.createPlaceByAdmin(placeNewDto), HttpStatus.OK);
    }

    @PatchMapping("/{placeId}")
    public ResponseEntity<PlaceFullDto> updatePlaceByAdmin(
            @PathVariable long placeId,
            @Validated @RequestBody PlaceUpdateDto placeUpdateDto) {
        log.info("Обработка эндпойнта PATCH/admin/places/" + placeId + ".(body: placeUpdateDto)");
        return new ResponseEntity<>(placeService.updatePlaceByAdmin(placeId, placeUpdateDto), HttpStatus.OK);

    }

    @DeleteMapping("/{placeId}")
    public void deletePlaceByAdmin(
            @PathVariable long placeId) {
        log.info("Обработка эндпойнта DELETE/admin/places/" + placeId + ".");
        placeService.deletePlaceByAdmin(placeId);
    }
}
