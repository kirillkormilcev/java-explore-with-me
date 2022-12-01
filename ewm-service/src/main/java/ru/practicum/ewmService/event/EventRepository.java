package ru.practicum.ewmService.event;

import org.springframework.data.domain.PageRequest;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import ru.practicum.ewmService.event.model.Event;
import ru.practicum.ewmService.event.model.State;

import java.util.List;
import java.util.Optional;

@Repository
public interface EventRepository extends JpaRepository<Event, Long>, JpaSpecificationExecutor<Event> {

    List<Event> findByInitiatorId(long userId, PageRequest pageRequest);

    Optional<Event> findByIdAndState(long eventId, State published);

    List<Event> findAllByCategoryId(long categoryId);

    List<Event> findAllByIdIn(List<Long> events);

    List<Event> findAllByInitiatorIdNot(long userId);

    @Query("select e from Event e " +
            "where function('distance', e.location.lat, e.location.lon, ?1, ?2) <= ?3")
    List<Event> findEventsInPlace(float lat, float lon, float radius);

    @Query("select e from Event e " +
            "join e.places p " +
            "where p.id = ?1 " +
            "and e.state = 'PUBLISHED' " +
            "order by e.annotation")
    List<Event> findAllByPlaceId(long placeId);
}
