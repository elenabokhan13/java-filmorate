package ru.yandex.practicum.filmorate.controller;

import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.exception.ValidationException;
import ru.yandex.practicum.filmorate.model.Film;

import java.time.LocalDate;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

import static ru.yandex.practicum.filmorate.model.Film.FORMATTER;

@RestController
@RequestMapping("/films")
@Slf4j
@Validated
public class FilmController {

    public static final String START_RELEASE_DATE = "1895-12-28";
    private final Map<Integer, Film> films = new HashMap<>();
    private int filmId = 0;

    @GetMapping
    public Collection<Film> getAll() {
        return films.values();
    }

    @PostMapping
    public Film create(@RequestBody @Valid Film film) throws ValidationException {
        filmValidation(film);
        filmId += 1;
        film.setId(filmId);
        films.put(filmId, film);
        log.info("Получен запрос к эндпоинту /films для добавления нового фильма");
        return film;
    }

    @PutMapping
    public Film updateOrCreate(@RequestBody @Valid Film film) throws ValidationException {
        filmValidation(film);
        Film filmNew = films.get(film.getId());
        if (filmNew == null) {
            throw new ValidationException("Данный фильм еще не добавлен в базу.");
        }
        films.remove(filmNew.getId());
        films.put(film.getId(), film);
        log.info("Получен запрос к эндпоинту /films для обновления фильма");
        return film;
    }

    private void filmValidation(Film film) throws ValidationException {
        if (film.getDescription() != null) {
            if (film.getDescription().length() > 200) {
                log.error("Описание фильма не может больше 200 символов");
                throw new ValidationException("Описание фильма не может больше 200 символов");
            }
        }
        if (film.getReleaseDate() != null) {
            if (film.getReleaseDate().isBefore(LocalDate.parse(START_RELEASE_DATE, FORMATTER))) {
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
