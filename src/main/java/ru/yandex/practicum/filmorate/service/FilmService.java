package ru.yandex.practicum.filmorate.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.exception.FilmOrUserNotRegistered;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.storage.film.FilmStorage;
import ru.yandex.practicum.filmorate.storage.user.UserStorage;

import java.util.Collection;
import java.util.Comparator;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Service
public class FilmService {
    private final FilmStorage filmStorage;
    private final UserStorage userStorage;

    @Autowired
    public FilmService(FilmStorage filmStorage, UserStorage userStorage) {
        this.filmStorage = filmStorage;
        this.userStorage = userStorage;
    }

    public void likeFilm(int id, int userId) {
        User user = userStorage.getUsers().get(userId);
        Film film = filmStorage.getFilms().get(id);
        if ((user == null) || (film == null)) {
            throw new FilmOrUserNotRegistered("Получен незарегистрированный фильм или пользователь");
        }
        if (user.getFilmsLiked().contains((long) film.getId())) {
            throw new RuntimeException("Пользователь уже залайкал данный фильм. Пользователь может поставить фильму " +
                    "только один лайк.");
        }
        Set<Long> currentUser = user.getFilmsLiked();
        currentUser.add((long) film.getId());
        user.setFilmsLiked(currentUser);
        film.setLikes(film.getLikes() + 1);

    }

    public void dislikeFilm(int id, int userId) {
        User user = userStorage.getUsers().get(userId);
        Film film = filmStorage.getFilms().get(id);
        if ((user == null) || (film == null)) {
            throw new FilmOrUserNotRegistered("Получен незарегистрированный фильм или пользователь");
        }
        if (!user.getFilmsLiked().contains((long) film.getId())) {
            throw new RuntimeException("Пользователь еще не залайкал данный фильм.");
        }
        Set<Long> currentUser = user.getFilmsLiked();
        currentUser.remove((long) film.getId());
        user.setFilmsLiked(currentUser);
        film.setLikes(film.getLikes() - 1);
    }

    public Collection<Film> getAll() {
        return filmStorage.getAll();
    }

    public List<Film> getTopPopularFilms(int count) {
        return filmStorage.getFilms().values().stream()
                .sorted(Comparator.comparing(Film::getLikes).reversed())
                .limit(count)
                .collect(Collectors.toList());
    }

    public Film findById(int id) {
        return filmStorage.getAll().stream()
                .filter(x -> x.getId() == id)
                .findFirst().orElseThrow(NullPointerException::new);
    }

    public Film create(Film film) {
        return filmStorage.create(film);
    }

    public Film update(Film film) {
        return filmStorage.update(film);
    }
}
