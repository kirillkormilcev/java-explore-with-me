package ru.practicum.ewmService.place.access.user;

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.ewmService.error.exception.NotFoundException;
import ru.practicum.ewmService.place.PlaceMapper;
import ru.practicum.ewmService.place.PlaceRepository;
import ru.practicum.ewmService.place.dto.PlaceFullDto;
import ru.practicum.ewmService.place.dto.PlaceShortDto;
import ru.practicum.ewmService.place.model.Place;

import java.util.List;
import java.util.stream.Collectors;

@Service("PrivatePlaceService")
@Transactional(readOnly = true)
@FieldDefaults(level = AccessLevel.PROTECTED)
@RequiredArgsConstructor
public class PlaceService {
    final PlaceRepository placeRepository;

    public List<PlaceShortDto> getAllPlacesByUser(String name, String placeTypes) {
        boolean isHaveName = true;
        boolean isHavePlaceTypes = true;
        if (name == null || name.isEmpty() || name.isBlank()) {
            isHaveName = false;
        }
        if (placeTypes == null || placeTypes.isEmpty() || placeTypes.isBlank()) {
            isHavePlaceTypes = false;
        }
        if (isHaveName) {
            if (isHavePlaceTypes) {
                return  placeRepository.findAllByNameAndPlaceType(name, placeTypes)
                        .stream().map(PlaceMapper::toPlaceShortDto)
                        .collect(Collectors.toList());
            } else {
                return placeRepository.findAllByName(name)
                        .stream().map(PlaceMapper::toPlaceShortDto)
                        .collect(Collectors.toList());
            }
        } else {
            if (isHavePlaceTypes) {
                return placeRepository.findAllByPlaceTypes(placeTypes)
                        .stream().map(PlaceMapper::toPlaceShortDto)
                        .collect(Collectors.toList());
            } else {
                return placeRepository.findAll()
                        .stream().map(PlaceMapper::toPlaceShortDto)
                        .collect(Collectors.toList());
            }
        }
    }

    public PlaceFullDto getPlaceByIdByUser(long placeId) {
        return PlaceMapper.toPlaceFullDto(findPlaceById(placeId));
    }

    private Place findPlaceById(long placeId) {
        return placeRepository.findById(placeId).orElseThrow(() ->
                new NotFoundException("Место с индексом " + placeId + " не найдено в базе."));
    }
}
