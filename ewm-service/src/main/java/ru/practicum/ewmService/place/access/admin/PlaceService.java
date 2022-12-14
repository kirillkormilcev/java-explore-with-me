package ru.practicum.ewmService.place.access.admin;

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.ewmService.error.exception.NotFoundException;
import ru.practicum.ewmService.event.EventRepository;
import ru.practicum.ewmService.event.model.Event;
import ru.practicum.ewmService.place.PlaceMapper;
import ru.practicum.ewmService.place.PlaceRepository;
import ru.practicum.ewmService.place.dto.PlaceFullDto;
import ru.practicum.ewmService.place.dto.PlaceNewDto;
import ru.practicum.ewmService.place.dto.PlaceUpdateDto;
import ru.practicum.ewmService.place.model.Place;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Service("AdminPlaceService")
@Transactional(readOnly = true)
@FieldDefaults(level = AccessLevel.PROTECTED)
@RequiredArgsConstructor
public class PlaceService {
    final PlaceRepository placeRepository;
    final EventRepository eventRepository;

    public List<PlaceFullDto> getAllPlacesByAdmin(String name) {
        if (name == null || name.isEmpty() || name.isBlank()) {
            return placeRepository.findAll()
                    .stream().map(PlaceMapper::toPlaceFullDto)
                    .collect(Collectors.toList());
        } else {
            return placeRepository.findAllByName(name)
                    .stream().map(PlaceMapper::toPlaceFullDto)
                    .collect(Collectors.toList());
        }
    }

    @Transactional
    public PlaceFullDto createPlaceByAdmin(PlaceNewDto placeNewDto) {

        Place placeFromRepository = placeRepository.save(PlaceMapper.toPlaceFromPlaceNewDto(placeNewDto));
        findEventsInThatPlaceAndAddToEventsPlaces(placeFromRepository);
        return PlaceMapper.toPlaceFullDto(placeFromRepository);
    }

    @Transactional
    public PlaceFullDto updatePlaceByAdmin(long placeId, PlaceUpdateDto placeUpdateDto) {
        Place placeFromRepository = findPlaceById(placeId);
        PlaceMapper.updateNotNullField(PlaceMapper.toPlaceFromPlaceUpdateDto(placeUpdateDto), placeFromRepository);
        if (placeFromRepository.getAvailable()) {
            findEventsInThatPlaceAndAddToEventsPlaces(placeFromRepository);
        } else {
            findEventsInNotAvailableOrDeletedPlaceAndRemoveFromEventsPlaces(placeFromRepository);
        }
        return PlaceMapper.toPlaceFullDto(placeRepository.save(placeFromRepository));
    }

    @Transactional
    public void deletePlaceByAdmin(long placeId) {
        Place placeFromRepository = findPlaceById(placeId);
        findEventsInNotAvailableOrDeletedPlaceAndRemoveFromEventsPlaces(placeFromRepository);
        placeRepository.deleteById(placeId);
    }

    private Place findPlaceById(long placeId) {
        return placeRepository.findById(placeId).orElseThrow(() ->
                new NotFoundException("Место с индексом " + placeId + " не найдено в базе."));
    }

    private void findEventsInThatPlaceAndAddToEventsPlaces(Place place) {
        List<Event> eventsInPlace = findEventsInPlace(
                place.getLocation().getLat(), place.getLocation().getLon(), place.getRadius());
        for (Event event: eventsInPlace) {
            Set<Place> places = event.getPlaces();
            places.add(place);
            String placeNames = "";
            for (Place placeItem: places) {
                placeNames = placeNames.concat(" ").concat(placeItem.getName());
            }
            event.setPlaces(places);
            event.setPlaceNames(placeNames);
        }
        eventRepository.saveAll(eventsInPlace);
    }

    private void findEventsInNotAvailableOrDeletedPlaceAndRemoveFromEventsPlaces(Place place) {
        List<Event> eventsInPlace = findEventsInPlace(
                place.getLocation().getLat(), place.getLocation().getLon(), place.getRadius());
        for (Event event: eventsInPlace) {
            Set<Place> places = event.getPlaces();
            places.remove(place);
            String placeNames = "";
            for (Place placeItem: places) {
                placeNames = placeNames.concat(" ").concat(placeItem.getName());
            }
            event.setPlaces(places);
            event.setPlaceNames(placeNames);
        }
        eventRepository.saveAll(eventsInPlace);
    }

    private List<Event> findEventsInPlace(float lat, float lon, float radius) {
        return eventRepository.findEventsInPlace(lat, lon, radius);
    }
}
