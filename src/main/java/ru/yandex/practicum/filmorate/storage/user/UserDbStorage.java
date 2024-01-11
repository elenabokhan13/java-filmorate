package ru.yandex.practicum.filmorate.storage.user;

import org.springframework.context.annotation.Primary;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.exception.ObjectNotFound;
import ru.yandex.practicum.filmorate.model.User;

import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.*;
import java.util.stream.Collectors;

import static ru.yandex.practicum.filmorate.model.User.FRIENDSHIP;

@Component("userDbStorage")
@Primary
public class UserDbStorage implements UserStorage {
    private final JdbcTemplate jdbcTemplate;

    public UserDbStorage(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    @Override
    public User create(User user) {
        String sql = "insert into users(email, login, name, birthday) " +
                "values (?, ?, ?, ?)";

        KeyHolder keyHolder = new GeneratedKeyHolder();
        jdbcTemplate.update(connection -> {
            PreparedStatement stmt = connection.prepareStatement(sql, new String[]{"user_id"});
            stmt.setString(1, user.getEmail());
            stmt.setString(2, user.getLogin());
            stmt.setString(3, user.getName());
            stmt.setDate(4, Date.valueOf(user.getBirthday()));
            return stmt;
        }, keyHolder);

        user.setId((int) Objects.requireNonNull(keyHolder.getKey()).longValue());
        return getUsers().get(user.getId());
    }

    @Override
    public User update(User user) {
        String sql = "update users set " +
                "email = ?, login = ?, name = ?, birthday = ? " +
                "where user_id = ?";

        User userCurrent = getUsers().get(user.getId());
        if (userCurrent == null) {
            throw new ObjectNotFound("Пользователь с id " + user.getId() + " не зарегистрирован");
        }
        jdbcTemplate.update(sql,
                user.getEmail(),
                user.getLogin(),
                user.getName(),
                user.getBirthday(),
                user.getId());
        return getUsers().get(user.getId());
    }

    @Override
    public Map<Integer, User> getUsers() {
        Map<Integer, User> response = new HashMap<>();
        String sql = "select * from users";
        List<User> users = jdbcTemplate.query(sql, (rs, rowNum) -> makeUser(rs));
        for (User userCurrent : users) {
            response.put(userCurrent.getId(), userCurrent);
        }
        return response;
    }

    @Override
    public List<User> getUsersById(Set<Integer> usersList) {
        List<User> response = new ArrayList<>();
        if (usersList.isEmpty()) {
            return response;
        }
        for (Integer userId : usersList) {
            response.add(getUsers().get(userId));
        }
        return response.stream().sorted(Comparator.comparingInt(User::getId)).collect(Collectors.toList());
    }

    @Override
    public void addFriend(User user, User friend) {
        String sql = "insert into friends_list(user_id, friend_id, friendship_status) " +
                "values (?, ?, ?)";
        jdbcTemplate.update(sql, user.getId(), friend.getId(), FRIENDSHIP);
    }

    @Override
    public void deleteFriend(User user, User friend) {
        String sql = "delete from friends_list where user_id = ? and friend_id = ?";
        jdbcTemplate.update(sql, new Object[]{user.getId()}, new Object[]{friend.getId()});
    }

    private User makeUser(ResultSet rs) throws SQLException {
        Integer id = rs.getInt("user_id");
        String email = rs.getString("email");
        String login = rs.getString("login");
        String name = rs.getString("name");
        LocalDate birthday = rs.getDate("birthday").toLocalDate();
        return User.builder()
                .id(id)
                .email(email)
                .login(login)
                .name(name)
                .birthday(birthday)
                .friends(getFriends(id))
                .filmsLiked(getFilms(id))
                .build();
    }

    private Set<Integer> getFriends(Integer id) {
        String sql = "select friend_id from friends_list where user_id = ?";
        return Set.copyOf(jdbcTemplate.query(sql, new Object[]{id},
                (rs, rowNum) -> new Integer(rs.getInt("friend_id"))));
    }

    private Set<Integer> getFilms(Integer id) {
        String sql = "select film_id from films_liked_list where user_id = ?";
        return Set.copyOf(jdbcTemplate.query(sql, new Object[]{id},
                (rs, rowNum) -> new Integer(rs.getInt("film_id"))));
    }
}
