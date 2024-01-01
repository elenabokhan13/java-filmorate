CREATE TABLE IF NOT EXISTS users (
    user_id INTEGER GENERATED BY DEFAULT AS IDENTITY PRIMARY KEY,
    name varchar(50),
    login varchar(25),
    email varchar(50),
    birthday date
);

CREATE TABLE IF NOT EXISTS rating_list (
    rating_id INTEGER GENERATED BY DEFAULT AS IDENTITY PRIMARY KEY,
    rating_name varchar(5) unique
);

CREATE TABLE IF NOT EXISTS genre_list (
    genre_id INTEGER GENERATED BY DEFAULT AS IDENTITY PRIMARY KEY,
    genre_name varchar(14) unique
);

CREATE TABLE IF NOT EXISTS films (
    film_id INTEGER GENERATED BY DEFAULT AS IDENTITY PRIMARY KEY,
    name varchar(50) unique,
    rating_id INTEGER REFERENCES rating_list(rating_id) ON DELETE CASCADE,
    description varchar(200),
    release_date date,
    duration integer
);

CREATE TABLE IF NOT EXISTS friends_list (
    user_id INTEGER REFERENCES users(user_id) ON DELETE CASCADE,
    friend_id INTEGER REFERENCES users(user_id) ON DELETE CASCADE,
    friendship_status varchar(11)
);

CREATE TABLE IF NOT EXISTS genre_film (
    film_id INTEGER REFERENCES films(film_id) ON DELETE CASCADE,
    genre_id INTEGER REFERENCES genre_list(genre_id) ON DELETE CASCADE
);

CREATE TABLE IF NOT EXISTS films_liked_list (
    user_id INTEGER REFERENCES users(user_id) ON DELETE CASCADE,
    film_id INTEGER REFERENCES films(film_id) ON DELETE CASCADE
);


