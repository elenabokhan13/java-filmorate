package ru.yandex.practicum.filmorate.controller;

import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.exception.ValidationException;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.service.FilmService;
import ru.yandex.practicum.filmorate.storage.film.FilmStorage;
import ru.yandex.practicum.filmorate.storage.user.UserStorage;

import java.util.Collection;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/films")
@Validated
public class FilmController {

    private final FilmStorage filmStorage;
    private final FilmService filmService;
    private final UserStorage userStorage;

    @Autowired
    public FilmController(FilmStorage filmStorage, FilmService filmService, UserStorage userStorage) {
        this.filmStorage = filmStorage;
        this.filmService = filmService;
        this.userStorage = userStorage;
    }

    @GetMapping
    public Collection<Film> getAll() {
        return filmStorage.getAll();
    }

    @GetMapping(value = "/{id}")
    public Film findById(@PathVariable int id) {
        return filmStorage.getAll().stream()
                .filter(x -> x.getId() == id)
                .findFirst().orElseThrow(NullPointerException::new);
    }

    @GetMapping(value = "/popular")
    public List<Film> getTopPopularFilms(@RequestParam(defaultValue = "10") int count) {
        return filmStorage.getFilms().values().stream()
                .sorted(Comparator.comparing(Film::getLikes).reversed())
                .limit(count)
                .collect(Collectors.toList());
    }

    @PostMapping
    public Film create(@RequestBody @Valid Film film) throws ValidationException {
        return filmStorage.create(film);
    }

    @PutMapping
    public Film updateOrCreate(@RequestBody @Valid Film film) throws ValidationException {
        return filmStorage.updateOrCreate(film);
    }

    @PutMapping(value = "/{id}/like/{userId}")
    public void likeFilm(@PathVariable int id, @PathVariable int userId) {
        User user = userStorage.getUsers().get(userId);
        Film film = filmStorage.getFilms().get(id);
        filmService.likeFilm(user, film);
    }

    @DeleteMapping(value = "/{id}/like/{userId}")
    public void dislikeFilm(@PathVariable int id, @PathVariable int userId) {
        User user = userStorage.getUsers().get(userId);
        Film film = filmStorage.getFilms().get(id);
        filmService.dislikeFilm(user, film);
    }
}
