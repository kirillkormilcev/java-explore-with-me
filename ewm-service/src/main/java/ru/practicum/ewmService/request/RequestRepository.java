package ru.practicum.ewmService.request;

import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import ru.practicum.ewmService.request.model.Request;

import java.util.List;
import java.util.Optional;

@Repository
public interface RequestRepository extends JpaRepository<Request, Long> {
    List<Request> findByEventId(long eventId, Sort sort);

    @Query("select r from Request r " +
            "where r.requester.id = ?1 " +
            "and r.event.id in ?2")
    List<Request> findRequestsByUserInAnotherUsersEvents(long userId, List<Long> eventIds);

    Optional<Request> findByRequesterIdAndEventId(long userId, long eventId);

    List<Request> findAllByEventIdAndStatus(long eventId, String pending);
}
