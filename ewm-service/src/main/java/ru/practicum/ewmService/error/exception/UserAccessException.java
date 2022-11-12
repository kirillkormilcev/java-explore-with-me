package ru.practicum.ewmService.error.exception;

public class UserAccessException extends RuntimeException {
    public UserAccessException(final String message) {
        super(message);
    }
}
