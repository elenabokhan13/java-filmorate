package ru.yandex.practicum.filmorate.storage.film;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.exception.FilmOrUserNotRegistered;
import ru.yandex.practicum.filmorate.exception.ValidationException;
import ru.yandex.practicum.filmorate.model.Film;

import java.time.LocalDate;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

import static ru.yandex.practicum.filmorate.model.Film.FORMATTER;


@Component
@Slf4j
public class InMemoryFilmStorage implements FilmStorage {
    public static final LocalDate START_RELEASE_DATE = LocalDate.parse("1895-12-28", FORMATTER);
    private static final Map<Integer, Film> films = new HashMap<>();
    private int filmId = 0;

    @Override
    public Collection<Film> getAll() {
        return films.values();
    }

    @Override
    public Film create(Film film) {
        filmValidation(film);
        filmId += 1;
        film.setId(filmId);
        films.put(filmId, film);
        return film;
    }

    @Override
    public Film update(Film film) {
        filmValidation(film);
        Film filmNew = films.get(film.getId());
        if (filmNew == null) {
            throw new FilmOrUserNotRegistered("Данный фильм еще не добавлен в базу.");
        }
        films.remove(filmNew.getId());
        films.put(film.getId(), film);
        return film;
    }

    @Override
    public Map<Integer, Film> getFilms() {
        return films;
    }

    private void filmValidation(Film film) throws ValidationException {
        if (film.getDescription() != null) {
            if (film.getDescription().length() > 200) {
                log.error("Описание фильма не может больше 200 символов");
                throw new ValidationException("Описание фильма не может больше 200 символов");
            }
        }
        if (film.getReleaseDate() != null) {
            if (film.getReleaseDate().isBefore(START_RELEASE_DATE)) {
                log.error("Дата релиза фильма не может быть раньше 28.12.1895");
                throw new ValidationException("Дата релиза фильма не может быть раньше START_RELEASE_DATE 28.12.1895");
            }
        }
        if (film.getDuration() < 0) {
            log.error("Длительность фильма не может быть отрицательной");
            throw new ValidationException("Длительность фильма не может быть отрицательной");
        }
        if (Objects.equals(film.getName(), "")) {
            log.error("Название фильма не может быть пустым");
            throw new ValidationException("Название фильма не может быть пустым");
        }
    }
}
