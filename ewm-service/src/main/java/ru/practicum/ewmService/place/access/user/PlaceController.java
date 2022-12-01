package ru.practicum.ewmService.place.access.user;

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.practicum.ewmService.place.dto.PlaceFullDto;
import ru.practicum.ewmService.place.dto.PlaceShortDto;

import java.util.List;

@Slf4j
@RestController("PrivatePlaceController")
@RequestMapping(path = "/users/places")
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
@Validated
public class PlaceController {
    final PlaceService placeService;

    @GetMapping
    public ResponseEntity<List<PlaceShortDto>> getAllPlacesByUser(
            @RequestParam(name = "name", required = false) String name,
            @RequestParam(name = "placeTypes", required = false) String placeTypes) {
        log.info("Обработка эндпойнта GET/users/places?name=" + name + "&placeTypes=" + placeTypes + ".");
        return new ResponseEntity<>(placeService.getAllPlacesByUser(name, placeTypes), HttpStatus.OK);
    }

    @GetMapping("/{placeId}")
    public ResponseEntity<PlaceFullDto> getPlaceByIdByUser(
            @PathVariable long placeId) {
        log.info("Обработка эндпойнта GET/users/places/" + placeId + ".");
        return new ResponseEntity<>(placeService.getPlaceByIdByUser(placeId), HttpStatus.OK);
    }

}
