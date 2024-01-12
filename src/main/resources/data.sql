INSERT INTO rating_list (rating_name)
SELECT ('G')
WHERE
    NOT EXISTS (
    SELECT rating_name FROM rating_list WHERE rating_name = 'G'
    );

INSERT INTO rating_list (rating_name)
SELECT ('PG')
WHERE
    NOT EXISTS (
    SELECT rating_name FROM rating_list WHERE rating_name = 'PG'
    );

INSERT INTO rating_list (rating_name)
SELECT ('PG-13')
WHERE
    NOT EXISTS (
    SELECT rating_name FROM rating_list WHERE rating_name = 'PG-13'
    );

INSERT INTO rating_list (rating_name)
SELECT ('R')
WHERE
    NOT EXISTS (
    SELECT rating_name FROM rating_list WHERE rating_name = 'R'
    );

INSERT INTO rating_list (rating_name)
SELECT ('NC-17')
WHERE
    NOT EXISTS (
    SELECT rating_name FROM rating_list WHERE rating_name = 'NC-17'
    );

INSERT INTO genre_list (genre_name)
SELECT ('Комедия')
WHERE
    NOT EXISTS (
    SELECT genre_name FROM genre_list WHERE genre_name = 'Комедия'
    );

INSERT INTO genre_list (genre_name)
SELECT ('Драма')
WHERE
    NOT EXISTS (
    SELECT genre_name FROM genre_list WHERE genre_name = 'Драма'
    );

INSERT INTO genre_list (genre_name)
SELECT ('Мультфильм')
WHERE
    NOT EXISTS (
    SELECT genre_name FROM genre_list WHERE genre_name = 'Мультфильм'
    );

INSERT INTO genre_list (genre_name)
SELECT ('Триллер')
WHERE
    NOT EXISTS (
    SELECT genre_name FROM genre_list WHERE genre_name = 'Триллер'
    );

INSERT INTO genre_list (genre_name)
SELECT ('Документальный')
WHERE
    NOT EXISTS (
    SELECT genre_name FROM genre_list WHERE genre_name = 'Документальный'
    );

INSERT INTO genre_list (genre_name)
SELECT ('Боевик')
WHERE
    NOT EXISTS (
    SELECT genre_name FROM genre_list WHERE genre_name = 'Боевик'
    );
