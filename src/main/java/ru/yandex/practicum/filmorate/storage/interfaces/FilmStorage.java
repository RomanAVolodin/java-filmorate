package ru.yandex.practicum.filmorate.storage.interfaces;

import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.dto.FilmDto;

import java.util.Collection;

public interface FilmStorage {
	Collection<Film> getAll();

	Film create(FilmDto dto);

	Film update(int id, FilmDto dto);

	Film replace(Film film);

	Film getById(int id);

	Collection<Film> getPopularFilms(int count);
}
