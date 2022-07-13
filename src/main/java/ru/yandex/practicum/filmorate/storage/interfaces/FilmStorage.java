package ru.yandex.practicum.filmorate.storage.interfaces;

import ru.yandex.practicum.filmorate.model.Film;

import java.util.Collection;
import java.util.Optional;

public interface FilmStorage {
	Collection<Film> getAll();

	Film create(Film film);

	Film replace(Film film);

	Optional<Film> getById(int id);

	Collection<Film> getPopularFilms(int count);

	void addLike(int userId, int filmId);

	void removeLike(int userId, int filmId);
}
