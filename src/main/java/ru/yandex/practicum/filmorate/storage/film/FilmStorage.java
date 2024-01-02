package ru.yandex.practicum.filmorate.storage.film;

import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.Genre;
import ru.yandex.practicum.filmorate.model.Mpa;
import ru.yandex.practicum.filmorate.model.User;

import java.util.Collection;
import java.util.Map;

public interface FilmStorage {
    Film create(Film film);

    Film update(Film film);

    Map<Integer, Film> getFilms();

    public void likeFilm(Film film, User user);

    public void dislikeFilm(Film film, User user);

    public Collection<Mpa> getAllMpa();

    public Mpa findMpaById(Integer id);

    public Collection<Genre> getAllGenre();

    public Genre findGenreById(Integer id);
}
