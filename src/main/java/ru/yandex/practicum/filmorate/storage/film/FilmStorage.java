package ru.yandex.practicum.filmorate.storage.film;

import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.IdNameSet;
import ru.yandex.practicum.filmorate.model.User;

import java.util.Collection;
import java.util.Map;

public interface FilmStorage {
    Film create(Film film);

    Film update(Film film);

    Map<Integer, Film> getFilms();

    public void likeFilm(Film film, User user);

    public void dislikeFilm(Film film, User user);

    public Collection<IdNameSet> getAllMpa();

    public IdNameSet findMpaById(Integer id);

    public Collection<IdNameSet> getAllGenre();

    public IdNameSet findGenreById(Integer id);
}
