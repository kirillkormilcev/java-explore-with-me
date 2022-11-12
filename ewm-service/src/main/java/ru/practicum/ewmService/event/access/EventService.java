package ru.practicum.ewmService.event.access;

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
import ru.practicum.ewmService.error.exception.NotFoundException;
import ru.practicum.ewmService.event.EventMapper;
import ru.practicum.ewmService.event.EventRepository;
import ru.practicum.ewmService.event.category.CategoryRepository;
import ru.practicum.ewmService.event.dto.EventFullDto;
import ru.practicum.ewmService.event.dto.EventShortDto;
import ru.practicum.ewmService.event.model.Event;
import ru.practicum.ewmService.event.model.State;
import ru.practicum.ewmService.event.model.repository.EventParameters;
import ru.practicum.ewmService.event.model.repository.EventSpecification;
import ru.practicum.ewmService.request.RequestRepository;
import ru.practicum.ewmService.user.UserRepository;

import java.util.List;
import java.util.stream.Collectors;

@Service("PublicEventService")
@Transactional(readOnly = true)
public class EventService extends GlobalService {
    public EventService(MyFeignClient feignClient,
                        EventRepository eventRepository,
                        UserRepository userRepository,
                        CategoryRepository categoryRepository,
                        RequestRepository requestRepository) {
        super(feignClient, eventRepository, userRepository, categoryRepository, requestRepository);
    }

    public List<EventShortDto> getEvents(EventParameters ep) {
        EventSpecification es = new EventSpecification();
        if (ep.getText() != null) {
            es.add(new SearchCriteria("annotation", ep.getText(), SearchOperation.MATCH));
            es.add(new SearchCriteria("description", ep.getText(), SearchOperation.OR_MATCH));
        }
        if (!ep.getCategories().isEmpty() && ep.getCategories() != null) {
            es.add(new SearchCriteria("category", categoryRepository.findAllByIdIn(ep.getCategories()), SearchOperation.IN));
        }
        if (ep.getPaid() != null) {
            switch (ep.getPaid()) {
                case "true":
                    es.add(new SearchCriteria("paid", ep.getPaid(), SearchOperation.TRUE));
                    break;
                case "false":
                    es.add(new SearchCriteria("paid", ep.getPaid(), SearchOperation.FALSE));
                    break;
                default:
                    throw new IncorrectRequestParamException("Параметр paid=" + ep.getPaid() + " не верный.");
            }
        }
        setStartAndEndRangesInSpecification(ep, es);
        if (ep.getOnlyAvailable() != null) {
            switch (ep.getOnlyAvailable()) {
                case "true":
                    es.add(new SearchCriteria("available", ep.getOnlyAvailable(), SearchOperation.TRUE));
                    break;
                case "false":
                    es.add(new SearchCriteria("available", ep.getOnlyAvailable(), SearchOperation.FALSE));
                    break;
                default:
                    throw new IncorrectRequestParamException("Параметр onlyAvailable=" + ep.getOnlyAvailable() + " не верный.");
            }
        }
        PageRequest pageRequest;
        switch (ep.getSort()) {
            case "VIEWS":
                pageRequest = new PageRequestModified(ep.getFrom(), ep.getSize(), Sort.by("views").descending());
                break;
            case "EVENT_DATE":
                pageRequest = new PageRequestModified(ep.getFrom(), ep.getSize(), Sort.by("eventDate"));
                break;
            default:
                throw new IncorrectRequestParamException("Параметр sort=" + ep.getSort() + " не верный.");
        }
        es.add(new SearchCriteria("state", State.PUBLISHED, SearchOperation.EQUAL));
        return eventRepository.findAll(es, pageRequest)
                .stream().map(EventMapper::toEventShortDto).collect(Collectors.toList());
    }

    public EventFullDto getEventById(long eventId) {
        Event eventFromRepository = eventRepository.findByIdAndState(eventId, State.PUBLISHED).orElseThrow(() ->
                new NotFoundException("Событие с индексом " + eventId + " не найдено в базе."));
        updateViewsOfEvents(List.of(eventFromRepository));
        return EventMapper.toEventFullDto(eventFromRepository);
    }
}
