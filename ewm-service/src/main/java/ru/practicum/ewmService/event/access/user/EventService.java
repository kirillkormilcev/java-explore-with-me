package ru.practicum.ewmService.event.access.user;

import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.ewmService.common.GlobalService;
import ru.practicum.ewmService.common.PageRequestModified;
import ru.practicum.ewmService.common.stats.MyFeignClient;
import ru.practicum.ewmService.error.exception.IncorrectRequestParamException;
import ru.practicum.ewmService.error.exception.NotFoundException;
import ru.practicum.ewmService.error.exception.RequestValidationException;
import ru.practicum.ewmService.event.EventMapper;
import ru.practicum.ewmService.event.EventRepository;
import ru.practicum.ewmService.event.category.CategoryRepository;
import ru.practicum.ewmService.event.dto.EventFullDto;
import ru.practicum.ewmService.event.dto.EventNewDto;
import ru.practicum.ewmService.event.dto.EventShortDto;
import ru.practicum.ewmService.event.dto.EventUpdateDto;
import ru.practicum.ewmService.event.model.Event;
import ru.practicum.ewmService.event.model.State;
import ru.practicum.ewmService.request.RequestMapper;
import ru.practicum.ewmService.request.RequestRepository;
import ru.practicum.ewmService.request.dto.RequestDto;
import ru.practicum.ewmService.request.model.Request;
import ru.practicum.ewmService.user.UserRepository;
import ru.practicum.ewmService.user.model.User;

import java.util.List;
import java.util.stream.Collectors;

@Service("PrivateEventService")
@Transactional(readOnly = true)
public class EventService extends GlobalService {

    public EventService(MyFeignClient feignClient,
                        EventRepository eventRepository,
                        UserRepository userRepository,
                        CategoryRepository categoryRepository,
                        RequestRepository requestRepository) {
        super(feignClient, eventRepository, userRepository, categoryRepository, requestRepository);
    }

    public List<EventShortDto> getEventsOfInitiator(long userId, Integer from, Integer size) {
        findUserById(userId);
        List<Event> events = eventRepository
                .findByInitiatorId(userId, new PageRequestModified(from, size, Sort.by("eventDate").descending()));
        if (events.isEmpty()) {
            throw new NotFoundException("События пользователя с индексом " + userId + " не найдены в базе.");
        }
        return events.stream().map(EventMapper::toEventShortDto).collect(Collectors.toList());
    }

    @Transactional
    public EventFullDto updateEventByInitiator(long userId, EventUpdateDto updateDto) {
        findUserById(userId);
        if (updateDto == null) {
            throw new IncorrectRequestParamException("От пользователя с индексом " + userId + " поступило пустое событие.");
        }
        if (updateDto.getEventDate() != null) {
            isEventCorrectDateForCreateAndUpdate(updateDto.getEventDate());
        }
        Event eventFromRepository = findEventById(updateDto.getEventId());
        switch (eventFromRepository.getState()) {
            case PUBLISHED:
                throw new IncorrectRequestParamException("Попытка редактирования опубликованного события.");
            case CANCELED:
                eventFromRepository.setState(State.PENDING);
        }
        isUserInitiator(eventFromRepository.getInitiator().getId(), userId);
        EventMapper.updateNotNullFields(EventMapper.toEventFromEventUpdateDto(updateDto), eventFromRepository);
        if (updateDto.getCategory() != null) {
            EventMapper.setCategory(eventFromRepository, findCategoryById(updateDto.getCategory()));
        }
        return EventMapper.toEventFullDto(eventRepository.save(eventFromRepository));
    }

    @Transactional
    public EventFullDto createEventByInitiator(long userId, EventNewDto eventNewDto) {
        isEventCorrectDateForCreateAndUpdate(eventNewDto.getEventDate());
        User initiator = findUserById(userId);
        Event newEvent = EventMapper.toEventFromEventNewDto(eventNewDto);
        if (eventNewDto.getCategory() != null) {
            EventMapper.setCategory(newEvent, findCategoryById(eventNewDto.getCategory()));
        }
        EventMapper.setInitiator(newEvent, initiator);
        calculateAvailableOfEvent(newEvent);
        return EventMapper.toEventFullDto(eventRepository.save(newEvent));
    }

    public EventFullDto getEventByInitiator(long userId, long eventId) {
        findUserById(userId);
        Event eventFromRepository = findEventById(eventId);
        isUserInitiator(eventFromRepository.getInitiator().getId(), userId);
        return EventMapper.toEventFullDto(eventFromRepository);
    }

    @Transactional
    public EventFullDto cancelEventByInitiator(long userId, long eventId) {
        findUserById(userId);
        Event eventFromRepository = findEventById(eventId);
        isUserInitiator(eventFromRepository.getInitiator().getId(), userId);
        if (eventFromRepository.getState() != State.PENDING) {
            throw new IncorrectRequestParamException("Попытка отменить не модерируемое событие.");
        }
        eventFromRepository.setState(State.CANCELED);
        return EventMapper.toEventFullDto(eventFromRepository);
    }

    public List<RequestDto> getRequestsOfEventByInitiatorId(long userId, long eventId) {
        findUserById(userId);
        Event eventFromRepository = findEventById(eventId);
        isUserInitiator(eventFromRepository.getInitiator().getId(), userId);
        return requestRepository.findByEventId(eventId, Sort.by("created")).stream().map(RequestMapper::toRequestDto)
                .collect(Collectors.toList());
    }

    @Transactional
    public RequestDto confirmRequestByInitiatorOfEvent(long userId, long eventId, long reqId) {
        findUserById(userId);
        Event eventFromRepository = findEventById(eventId);
        isUserInitiator(eventFromRepository.getInitiator().getId(), userId);
        if (eventFromRepository.getParticipantLimit() == 0) {
            throw new RequestValidationException("Подтверждение заявки на событие не требуется.");
        }
        if (eventFromRepository.getConfirmedRequests() >= eventFromRepository.getParticipantLimit()) {
            List<Request> pendingRequestsOfEvent = requestRepository.findAllByEventIdAndStatus(eventId, "PENDING");
            for (Request request: pendingRequestsOfEvent) {
                request.setStatus("REJECTED");
            }
            requestRepository.saveAll(pendingRequestsOfEvent);
            throw new RequestValidationException("Достигнут лимит по подтвержденным заявкам.");
        }
        Request requestFromRepository = findRequestById(reqId);
        requestFromRepository.setStatus("CONFIRMED");
        eventFromRepository.setConfirmedRequests(eventFromRepository.getConfirmedRequests() + 1);
        calculateAvailableOfEvent(eventFromRepository);
        eventRepository.save(eventFromRepository);
        return RequestMapper.toRequestDto(requestFromRepository);
    }

    @Transactional
    public RequestDto rejectRequestByInitiatorOfEvent(long userId, long eventId, long reqId) {
        findUserById(userId);
        Event eventFromRepository = findEventById(eventId);
        isUserInitiator(eventFromRepository.getInitiator().getId(), userId);
        Request requestFromRepository = findRequestById(reqId);
        requestFromRepository.setStatus("REJECTED");
        return RequestMapper.toRequestDto(requestFromRepository);
    }
}
