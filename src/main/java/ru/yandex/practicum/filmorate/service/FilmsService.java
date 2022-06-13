package ru.yandex.practicum.filmorate.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.dto.FilmDto;
import ru.yandex.practicum.filmorate.storage.InMemoryFilmStorage;
import ru.yandex.practicum.filmorate.storage.InMemoryUserStorage;
import ru.yandex.practicum.filmorate.storage.interfaces.FilmStorage;
import ru.yandex.practicum.filmorate.storage.interfaces.UserStorage;

import java.util.Collection;

@Service
public class FilmsService {
	private final FilmStorage storage;
	private final UserStorage usersStorage;

	@Autowired
	FilmsService(InMemoryFilmStorage storage, InMemoryUserStorage usersStorage) {
		this.storage = storage;
		this.usersStorage = usersStorage;
	}

	public Collection<Film> getAll() {
		return storage.getAll();
	}

	public Film create(FilmDto dto) {
		return storage.create(dto);
	}

	public Film update(int id, FilmDto dto) {
		return storage.update(id, dto);
	}

	public Film replace(Film film) {
		return storage.replace(film);
	}

	public Film getById(int id) {
		return storage.getById(id);
	}

	public void addLikeFromUser(int id, int userId) {
		var film = storage.getById(id);
		var user = usersStorage.getById(userId);
		film.addLike(user.getId());
		film.increaseLikesAmount();
	}

	public void removeLikeFromUser(int id, int userId) {
		var film = storage.getById(id);
		var user = usersStorage.getById(userId);
		film.removeLike(user.getId());
		film.decreaseLikesAmount();
	}

	public Collection<Film> getPopularFilms(int count) {
		return storage.getPopularFilms(count);
	}
}
