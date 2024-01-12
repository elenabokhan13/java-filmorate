package ru.yandex.practicum.filmorate;

import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.JdbcTest;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.test.context.jdbc.Sql;
import ru.yandex.practicum.filmorate.exception.ObjectNotFound;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.IdNameSet;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.storage.film.FilmDbStorage;
import ru.yandex.practicum.filmorate.storage.user.UserDbStorage;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Set;

import static org.assertj.core.api.Assertions.assertThat;
import static ru.yandex.practicum.filmorate.model.Film.FORMATTER;

@JdbcTest
@Sql("/schema.sql")
@RequiredArgsConstructor(onConstructor_ = @Autowired)
class FilmDbStorageTest {
    private final JdbcTemplate jdbcTemplate;
    private User newUserOne;
    private Film newFilmOne;
    private FilmDbStorage filmStorage;
    private UserDbStorage userStorage;

    @BeforeEach
    public void createUsersFilms() {
        newUserOne = User.builder()
                .id(1)
                .email("user@email.ru")
                .login("vanya123")
                .name("Ivan Petrov")
                .birthday(LocalDate.of(1990, 1, 1))
                .filmsLiked(Set.of())
                .friends(Set.of())
                .build();

        newFilmOne = Film.builder()
                .name("Movie")
                .id(1)
                .description("Movie description")
                .releaseDate(LocalDate.parse("2002-09-13", FORMATTER))
                .duration(100)
                .mpa(IdNameSet.builder().id(1).name("G").build())
                .genres(List.of(IdNameSet.builder().id(1).name("Комедия").build(), IdNameSet.builder().id(2).name("Драма").build()))
                .build();

        filmStorage = new FilmDbStorage(jdbcTemplate);
        userStorage = new UserDbStorage(jdbcTemplate);

        userStorage.create(newUserOne);
        filmStorage.create(newFilmOne);
    }

    @Test
    public void testFindFilmById() {
        Film savedFilm = filmStorage.getFilms().values().stream()
                .filter(x -> x.getId() == 1)
                .findFirst().orElseThrow(() -> new ObjectNotFound("Пользователь с таким id не " +
                        "зарегистрирован"));

        assertThat(savedFilm).isEqualTo(newFilmOne);
    }

    @Test
    public void testCreateFilm() {
        Film savedFilm = filmStorage.create(newFilmOne);
        assertThat(savedFilm).isEqualTo(newFilmOne);
    }

    @Test
    public void testUpdateFilm() {
        Film updatedFilmOne = Film.builder()
                .name("Movie updated")
                .id(1)
                .description("Movie description updated")
                .releaseDate(LocalDate.parse("2002-09-13", FORMATTER))
                .duration(100)
                .mpa(IdNameSet.builder().id(1).name("G").build())
                .genres(List.of(IdNameSet.builder().id(1).name("Комедия").build(), IdNameSet.builder().id(2).name("Драма").build()))
                .build();

        Film updatedFilmReturn = filmStorage.update(updatedFilmOne);

        assertThat(updatedFilmReturn).isEqualTo(updatedFilmOne);
    }

    @Test
    public void testGetFilms() {
        Collection<Film> filmCollection = new ArrayList<>();
        filmCollection.add(newFilmOne);
        Collection<Film> filmCollectionReturn = filmStorage.getFilms().values();

        assertThat(filmCollection.toString()).isEqualTo(filmCollectionReturn.toString());
    }

    @Test
    public void testLikeFilm() {
        filmStorage.likeFilm(newFilmOne, newUserOne);

        Film filmSaved = filmStorage.getFilms().values().stream()
                .filter(x -> x.getId() == 1)
                .findFirst().get();

        assertThat(filmSaved.getLikes()).isEqualTo(1);
    }

    @Test
    public void testDislikeFilm() {
        filmStorage.likeFilm(newFilmOne, newUserOne);
        filmStorage.dislikeFilm(newFilmOne, newUserOne);

        Film filmSaved = filmStorage.getFilms().values().stream()
                .filter(x -> x.getId() == 1)
                .findFirst().get();

        assertThat(filmSaved.getLikes()).isEqualTo(0);
    }

    @Test
    public void testGetAllMpa() {
        Collection<IdNameSet> returnMpa = filmStorage.getAllMpa();
        Collection<IdNameSet> mpas = new ArrayList<>();
        mpas.add(IdNameSet.builder().id(1).name("G").build());
        mpas.add(IdNameSet.builder().id(2).name("PG").build());
        mpas.add(IdNameSet.builder().id(3).name("PG-13").build());
        mpas.add(IdNameSet.builder().id(4).name("R").build());
        mpas.add(IdNameSet.builder().id(5).name("NC-17").build());

        assertThat(returnMpa).isEqualTo(mpas);
    }

    @Test
    public void testGetMpaById() {
        IdNameSet returnMpa = filmStorage.findMpaById(1);

        assertThat(returnMpa.getName()).isEqualTo("G");
    }

    @Test
    public void testGetAllGenre() {
        Collection<IdNameSet> returnGenre = filmStorage.getAllGenre();
        Collection<IdNameSet> genres = new ArrayList<>();
        genres.add(IdNameSet.builder().id(1).name("Комедия").build());
        genres.add(IdNameSet.builder().id(2).name("Драма").build());
        genres.add(IdNameSet.builder().id(3).name("Мультфильм").build());
        genres.add(IdNameSet.builder().id(4).name("Триллер").build());
        genres.add(IdNameSet.builder().id(5).name("Документальный").build());
        genres.add(IdNameSet.builder().id(6).name("Боевик").build());

        assertThat(returnGenre).isEqualTo(genres);
    }

    @Test
    public void testGenreMpaById() {
        IdNameSet returnGenre = filmStorage.findGenreById(1);
        assertThat(returnGenre.getName()).isEqualTo("Комедия");
    }
}
