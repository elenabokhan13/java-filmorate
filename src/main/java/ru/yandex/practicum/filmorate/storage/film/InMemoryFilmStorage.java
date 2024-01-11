package ru.yandex.practicum.filmorate.storage.film;

import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.exception.ObjectNotFound;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.IdNameSet;
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
        film.setMpa(createMpaMap().get(film.getMpa().getId()));
        List<IdNameSet> response = new ArrayList<>();
        Set<Integer> idSet = new HashSet<>();
        for (IdNameSet genre : film.getGenres()) {
            int length = idSet.size();
            idSet.add(genre.getId());
            int newLength = idSet.size();
            if (length == newLength) {
                continue;
            }
            IdNameSet genreCurrent = createGenreMap().get(genre.getId());
            response.add(genreCurrent);
        }
        film.setGenres(response);

        films.put(filmId, film);
        return film;
    }

    @Override
    public Film update(Film film) {
        Film filmNew = films.get(film.getId());
        if (filmNew == null) {
            throw new ObjectNotFound("Фильм с id " + film.getId() + " не зарегистрирован");
        }
        films.remove(filmNew.getId());
        film.setMpa(createMpaMap().get(film.getMpa().getId()));
        List<IdNameSet> response = new ArrayList<>();
        Set<Integer> idSet = new HashSet<>();
        for (IdNameSet genre : film.getGenres()) {
            int length = idSet.size();
            idSet.add(genre.getId());
            int newLength = idSet.size();
            if (length == newLength) {
                continue;
            }
            IdNameSet genreCurrent = createGenreMap().get(genre.getId());
            response.add(genreCurrent);
        }
        film.setGenres(response);

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
    public Collection<IdNameSet> getAllMpa() {
        return createMpaMap().values();
    }

    @Override
    public IdNameSet findMpaById(Integer id) {
        IdNameSet response = createMpaMap().get(id);
        if (response == null) {
            throw new ObjectNotFound("Mpa с id " + id + "не найден.");
        }
        return response;
    }

    @Override
    public Collection<IdNameSet> getAllGenre() {
        return createGenreMap().values();
    }

    @Override
    public IdNameSet findGenreById(Integer id) {
        IdNameSet response = createGenreMap().get(id);
        if (response == null) {
            throw new ObjectNotFound("Жанр с id " + id + " не найден.");
        }
        return response;
    }

    private Map<Integer, IdNameSet> createMpaMap() {
        List<String> mpas = new ArrayList<>();
        mpas.add("G");
        mpas.add("PG");
        mpas.add("PG-13");
        mpas.add("R");
        mpas.add("NC-17");

        Map<Integer, IdNameSet> mpaList = new HashMap<>();
        int index = 1;
        for (String mpaNames : mpas) {
            IdNameSet mpa = IdNameSet.builder().id(index).name(mpaNames).build();
            mpaList.put(index, mpa);
            index += 1;
        }
        return mpaList;
    }

    private Map<Integer, IdNameSet> createGenreMap() {
        List<String> genres = new ArrayList<>();
        genres.add("Комедия");
        genres.add("Драма");
        genres.add("Мультфильм");
        genres.add("Триллер");
        genres.add("Документальный");
        genres.add("Боевик");
        Map<Integer, IdNameSet> genreList = new HashMap<>();
        int index = 1;
        for (String genreNames : genres) {
            IdNameSet genre = IdNameSet.builder().id(index).name(genreNames).build();
            genreList.put(index, genre);
            index += 1;
        }
        return genreList;
    }
}
