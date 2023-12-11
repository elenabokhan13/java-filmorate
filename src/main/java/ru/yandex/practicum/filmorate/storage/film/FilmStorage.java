package ru.yandex.practicum.filmorate.storage.film;

import ru.yandex.practicum.filmorate.model.Film;

import java.util.Collection;
import java.util.Map;

public interface FilmStorage {

    Collection<Film> getAll();

    Film create(Film film);

    Film updateOrCreate(Film film);

    Map<Integer, Film> getFilms();
}
