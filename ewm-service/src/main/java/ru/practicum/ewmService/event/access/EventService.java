package ru.practicum.ewmService.event.access;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.ewmService.common.GlobalService;
import ru.practicum.ewmService.common.stats.MyFeignClient;
import ru.practicum.ewmService.error.exception.IncorrectRequestParamException;
import ru.practicum.ewmService.error.exception.NotFoundException;
import ru.practicum.ewmService.event.EventMapper;
import ru.practicum.ewmService.event.EventRepository;
import ru.practicum.ewmService.event.category.CategoryRepository;
import ru.practicum.ewmService.event.dto.EventFullDto;
import ru.practicum.ewmService.event.dto.EventShortDto;
import ru.practicum.ewmService.event.model.Event;
import ru.practicum.ewmService.event.model.State;
import ru.practicum.ewmService.event.model.repository.EventParameters;
import ru.practicum.ewmService.request.RequestRepository;
import ru.practicum.ewmService.user.UserRepository;

import javax.persistence.EntityManager;
import javax.persistence.TypedQuery;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service("PublicEventService")
@Transactional(readOnly = true)
public class EventService extends GlobalService {
    private final EntityManager em;

    public EventService(MyFeignClient feignClient,
                        EventRepository eventRepository,
                        UserRepository userRepository,
                        CategoryRepository categoryRepository,
                        RequestRepository requestRepository,
                        EntityManager em) {
        super(feignClient, eventRepository, userRepository, categoryRepository, requestRepository);
        this.em = em;
    }

    public List<EventShortDto> getEvents(EventParameters ep) {
        CriteriaBuilder cb = em.getCriteriaBuilder();
        CriteriaQuery<Event> cq = cb.createQuery(Event.class);
        Root<Event> event = cq.from(Event.class);
        List<Predicate> predicates = new ArrayList<>();
        if (ep.getText() != null) {
            predicates.add(cb.or(cb.like(cb.lower(event.get("annotation")), "%" + ep.getText().toLowerCase() + "%"),
                    cb.like(cb.lower(event.get("description")), "%" + ep.getText().toLowerCase() + "%")));
        }
        if (ep.getPlace() != null) {
            predicates.add(cb.like(cb.lower(event.get("placeNames")), "%" + ep.getPlace().toLowerCase() + "%"));
        }
        if (ep.getCategories() != null && !ep.getCategories().isEmpty()) {
            predicates.add(cb.in(event.get("category").get("id")).value(ep.getCategories()));
        }
        if (ep.getPaid() != null) {
            switch (ep.getPaid()) {
                case "true":
                    predicates.add(cb.isTrue(event.get("paid")));
                    break;
                case "false":
                    predicates.add(cb.isFalse(event.get("paid")));
                    break;
                default:
                    throw new IncorrectRequestParamException("Параметр paid=" + ep.getPaid() + " не верный.");
            }
        }
        LocalDateTime rangeStart;
        if (ep.getRangeStartText() != null) {
            rangeStart = LocalDateTime.parse(ep.getRangeStartText(), DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));
        } else {
            rangeStart = LocalDateTime.now();
        }
        predicates.add(cb.greaterThanOrEqualTo(event.get("eventDate"), rangeStart));
        LocalDateTime rangeEnd;
        if (ep.getRangeEndText() != null) {
            rangeEnd = LocalDateTime.parse(ep.getRangeEndText(), DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));
        } else {
            rangeEnd = LocalDateTime.now().plusYears(100);
        }
        predicates.add(cb.lessThanOrEqualTo(event.get("eventDate"), rangeEnd));
        if (ep.getOnlyAvailable() != null) {
            switch (ep.getOnlyAvailable()) {
                case "true":
                    predicates.add(cb.isTrue(event.get("available")));
                    break;
                case "false":
                    break;
                default:
                    throw new IncorrectRequestParamException("Параметр onlyAvailable=" + ep.getOnlyAvailable() + " не верный.");
            }
        }
        predicates.add(cb.equal(event.get("state"), State.PUBLISHED));
        switch (ep.getSort()) {
            case "VIEWS":
                cq.orderBy(cb.desc(event.get("views")));
                break;
            case "EVENT_DATE":
                cq.orderBy(cb.asc(event.get("eventDate")));
                break;
            default:
                throw new IncorrectRequestParamException("Параметр sort=" + ep.getSort() + " не верный.");
        }
        cq.where(predicates.toArray(new Predicate[0]));
        TypedQuery<Event> tq = em.createQuery(cq);
        List<Event> events = tq.setFirstResult(ep.getFrom()).setMaxResults(ep.getSize()).getResultList();
        return events.stream().map(EventMapper::toEventShortDto).collect(Collectors.toList());
    }

    public EventFullDto getEventById(long eventId) {
        Event eventFromRepository = eventRepository.findByIdAndState(eventId, State.PUBLISHED).orElseThrow(() ->
                new NotFoundException("Событие с индексом " + eventId + " не найдено в базе."));
        updateViewsOfEvents(List.of(eventFromRepository));
        return EventMapper.toEventFullDto(eventFromRepository);
    }

    public List<EventShortDto> getEventsByPlaceId(long placeId) {
        return eventRepository.findAllByPlaceId(placeId)
                .stream().map(EventMapper::toEventShortDto).collect(Collectors.toList());
    }
}
