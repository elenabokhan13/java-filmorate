package ru.yandex.practicum.filmorate.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import ru.yandex.practicum.filmorate.model.Mpa;
import ru.yandex.practicum.filmorate.service.FilmService;

import java.util.Collection;

@RestController
@RequestMapping("/mpa")
@Validated
@Slf4j
public class MpaController {
    private final FilmService filmService;

    @Autowired
    public MpaController(FilmService filmService) {
        this.filmService = filmService;
    }

    @GetMapping
    public Collection<Mpa> getAll() {
        log.info("Получен запрос к эндпоинту /mpa для получения списка всех возрастных рейтингов.");
        return filmService.getAllMpa();
    }

    @GetMapping(value = "/{id}")
    public Mpa findById(@PathVariable int id) {
        log.info("Получен запрос к эндпоинту /mpa для получения рейтинга по id " + id);
        return filmService.findMpaById(id);
    }
}
