package ru.yandex.practicum.filmorate.exception;

public class ObjectNotFound extends RuntimeException {
    public ObjectNotFound(final String message) {
        super(message);
    }
}
