package ru.yandex.practicum.filmorate.storage.film;

import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.exception.FilmOrUserNotRegistered;
import ru.yandex.practicum.filmorate.model.Film;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;


@Component
public class InMemoryFilmStorage implements FilmStorage {
    private final Map<Integer, Film> films = new HashMap<>();
    private static int filmId = 0;

    @Override
    public Collection<Film> getAll() {
        return films.values();
    }

    @Override
    public Film create(Film film) {
        filmId += 1;
        film.setId(filmId);
        films.put(filmId, film);
        return film;
    }

    @Override
    public Film update(Film film) {
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
}
