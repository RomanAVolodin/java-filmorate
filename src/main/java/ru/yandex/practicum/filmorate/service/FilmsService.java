package ru.yandex.practicum.filmorate.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.exceptions.ItemNotFoundException;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.model.dto.FilmDto;
import ru.yandex.practicum.filmorate.storage.BaseIdCountable;
import ru.yandex.practicum.filmorate.storage.InMemoryFilmStorage;
import ru.yandex.practicum.filmorate.storage.InMemoryUserStorage;
import ru.yandex.practicum.filmorate.storage.interfaces.FilmStorage;
import ru.yandex.practicum.filmorate.storage.interfaces.UserStorage;

import java.util.Collection;

@Service
public class FilmsService {
	private final BaseIdCountable idGenerator;
	private final FilmStorage storage;
	private final UserStorage usersStorage;

	@Autowired
	FilmsService(InMemoryFilmStorage storage, InMemoryUserStorage usersStorage, BaseIdCountable idGenerator) {
		this.storage = storage;
		this.usersStorage = usersStorage;
		this.idGenerator = idGenerator;
	}

	public Film getById(int id) {
		return storage.getById(id).orElseThrow(() -> new ItemNotFoundException("Film was not found by id"));
	}

	public Collection<Film> getAll() {
		return storage.getAll();
	}

	public Film create(FilmDto dto) {
		var film = new Film(
				idGenerator.getNextId(), dto.getName(), dto.getDescription(), dto.getReleaseDate(), dto.getDuration()
		);
		return storage.create(film);
	}

	public Film update(int id, FilmDto dto) {
		var film = getById(id);
		film.setName(dto.getName());
		film.setDescription(dto.getDescription());
		film.setReleaseDate(dto.getReleaseDate());
		film.setDuration(dto.getDuration());
		return film;
	}

	public Film replace(Film film) {
		var oldFilm = getById(film.getId());
		return storage.replace(film);
	}

	public void addLikeFromUser(int id, int userId) {
		var film = getById(id);
		var user = fetchUserFromStorage(userId);
		film.addLike(user.getId());
		film.increaseLikesAmount();
	}

	public void removeLikeFromUser(int id, int userId) {
		var film = getById(id);
		var user = fetchUserFromStorage(userId);
		film.removeLike(user.getId());
		film.decreaseLikesAmount();
	}

	public Collection<Film> getPopularFilms(int count) {
		return storage.getPopularFilms(count);
	}

	private User fetchUserFromStorage(int id) {
		return usersStorage.getById(id)
				.orElseThrow(() -> new ItemNotFoundException("User was not found by id"));
	}
}
