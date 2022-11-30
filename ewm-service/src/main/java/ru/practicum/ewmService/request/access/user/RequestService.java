package ru.practicum.ewmService.request.access.user;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.ewmService.common.GlobalService;
import ru.practicum.ewmService.common.stats.MyFeignClient;
import ru.practicum.ewmService.event.EventRepository;
import ru.practicum.ewmService.event.category.CategoryRepository;
import ru.practicum.ewmService.event.model.Event;
import ru.practicum.ewmService.request.RequestMapper;
import ru.practicum.ewmService.request.RequestRepository;
import ru.practicum.ewmService.request.dto.RequestDto;
import ru.practicum.ewmService.request.model.Request;
import ru.practicum.ewmService.user.UserRepository;
import ru.practicum.ewmService.user.model.User;

import java.util.List;
import java.util.stream.Collectors;

@Service("PrivateRequestService")
@Transactional(readOnly = true)
public class RequestService extends GlobalService {
    public RequestService(MyFeignClient feignClient,
                          EventRepository eventRepository,
                          UserRepository userRepository,
                          CategoryRepository categoryRepository,
                          RequestRepository requestRepository) {
        super(feignClient, eventRepository, userRepository, categoryRepository, requestRepository);
    }

    public List<RequestDto> getRequestsByUserInAnotherEvents(long userId) {
        findUserById(userId);
        List<Long> anotherUserEventsIds = eventRepository.findAllByInitiatorIdNot(userId)
                .stream().map(Event::getId).collect(Collectors.toList());
        return requestRepository.findRequestsByUserInAnotherUsersEvents(userId, anotherUserEventsIds)
                .stream().map(RequestMapper::toRequestDto).collect(Collectors.toList());
    }

    @Transactional
    public RequestDto createRequestByUser(long userId, long eventId) {
        User userFromRepository = findUserById(userId);
        isNewRequest(userId, eventId);
        Event eventFromRepository = findEventById(eventId);
        isUserNotInitiator(eventFromRepository.getInitiator().getId(), userId);
        isEventPublished(eventFromRepository.getState());
        isEventAvailable(eventFromRepository.getAvailable());
        Request newRequest = Request.builder()
                .event(eventFromRepository)
                .requester(userFromRepository)
                .status("PENDING")
                .build();
        if (!eventFromRepository.getRequestModeration()) {
            newRequest.setStatus("CONFIRMED");
        }
        return RequestMapper.toRequestDto(requestRepository.save(newRequest));
    }

    public RequestDto cancelOwnRequestByUser(long userId, long requestId) {
        findUserById(userId);
        Request requestFromRepository = findRequestById(requestId);
        isUserRequester(requestFromRepository, userId);
        requestFromRepository.setStatus("CANCELED");
        return RequestMapper.toRequestDto(requestRepository.save(requestFromRepository));
    }
}
