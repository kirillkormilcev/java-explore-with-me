package ru.practicum.ewmService.event;

import org.springframework.data.domain.PageRequest;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
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
}
