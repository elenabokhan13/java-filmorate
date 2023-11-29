package ru.yandex.practicum.filmorate.controller;

import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.exception.ValidationException;
import ru.yandex.practicum.filmorate.model.Film;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import static ru.yandex.practicum.filmorate.model.Film.FORMATTER;

@RestController
@Slf4j
public class FilmController {
    private final List<Film> films = new ArrayList<>();
    private int filmId = 0;

    @GetMapping("/films")
    public List<Film> getAll() {
        return films;
    }

    @PostMapping("/films")
    public Film create(@Valid @RequestBody Film film) throws ValidationException {
        filmValidation(film);
        filmId += 1;
        film.setId(filmId);
        films.add(film);
        log.info("Получен запрос к эндпоинту /films для добавления нового фильма");
        return film;
    }

    @PutMapping("/films")
    public Film updateOrCreate(@Valid @RequestBody Film film) throws ValidationException {
        filmValidation(film);
        Film filmNew = null;
        for (Film filmCurrent : films) {
            if (filmCurrent.getId() == film.getId()) {
                filmNew = filmCurrent;
                break;
            }
        }
        if (filmNew != null) {
            films.remove(filmNew);
            films.add(film);
        } else {
            throw new ValidationException();
        }

        log.info("Получен запрос к эндпоинту /films для обновления фильма");
        return film;
    }

    private void filmValidation(Film film) throws ValidationException {
        if (film.getDescription().length() > 200) {
            log.error("Описание фильма не может больше 200 символов");
            throw new ValidationException();
        } else if (film.getReleaseDate().isBefore(LocalDate.parse("1895-12-28", FORMATTER))) {
            log.error("Дата релиза фильма не может быть раньше 28.12.1895");
            throw new ValidationException();
        } else if (film.getDuration() < 0) {
            log.error("Длительность фильма не может быть отрицательной");
            throw new ValidationException();
        }
    }
}
