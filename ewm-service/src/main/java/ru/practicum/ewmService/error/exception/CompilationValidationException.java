package ru.practicum.ewmService.error.exception;

public class CompilationValidationException extends RuntimeException {
    public CompilationValidationException(String message) {
        super(message);
    }
}
