package ru.yandex.practicum.filmorate.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import ru.yandex.practicum.filmorate.model.Genre;
import ru.yandex.practicum.filmorate.service.FilmService;

import java.util.Collection;

@RestController
@RequestMapping("/genres")
@Validated
@Slf4j
public class GenreController {
    private final FilmService filmService;

    @Autowired
    public GenreController(FilmService filmService) {
        this.filmService = filmService;
    }

    @GetMapping
    public Collection<Genre> getAll() {
        log.info("Получен запрос к эндпоинту /genres для получения списка всех жанров.");
        return filmService.getAllGenre();
    }

    @GetMapping(value = "/{id}")
    public Genre findById(@PathVariable int id) {
        log.info("Получен запрос к эндпоинту /genres для получения жанра по id " + id);
        return filmService.findGenreById(id);
    }
}
