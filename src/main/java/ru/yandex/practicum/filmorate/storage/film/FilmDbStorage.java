package ru.yandex.practicum.filmorate.storage.film;

import org.springframework.context.annotation.Primary;
import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.exception.ObjectNotFound;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.IdNameSet;
import ru.yandex.practicum.filmorate.model.User;

import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.*;
import java.util.stream.Collectors;

@Component("filmDbStorage")
@Primary
public class FilmDbStorage implements FilmStorage {
    private final JdbcTemplate jdbcTemplate;

    public FilmDbStorage(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    @Override
    public Film create(Film film) {
        String sql = "insert into films(rating_id, name, description, release_date, duration) " +
                "values (?, ?, ?, ?, ?)";

        KeyHolder keyHolder = new GeneratedKeyHolder();
        jdbcTemplate.update(connection -> {
            PreparedStatement stmt = connection.prepareStatement(sql, new String[]{"film_id"});
            stmt.setString(2, film.getName());
            stmt.setString(3, film.getDescription());
            stmt.setDate(4, Date.valueOf(film.getReleaseDate()));
            stmt.setInt(5, film.getDuration());
            stmt.setInt(1, film.getMpa().getId());

            return stmt;
        }, keyHolder);

        film.setId((int) Objects.requireNonNull(keyHolder.getKey()).longValue());

        for (IdNameSet genre : film.getGenres()) {
            String sqlNew = "insert into genre_film set " +
                    "film_id = ?, genre_id = ?";
            try {
                jdbcTemplate.update(sqlNew,
                        film.getId(),
                        genre.getId());
            } catch (DataAccessException e) {
                continue;
            }
        }
        return getFilms().get(film.getId());
    }

    @Override
    public Film update(Film film) {
        Film filmCurrent = getFilms().get(film.getId());
        if (filmCurrent == null) {
            throw new ObjectNotFound("Фильм с id " + film.getId() + " не зарегистрирован");
        }

        String sql = "update films set " +
                "rating_id = ?, name = ?, description = ?, release_date = ?, duration = ?" +
                "where film_id = ?";

        jdbcTemplate.update(sql,
                film.getMpa().getId(),
                film.getName(),
                film.getDescription(),
                film.getReleaseDate(),
                film.getDuration(),
                film.getId());

        String sqlDelete = "delete from genre_film where film_id = ?";
        jdbcTemplate.update(sqlDelete, film.getId());

        for (IdNameSet genre : film.getGenres()) {
            String sqlNew = "insert into genre_film set " +
                    "film_id = ?, genre_id = ?";
            try {
                jdbcTemplate.update(sqlNew,
                        film.getId(),
                        genre.getId());
            } catch (DataAccessException e) {
                continue;
            }
        }
        return getFilms().get(film.getId());
    }

    @Override
    public Map<Integer, Film> getFilms() {
        Map<Integer, Film> response = new HashMap<>();
        String sql = "select * from films";
        List<Film> films = jdbcTemplate.query(sql, (rs, rowNum) -> makeFilm(rs));
        for (Film filmCurrent : films) {
            response.put(filmCurrent.getId(), filmCurrent);
        }
        return response;
    }

    @Override
    public void likeFilm(Film film, User user) {
        String sql = "insert into films_liked_list(user_id, film_id) " +
                "values (?, ?)";
        jdbcTemplate.update(sql, user.getId(), film.getId());
    }

    @Override
    public void dislikeFilm(Film film, User user) {
        String sql = "delete from films_liked_list where user_id = ? and film_id = ? ";
        jdbcTemplate.update(sql, new Object[]{user.getId()}, new Object[]{film.getId()});
    }

    @Override
    public Collection<IdNameSet> getAllMpa() {
        String sql = "select * from rating_list";
        return jdbcTemplate.query(sql, (rs, rowNum) -> getMpa(rs.getInt("rating_id"))).stream()
                .sorted(Comparator.comparingInt(IdNameSet::getId)).collect(Collectors.toList());
    }

    @Override
    public IdNameSet findMpaById(Integer id) {
        return getMpa(id);
    }

    @Override
    public Collection<IdNameSet> getAllGenre() {
        String sql = "select * from genre_list";
        return jdbcTemplate.query(sql, (rs, rowNum) -> getGenre(rs.getInt("genre_id"))).stream()
                .sorted(Comparator.comparingInt(IdNameSet::getId)).collect(Collectors.toList());
    }

    @Override
    public IdNameSet findGenreById(Integer id) {
        return getGenre(id);
    }

    private Film makeFilm(ResultSet rs) throws SQLException {
        Integer id = rs.getInt("film_id");
        Integer ratingId = rs.getInt("rating_id");
        Integer duration = rs.getInt("duration");
        String description = rs.getString("description");
        String name = rs.getString("name");
        LocalDate releaseDate = rs.getDate("release_date").toLocalDate();

        return Film.builder()
                .id(id)
                .name(name)
                .releaseDate(releaseDate)
                .description(description)
                .duration(duration)
                .likes(getLikes(id))
                .mpa(getMpa(ratingId))
                .genres(getGenres(id))
                .build();
    }

    private IdNameSet getMpa(Integer ratingNumber) {
        String sql = "select rating_name from rating_list where rating_id = ?";
        try {
            String ratingName = jdbcTemplate.queryForObject(sql, new Object[]{ratingNumber}, String.class);
            return IdNameSet.builder().id(ratingNumber).name(ratingName).build();
        } catch (Exception e) {
            throw new ObjectNotFound("Mpa с id " + ratingNumber + "не найден.");
        }
    }

    private IdNameSet getGenre(Integer id) {
        try {
            String sqlNew = "select genre_name from genre_list where genre_id = ?";
            String name = jdbcTemplate.queryForObject(sqlNew, new Object[]{id}, String.class);
            return IdNameSet.builder().id(id).name(name).build();
        } catch (Exception e) {
            throw new ObjectNotFound("Жанр с id " + id + " не найден.");
        }
    }

    private List<IdNameSet> getGenres(Integer id) {
        String sql = "select genre_id from genre_film where film_id = ?";
        List<Integer> response = jdbcTemplate.query(sql, new Object[]{id},
                (rs, rowNum) -> new Integer(rs.getInt("genre_id")));
        Collections.sort(response);
        List<IdNameSet> genres = new ArrayList<>();
        for (Integer number : response) {
            genres.add(getGenre(number));
        }
        return genres;
    }

    private Integer getLikes(Integer id) {
        String sql = "select count(user_id) from films_liked_list where film_id = ?";
        return jdbcTemplate.queryForObject(sql, new Object[]{id}, Integer.class);
    }
}
