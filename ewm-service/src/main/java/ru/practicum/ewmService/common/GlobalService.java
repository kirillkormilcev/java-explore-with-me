package ru.practicum.ewmService.common;

import lombok.AccessLevel;
import lombok.experimental.FieldDefaults;
import ru.practicum.ewmService.common.stats.CommonStatsClient;
import ru.practicum.ewmService.common.stats.MyFeignClient;
import ru.practicum.ewmService.error.exception.IncorrectRequestParamException;
import ru.practicum.ewmService.error.exception.NotFoundException;
import ru.practicum.ewmService.error.exception.RequestValidationException;
import ru.practicum.ewmService.error.exception.UserAccessException;
import ru.practicum.ewmService.event.EventRepository;
import ru.practicum.ewmService.event.category.CategoryRepository;
import ru.practicum.ewmService.event.category.model.Category;
import ru.practicum.ewmService.event.model.Event;
import ru.practicum.ewmService.event.model.State;
import ru.practicum.ewmService.request.RequestRepository;
import ru.practicum.ewmService.request.model.Request;
import ru.practicum.ewmService.user.UserRepository;
import ru.practicum.ewmService.user.model.User;

import java.time.LocalDateTime;

@FieldDefaults(level = AccessLevel.PROTECTED)
public class GlobalService extends CommonStatsClient {
    final UserRepository userRepository;
    final CategoryRepository categoryRepository;
    final RequestRepository requestRepository;

    public GlobalService(MyFeignClient feignClient,
                         EventRepository eventRepository,
                         UserRepository userRepository,
                         CategoryRepository categoryRepository,
                         RequestRepository requestRepository) {
        super(feignClient, eventRepository);
        this.userRepository = userRepository;
        this.categoryRepository = categoryRepository;
        this.requestRepository = requestRepository;
    }

    protected Request findRequestById(long reqId) {
        return requestRepository.findById(reqId).orElseThrow(() ->
                new NotFoundException("Запрос с индексом " + reqId + " не найден в базе."));
    }

    protected User findUserById(long userId) {
        return userRepository.findById(userId).orElseThrow(() ->
                new NotFoundException("Пользователь с индексом " + userId + " не найден в базе."));
    }

    protected Event findEventById(long eventId) {
        return eventRepository.findById(eventId).orElseThrow(() ->
                new NotFoundException("Событие с индексом " + eventId + " не найдено в базе."));
    }

    protected Category findCategoryById(long categoryId) {
        return categoryRepository.findById(categoryId).orElseThrow(() ->
                new NotFoundException("Категория с индексом " + categoryId + " не найдена в базе."));
    }

    protected void isUserInitiator(long initiatorId, long userId) {
        if (initiatorId != userId) {
            throw new UserAccessException("Попытка редактирования/доступа к событию не его инициатором.");
        }
    }

    protected void isUserNotInitiator(long initiatorId, long userId) {
        if (initiatorId == userId) {
            throw new UserAccessException("Попытка создания запроса к собственному событию.");
        }
    }

    protected void isUserRequester(Request request, long userId) {
        if (request.getRequester().getId() != userId) {
            throw new UserAccessException("Попытка отменить не свой запрос.");
        }
    }

    protected void isEventCorrectDateForCreateAndUpdate(LocalDateTime eventDate) {
        if (eventDate.isBefore(LocalDateTime.now().plusHours(2))) {
            throw new IncorrectRequestParamException("Начало события должно быть не раньше чем через 2 часа.");
        }
    }

    protected void isEventCorrectDateForPublish(LocalDateTime eventDate) {
        if (eventDate.isBefore(LocalDateTime.now().plusHours(1))) {
            throw new IncorrectRequestParamException("Начало события должно быть не раньше чем через 1 час после публикации.");
        }
    }

    protected void isEventAvailable(Boolean available) {
        if (!available) {
            throw new RequestValidationException("У события исчерпан лимит запросов.");
        }
    }

    protected void isEventPublished(State state) {
        if (state != State.PUBLISHED) {
            throw new IncorrectRequestParamException("Попытка создания запроса на участие в неопубликованном событии.");
        }
    }

    protected void isEventPending(State state) {
        if (state != State.PENDING) {
            throw new IncorrectRequestParamException("Попытка опубликования события не в статусе: " + state + ".");
        }
    }

    protected void isNewRequest(long userId, long eventId) {
        if (requestRepository.findByRequesterIdAndEventId(userId, eventId).isPresent()) {
            throw new IncorrectRequestParamException("Запрос пользователя с индексом " + userId + " к событию с иднексом "
                    + eventId + " уже создан.");
        }
    }

    protected void calculateAvailableOfEvent(Event event) {
        if (!event.getRequestModeration()) {
            event.setAvailable(true);
        } else {
            event.setAvailable(event.getConfirmedRequests() < event.getParticipantLimit());
        }
    }
}
