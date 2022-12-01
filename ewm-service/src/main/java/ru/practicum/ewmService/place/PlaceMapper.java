package ru.practicum.ewmService.place;

import ru.practicum.ewmService.place.dto.PlaceFullDto;
import ru.practicum.ewmService.place.dto.PlaceShortDto;
import ru.practicum.ewmService.place.dto.PlaceNewDto;
import ru.practicum.ewmService.place.dto.PlaceUpdateDto;
import ru.practicum.ewmService.place.model.Place;

public class PlaceMapper {
    public static PlaceShortDto toPlaceShortDto(Place place) {
        return PlaceShortDto.builder()
                .name(place.getName())
                .description(place.getDescription())
                .placeType(new PlaceShortDto().new PlaceType(place.getPlaceType().getName()))
                .location(place.getLocation())
                .radius(place.getRadius())
                .build();
    }

    public static PlaceFullDto toPlaceFullDto(Place place) {
        return PlaceFullDto.builder()
                .id(place.getId())
                .name(place.getName())
                .description(place.getDescription())
                .placeType(new PlaceFullDto().new PlaceType(place.getPlaceType().getName()))
                .location(place.getLocation())
                .radius(place.getRadius())
                .available(place.getAvailable())
                .build();
    }

    public static Place toPlaceFromPlaceNewDto(PlaceNewDto placeNewDto) {
        return Place.builder()
                .name(placeNewDto.getName())
                .description(placeNewDto.getDescription())
                .placeType(placeNewDto.getPlaceType())
                .location(placeNewDto.getLocation())
                .radius(placeNewDto.getRadius())
                .build();
    }

    public static Place toPlaceFromPlaceUpdateDto(PlaceUpdateDto placeUpdateDto) {
        return Place.builder()
                .name(placeUpdateDto.getName())
                .description(placeUpdateDto.getDescription())
                .placeType(placeUpdateDto.getPlaceType())
                .location(placeUpdateDto.getLocation())
                .radius(placeUpdateDto.getRadius())
                .available(placeUpdateDto.getAvailable())
                .build();
    }

    public static void updateNotNullField(Place place, Place placeFromRepository) {
        if (place.getName() != null) {
            placeFromRepository.setName(place.getName());
        }
        if (place.getDescription() != null) {
            placeFromRepository.setDescription(place.getDescription());
        }
        if (place.getPlaceType() != null) {
            placeFromRepository.setPlaceType(place.getPlaceType());
        }
        if (place.getLocation() != null) {
            placeFromRepository.setLocation(place.getLocation());
        }
        if (place.getRadius() != null) {
            placeFromRepository.setRadius(place.getRadius());
        }
        if (place.getAvailable() != null) {
            placeFromRepository.setAvailable(place.getAvailable());
        }
    }
}
