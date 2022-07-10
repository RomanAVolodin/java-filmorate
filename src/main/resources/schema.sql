CREATE TABLE IF NOT EXISTS mpas (
    id INTEGER GENERATED BY DEFAULT AS IDENTITY PRIMARY KEY,
    name VARCHAR(255) NOT NULL
);

DELETE FROM mpas WHERE 1;
INSERT INTO mpas (id, name) values (1, 'G'), (2, 'PG'), (3, 'PG-13'), (4, 'R'), (5, 'NC-17');


CREATE TABLE IF NOT EXISTS films (
    id INTEGER GENERATED BY DEFAULT AS IDENTITY PRIMARY KEY,
    name VARCHAR(255) NOT NULL,
    description TEXT,
    releaseDate DATE NOT NULL,
    duration INTEGER NOT NULL,
    likesAmount INTEGER,
    rate INTEGER,
    mpa INTEGER,
    CONSTRAINT fk_film_mpa_pa FOREIGN KEY(mpa) REFERENCES mpas(id) ON DELETE SET NULL
);


CREATE TABLE IF NOT EXISTS users (
    id INTEGER GENERATED BY DEFAULT AS IDENTITY PRIMARY KEY,
    login VARCHAR(255) NOT NULL,
    name VARCHAR(255) NOT NULL,
    birthday DATE NOT NULL,
    email VARCHAR(255) NOT NULL
);


CREATE TABLE IF NOT EXISTS genres (
    id INTEGER GENERATED BY DEFAULT AS IDENTITY PRIMARY KEY,
    name VARCHAR(255) NOT NULL
);

DELETE FROM genres WHERE 1;
INSERT INTO genres (id, name) values (1, 'Комедия'), (2, 'Драма'), (3, 'Мультфильм'), (4, 'Триллер'), (5, 'Документальный'), (6, 'Боевик');

CREATE TABLE IF NOT EXISTS film_genre (
    id INTEGER GENERATED BY DEFAULT AS IDENTITY PRIMARY KEY,
    film_id INTEGER NOT NULL,
    genre_id INTEGER NOT NULL,
    CONSTRAINT fk_film_genre_film FOREIGN KEY(film_id) REFERENCES films(id) ON DELETE CASCADE,
    CONSTRAINT fk_film_genre_genre FOREIGN KEY(genre_id) REFERENCES genres(id) ON DELETE CASCADE,
    CONSTRAINT uniq_film_genre_film_genre UNIQUE(film_id, genre_id)
);


CREATE INDEX IF NOT EXISTS idx_film_genre_film_id ON film_genre(film_id);
CREATE INDEX IF NOT EXISTS idx_film_genre_genre_id ON film_genre(genre_id);

CREATE TABLE IF NOT EXISTS likes (
      id INTEGER GENERATED BY DEFAULT AS IDENTITY PRIMARY KEY,
      user_id INTEGER NOT NULL,
      film_id INTEGER NOT NULL,
      CONSTRAINT fk_likes_user FOREIGN KEY(user_id) REFERENCES users(id) ON DELETE CASCADE,
      CONSTRAINT fk_likes_film_id FOREIGN KEY(film_id) REFERENCES films(id) ON DELETE CASCADE,
      CONSTRAINT uniq_likes_user_film UNIQUE(user_id, film_id)
);

CREATE INDEX IF NOT EXISTS idx_likes_film_id ON likes(film_id);
CREATE INDEX IF NOT EXISTS idx_likes_user_id ON likes(user_id);

CREATE TABLE IF NOT EXISTS friends (
      id INTEGER GENERATED BY DEFAULT AS IDENTITY PRIMARY KEY,
      user_id INTEGER NOT NULL,
      friend_id INTEGER NOT NULL,
      is_approved BOOLEAN DEFAULT false,
      CONSTRAINT fk_friends_user FOREIGN KEY(user_id) REFERENCES users(id) ON DELETE CASCADE,
      CONSTRAINT fk_friends_friend FOREIGN KEY(friend_id) REFERENCES users(id) ON DELETE CASCADE,
      CONSTRAINT uniq_friends_user_friend UNIQUE(user_id, friend_id)
);

CREATE INDEX IF NOT EXISTS idx_users_user_friend ON friends(user_id, friend_id);