package ru.yandex.practicum.filmorate.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.User;

import java.util.Set;

@Service
@Slf4j
public class FilmService {
    public void likeFilm(User user, Film film) {
        if ((user != null) && (film != null)) {
            if (user.getFilmsLiked().contains((long) film.getId())) {
                throw new RuntimeException("Пользователь уже залайкал данный фильм. Пользователь может поставить фильму " +
                        "только один лайк.");
            } else {
                Set<Long> currentUser = user.getFilmsLiked();
                currentUser.add((long) film.getId());
                user.setFilmsLiked(currentUser);
                film.setLikes(film.getLikes() + 1);
                log.info("Получен запрос к эндпоинту /films для добавления любимого фильма");
            }
        } else {
            throw new NullPointerException();
        }
    }

    public void dislikeFilm(User user, Film film) {
        if ((user != null) && (film != null)) {
            if (!user.getFilmsLiked().contains((long) film.getId())) {
                throw new RuntimeException("Пользователь еще не залайкал данный фильм.");
            } else {
                Set<Long> currentUser = user.getFilmsLiked();
                currentUser.remove((long) film.getId());
                user.setFilmsLiked(currentUser);
                film.setLikes(film.getLikes() - 1);
                log.info("Получен запрос к эндпоинту /films для удаления любимого фильма");
            }
        } else {
            throw new NullPointerException();
        }
    }
}
