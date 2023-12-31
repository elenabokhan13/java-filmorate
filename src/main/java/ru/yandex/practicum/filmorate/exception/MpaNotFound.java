package ru.yandex.practicum.filmorate.exception;

public class MpaNotFound extends RuntimeException {
    public MpaNotFound(final String message) {
        super(message);
    }
}
