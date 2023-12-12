package ru.yandex.practicum.filmorate.exception;

public class FilmOrUserNotRegistered extends RuntimeException {
    public FilmOrUserNotRegistered(final String message) {
        super(message);
    }
}
