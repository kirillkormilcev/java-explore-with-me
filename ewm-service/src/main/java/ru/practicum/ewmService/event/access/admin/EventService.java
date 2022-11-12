package ru.practicum.ewmService.event.access.admin;

import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.ewmService.common.GlobalService;
import ru.practicum.ewmService.common.PageRequestModified;
import ru.practicum.ewmService.common.model.specification.SearchCriteria;
import ru.practicum.ewmService.common.model.specification.SearchOperation;
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
import ru.practicum.ewmService.event.model.repository.EventSpecification;
import ru.practicum.ewmService.request.RequestRepository;
import ru.practicum.ewmService.user.UserRepository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service("AdminEventService")
@Transactional(readOnly = true)
public class EventService extends GlobalService {

    public EventService(MyFeignClient feignClient,
                        EventRepository eventRepository,
                        UserRepository userRepository,
                        CategoryRepository categoryRepository,
                        RequestRepository requestRepository) {
        super(feignClient, eventRepository, userRepository, categoryRepository, requestRepository);
    }

    public List<EventFullDto> getEvents(EventParameters ep) {
        EventSpecification es = new EventSpecification();
        if (!ep.getUsers().isEmpty() && ep.getUsers() != null) {
            es.add(new SearchCriteria("initiator", userRepository.findAllByIdIn(ep.getUsers()), SearchOperation.IN));
        }
        if (ep.getStates() != null) {
            es.add(new SearchCriteria("state", ep.getStates(), SearchOperation.IN));
        }
        if (!ep.getCategories().isEmpty() && ep.getCategories() != null) {
            es.add(new SearchCriteria("category", categoryRepository.findAllByIdIn(ep.getCategories()), SearchOperation.IN));
        }
        setStartAndEndRangesInSpecification(ep, es);
        PageRequest pageRequest = new PageRequestModified(ep.getFrom(), ep.getSize(), Sort.by("eventDate"));
        return eventRepository.findAll(es, pageRequest)
                .stream().map(EventMapper::toEventFullDto).collect(Collectors.toList());
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
