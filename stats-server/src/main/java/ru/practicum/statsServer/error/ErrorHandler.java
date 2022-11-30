package ru.practicum.statsServer.error;

import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.exception.ExceptionUtils;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import ru.practicum.statsServer.error.dto.ErrorResponse;

import java.time.LocalDateTime;
import java.time.format.DateTimeParseException;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

@RestControllerAdvice
@Slf4j
public class ErrorHandler {

    @ExceptionHandler
    public ResponseEntity<ErrorResponse> handleDateTimeParseException(final DateTimeParseException e) {
        return new ResponseEntity<>(new ErrorResponse(logAndGetErrorsFromStackTrace(e), e.getLocalizedMessage(),
                "Не корректный запрос в части дат.", HttpStatus.BAD_REQUEST, LocalDateTime.now()),
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
