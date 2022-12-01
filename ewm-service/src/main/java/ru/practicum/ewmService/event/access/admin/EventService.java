package ru.practicum.ewmService.event.access.admin;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.ewmService.common.GlobalService;
import ru.practicum.ewmService.common.stats.MyFeignClient;
import ru.practicum.ewmService.error.exception.IncorrectRequestParamException;
import ru.practicum.ewmService.event.EventMapper;
import ru.practicum.ewmService.event.EventRepository;
import ru.practicum.ewmService.event.category.CategoryRepository;
import ru.practicum.ewmService.event.dto.EventAdminUpdateDto;
import ru.practicum.ewmService.event.dto.EventFullDto;
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

@Service("AdminEventService")
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

    public List<EventFullDto> getEvents(EventParameters ep) {
        CriteriaBuilder cb = em.getCriteriaBuilder();
        CriteriaQuery<Event> cq = cb.createQuery(Event.class);
        Root<Event> event = cq.from(Event.class);
        List<Predicate> predicates = new ArrayList<>();
        if (!ep.getUsers().isEmpty() && ep.getUsers() != null) {
            predicates.add(cb.in(event.get("initiator").get("id")).value(ep.getUsers()));
        }
        if (ep.getStates() != null) {
            predicates.add(cb.in(event.get("state")).value(ep.getStates()));
        }
        if (!ep.getCategories().isEmpty() && ep.getCategories() != null) {
            predicates.add(cb.in(event.get("category").get("id")).value(ep.getCategories()));
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
        cq.orderBy(cb.asc(event.get("eventDate")));
        cq.where(predicates.toArray(new Predicate[0]));
        TypedQuery<Event> tq = em.createQuery(cq);
        List<Event> events = tq.setFirstResult(ep.getFrom()).setMaxResults(ep.getSize()).getResultList();
        return events.stream().map(EventMapper::toEventFullDto).collect(Collectors.toList());
    }

    @Transactional
    public EventFullDto updateEventByAdmin(long eventId, EventAdminUpdateDto updateDto) {
        if (updateDto == null) {
            throw new IncorrectRequestParamException("На обновление события с индексом " + eventId + " поступило пустое событие.");
        }
        if (updateDto.getEventDate() != null) {
            isEventCorrectDateForCreateAndUpdate(updateDto.getEventDate());
        }
        Event eventFromRepository = findEventById(eventId);
        EventMapper.updateNotNullFields(EventMapper.toEventFromEventAdminUpdateDto(updateDto), eventFromRepository);
        if (updateDto.getCategory() != null) {
            EventMapper.setCategory(eventFromRepository, findCategoryById(updateDto.getCategory()));
        }
        calculateAvailableOfEvent(eventFromRepository);
        return EventMapper.toEventFullDto(eventRepository.save(eventFromRepository));
    }

    @Transactional
    public EventFullDto publishEventByAdmin(long eventId) {
        Event eventFromRepository = findEventById(eventId);
        isEventCorrectDateForPublish(eventFromRepository.getEventDate());
        isEventPending(eventFromRepository.getState());
        eventFromRepository.setPublishedOn(LocalDateTime.now());
        eventFromRepository.setState(State.PUBLISHED);
        return EventMapper.toEventFullDto(eventRepository.save(eventFromRepository));
    }

    @Transactional
    public EventFullDto rejectEventByAdmin(long eventId) {
        Event eventFromRepository = findEventById(eventId);
        isEventPending(eventFromRepository.getState());
        eventFromRepository.setState(State.CANCELED);
        return EventMapper.toEventFullDto(eventRepository.save(eventFromRepository));
    }
}
