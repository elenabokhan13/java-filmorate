# java-filmorate
Template repository for Filmorate project.

Link to Database scheme https://github.com/elenabokhan13/java-filmorate/blob/main/Database.PNG

   - USERS contains:
      - USERS_ID - is a primary key that defines the unique integer number as a user identification, type int
      - EMAIL - user email, type verchar, should be unique
      - LOGIN - user login, type verchar
      - NAME - user name, type verchar
      - BITHDAY - user birthday, type date

  - FILMS contains:
    - FILM_ID - is a primary key, type int, used to set up a unique identifier
    - RATING_ID - a foreign key, type int, identifies the rating it of a movie, references to RATING_LIST table one-two-one
    - NAME - movie title, type varchar
    - DESCRIPTION - movie discription, no more than 200 symbols, type varchar
    - RELEASE_DATE - movie release date, type date
    - DURATION - movie duration, type int

  - RATING_LIST   contains:
    - RATING_ID - is a primary key, type int, used to set up a unique identifier for a rating
    - RATING_NAME - number of a rating

  - GENRE_LIST contains;
    - FILM_ID - foreign key, identifying the film id, type int
    - GENRE_NAME - name of a genre, type varchar. one movie can have several genres

  - FRIENDS_LIST contains:
    - USER_ID - foreign key USER_ID to identify user
    - FRIEND_ID - foreign key USER_ID to identify friend. both lines reference the same user table
    - FRIENDSHIP_STATUS - status can be confirmed and uncomfirmed, type varchar
      
  - FILMS_LIKED_LIST contains:
    - USER_ID - USER_ID foriegn key, many to many one user can like several different films
    - FILM_ID - FILM_ID foreign key, the same film can be liked by diffrenet users


