package ru.yandex.practicum.filmorate.exception;

public class GenreNotFound extends RuntimeException {
    public GenreNotFound(final String message) {
        super(message);
    }
}
