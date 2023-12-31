package ru.yandex.practicum.filmorate.storage.film;

import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.exception.FilmOrUserNotRegistered;
import ru.yandex.practicum.filmorate.exception.GenreNotFound;
import ru.yandex.practicum.filmorate.exception.MpaNotFound;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.Genre;
import ru.yandex.practicum.filmorate.model.Mpa;
import ru.yandex.practicum.filmorate.model.User;

import java.util.*;


@Component("inMemoryFilmStorage")
public class InMemoryFilmStorage implements FilmStorage {
    private final Map<Integer, Film> films = new HashMap<>();
    private static int filmId = 0;

    @Override
    public Film create(Film film) {
        filmId += 1;
        film.setId(filmId);
        if (film.getMpa() != null) {
            film.setMpa(createMpaMap().get(film.getMpa().getId()));
        }
        if (film.getGenres() != null) {
            List<Genre> response = new ArrayList<>();
            Set<Integer> idSet = new HashSet<>();
            for (Genre genre : film.getGenres()) {
                int length = idSet.size();
                idSet.add(genre.getId());
                int newLength = idSet.size();
                if (length == newLength) {
                    continue;
                }
                Genre genreCurrent = createGenreMap().get(genre.getId());
                response.add(genreCurrent);
            }
            film.setGenres(response);
        } else {
            film.setGenres(List.of());
        }
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
        if (film.getMpa() != null) {
            film.setMpa(createMpaMap().get(film.getMpa().getId()));
        }
        if (film.getGenres() != null) {
            List<Genre> response = new ArrayList<>();
            Set<Integer> idSet = new HashSet<>();
            for (Genre genre : film.getGenres()) {
                int length = idSet.size();
                idSet.add(genre.getId());
                int newLength = idSet.size();
                if (length == newLength) {
                    continue;
                }
                Genre genreCurrent = createGenreMap().get(genre.getId());
                response.add(genreCurrent);
            }
            film.setGenres(response);
        } else {
            film.setGenres(List.of());
        }
        films.put(film.getId(), film);
        return film;
    }

    @Override
    public Map<Integer, Film> getFilms() {
        return films;
    }

    @Override
    public void likeFilm(Film film, User user) {
        Set<Integer> currentUser = user.getFilmsLiked();
        currentUser.add(film.getId());
        user.setFilmsLiked(currentUser);
        film.setLikes(film.getLikes() + 1);
    }

    @Override
    public void dislikeFilm(Film film, User user) {
        Set<Integer> currentUser = user.getFilmsLiked();
        currentUser.remove(film.getId());
        user.setFilmsLiked(currentUser);
        film.setLikes(film.getLikes() - 1);
    }

    @Override
    public Collection<Mpa> getAllMpa() {
        return createMpaMap().values();
    }

    @Override
    public Mpa findMpaById(Integer id) {
        Mpa response = createMpaMap().get(id);
        if (response == null) {
            throw new MpaNotFound("Mpa с данным id не найден.");
        }
        return response;
    }

    @Override
    public Collection<Genre> getAllGenre() {
        return createGenreMap().values();
    }

    @Override
    public Genre findGenreById(Integer id) {
        Genre response = createGenreMap().get(id);
        if (response == null) {
            throw new GenreNotFound("Данный жанр не найден.");
        }
        return response;
    }

    private Map<Integer, Mpa> createMpaMap() {
        List<String> mpas = new ArrayList<>();
        mpas.add("G");
        mpas.add("PG");
        mpas.add("PG-13");
        mpas.add("R");
        mpas.add("NC-17");

        Map<Integer, Mpa> mpaList = new HashMap<>();
        int index = 1;
        for (String mpaNames : mpas) {
            Mpa mpa = Mpa.builder().id(index).name(mpaNames).build();
            mpaList.put(index, mpa);
            index += 1;
        }
        return mpaList;
    }

    private Map<Integer, Genre> createGenreMap() {
        List<String> genres = new ArrayList<>();
        genres.add("Комедия");
        genres.add("Драма");
        genres.add("Мультфильм");
        genres.add("Триллер");
        genres.add("Документальный");
        genres.add("Боевик");
        Map<Integer, Genre> genreList = new HashMap<>();
        int index = 1;
        for (String genreNames : genres) {
            Genre genre = Genre.builder().id(index).name(genreNames).build();
            genreList.put(index, genre);
            index += 1;
        }
        return genreList;
    }
}
