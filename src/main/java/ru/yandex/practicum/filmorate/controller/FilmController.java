package ru.yandex.practicum.filmorate.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.exception.ValidationException;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.service.FilmService;

import javax.validation.Valid;
import java.util.Collection;
import java.util.List;

@RestController
@RequestMapping("/films")
@Validated
@Slf4j
public class FilmController {
    private final FilmService filmService;

    @Autowired
    public FilmController(FilmService filmService) {
        this.filmService = filmService;
    }

    @GetMapping
    public Collection<Film> getAll() {
        log.info("Получен запрос к эндпоинту /films для получения списка всех фильмов.");
        return filmService.getAll();
    }

    @GetMapping(value = "/{id}")
    public Film findById(@PathVariable int id) {
        log.info("Получен запрос к эндпоинту /films для получения фильма по id " + id);
        return filmService.findById(id);
    }

    @GetMapping(value = "/popular")
    public List<Film> getTopPopularFilms(@RequestParam(defaultValue = "10") int count) {
        log.info("Получен запрос к эндпоинту /films для получения списка самых популярных " + count + " фильмов.");
        return filmService.getTopPopularFilms(count);
    }

    @PostMapping
    public Film create(@RequestBody @Valid Film film) throws ValidationException {
        log.info("Получен запрос к эндпоинту /films для добавления нового фильма");
        return filmService.create(film);
    }

    @PutMapping
    public Film update(@RequestBody @Valid Film film) throws ValidationException {
        log.info("Получен запрос к эндпоинту /films для обновления фильма");
        return filmService.update(film);
    }

    @PutMapping(value = "/{id}/like/{userId}")
    public void likeFilm(@PathVariable int id, @PathVariable int userId) {
        log.info("Получен запрос к эндпоинту /films для добавления любимого фильма");
        filmService.likeFilm(id, userId);
    }

    @DeleteMapping(value = "/{id}/like/{userId}")
    public void dislikeFilm(@PathVariable int id, @PathVariable int userId) {
        log.info("Получен запрос к эндпоинту /films для удаления любимого фильма");
        filmService.dislikeFilm(id, userId);
    }
}
