package ru.practicum.ewmService.error;

import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.exception.ExceptionUtils;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.MissingServletRequestParameterException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import ru.practicum.ewmService.error.dto.ErrorResponse;
import ru.practicum.ewmService.error.exception.IncorrectRequestParamException;
import ru.practicum.ewmService.error.exception.NotFoundException;
import ru.practicum.ewmService.error.exception.RequestValidationException;
import ru.practicum.ewmService.error.exception.UserAccessException;

import javax.validation.ConstraintViolationException;
import java.time.LocalDateTime;
import java.time.format.DateTimeParseException;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

@RestControllerAdvice
@Slf4j
public class ErrorHandler {

    @ExceptionHandler
    public ResponseEntity<ErrorResponse> handleIncorrectRequestParamException(final IncorrectRequestParamException e) {
        return new ResponseEntity<>(new ErrorResponse(logAndGetErrorsFromStackTrace(e), e.getLocalizedMessage(),
                "Не корректный запрос в части параметров.", HttpStatus.BAD_REQUEST, LocalDateTime.now()),
                HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler
    public ResponseEntity<ErrorResponse> handleDateTimeParseException(final DateTimeParseException e) {
        return new ResponseEntity<>(new ErrorResponse(logAndGetErrorsFromStackTrace(e), e.getLocalizedMessage(),
                "Не корректный запрос в части дат.", HttpStatus.BAD_REQUEST, LocalDateTime.now()),
                HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler
    public ResponseEntity<ErrorResponse> handleNotFoundException(final NotFoundException e) {
        logAndGetErrorsFromStackTrace(e);
        return new ResponseEntity<>(new ErrorResponse(e.getLocalizedMessage(),
                "Значение не найдено.", HttpStatus.NOT_FOUND, LocalDateTime.now()), HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler
    public ResponseEntity<ErrorResponse> handleUserAccessException(final UserAccessException e) {
        return new ResponseEntity<>(new ErrorResponse(logAndGetErrorsFromStackTrace(e), e.getLocalizedMessage(),
                "Проблема доступа.", HttpStatus.FORBIDDEN, LocalDateTime.now()), HttpStatus.FORBIDDEN);
    }

    @ExceptionHandler
    public ResponseEntity<ErrorResponse> handleConstraintViolationException(final ConstraintViolationException e) {
        logAndGetErrorsFromStackTrace(e);
        return new ResponseEntity<>(new ErrorResponse(e.getLocalizedMessage(),
                "Ограничение целостности было нарушено.", HttpStatus.CONFLICT, LocalDateTime.now()),
                HttpStatus.CONFLICT);
    }

    @ExceptionHandler
    public ResponseEntity<ErrorResponse> handleRequestValidationException(final RequestValidationException e) {
        logAndGetErrorsFromStackTrace(e);
        return new ResponseEntity<>(new ErrorResponse(e.getLocalizedMessage(),
                "Ограничение целостности было нарушено.", HttpStatus.CONFLICT, LocalDateTime.now()),
                HttpStatus.CONFLICT);
    }

    @ExceptionHandler
    public ResponseEntity<ErrorResponse> handleDataIntegrityViolationException(final DataIntegrityViolationException e) {
        logAndGetErrorsFromStackTrace(e);
        return new ResponseEntity<>(new ErrorResponse(e.getLocalizedMessage(),
                "Ограничение целостности было нарушено.", HttpStatus.CONFLICT, LocalDateTime.now()),
                HttpStatus.CONFLICT);
    }

    @ExceptionHandler
    public ResponseEntity<ErrorResponse> handleThrowable(final Throwable e) {
        logAndGetErrorsFromStackTrace((Exception) e);
        return new ResponseEntity<>(new ErrorResponse(e.getLocalizedMessage(), "Произошла ошибка",
                HttpStatus.INTERNAL_SERVER_ERROR, LocalDateTime.now()), HttpStatus.INTERNAL_SERVER_ERROR);
    }

    @ExceptionHandler
    public ResponseEntity<ErrorResponse> handleMissingServletRequestParameterException(final MissingServletRequestParameterException e) {
        return new ResponseEntity<>(new ErrorResponse(logAndGetErrorsFromStackTrace(e), e.getLocalizedMessage(),
                "Отсутствует обязательный параметр запроса.", HttpStatus.BAD_REQUEST, LocalDateTime.now()),
                HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler
    public ResponseEntity<ErrorResponse> handleMethodArgumentNotValidException(final MethodArgumentNotValidException e) {
        return new ResponseEntity<>(new ErrorResponse(logAndGetErrorsFromStackTrace(e), e.getLocalizedMessage(),
                "Поля тела запроса не прошли проверку.", HttpStatus.BAD_REQUEST, LocalDateTime.now()),
                HttpStatus.BAD_REQUEST);
    }

    private List<String> logAndGetErrorsFromStackTrace(Exception e) {
        log.warn(e.getMessage(), e.getCause());
        //e.printStackTrace(); //todo: remove after debug
        return Arrays.stream(ExceptionUtils.getRootCauseStackTrace(e)).filter(f -> f.contains("ru.practicum"))
                .map((string) -> {
                    if (string.contains("\t")) {
                        string = string.substring(1);
                    }
                    return string;
        })
                .collect(Collectors.toList());
    }
}
