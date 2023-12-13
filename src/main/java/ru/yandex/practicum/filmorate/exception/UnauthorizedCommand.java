package ru.yandex.practicum.filmorate.exception;

public class UnauthorizedCommand extends RuntimeException {
    public UnauthorizedCommand(final String message) {
        super(message);
    }
}

