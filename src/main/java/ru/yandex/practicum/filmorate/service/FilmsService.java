package ru.yandex.practicum.filmorate.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.exceptions.ItemNotFoundException;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.Genre;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.model.dto.FilmDto;
import ru.yandex.practicum.filmorate.storage.BaseIdCountable;
import ru.yandex.practicum.filmorate.storage.interfaces.FilmStorage;
import ru.yandex.practicum.filmorate.storage.interfaces.GenreStorage;
import ru.yandex.practicum.filmorate.storage.interfaces.UserStorage;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

@Service
public class FilmsService {
	private final BaseIdCountable idGenerator;
	private final FilmStorage storage;
	private final UserStorage usersStorage;
	private final GenreStorage genresStorage;

	@Autowired
	FilmsService(@Qualifier("database") FilmStorage storage,
				 @Qualifier("database") UserStorage usersStorage,
				 @Qualifier("database") GenreStorage genresStorage,
				 BaseIdCountable idGenerator
	) {
		this.storage = storage;
		this.usersStorage = usersStorage;
		this.genresStorage = genresStorage;
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
				idGenerator.getNextId(),
				dto.getName(),
				dto.getDescription(),
				dto.getReleaseDate(),
				dto.getDuration(),
				dto.getRate()
		);
		if (dto.getMpa().get("id") != null) {
			film.setMpaId(dto.getMpa().get("id"));
		}

		List<Genre> genres = new ArrayList<>();
		if (dto.getGenres() != null) {
			for (var genreHash : dto.getGenres()) {
				var genre = genresStorage.getById(genreHash.get("id")).orElseThrow(
						() -> new ItemNotFoundException("Genre was not found by id")
				);
				genres.add(genre);
			}
			film.setGenres(genres);
		}

		return storage.create(film);
	}

	public Film update(int id, FilmDto dto) {
		dto.setId(id);
		return replace(dto);
	}

	public Film replace(FilmDto dto) {
		var oldFilm = getById(dto.getId());
		return storage.replace(dto);
	}

	public void addLikeFromUser(int id, int userId) {
		var film = getById(id);
		var user = fetchUserFromStorage(userId);
		storage.addLike(user.getId(), film.getId());
		film.increaseLikesAmount();
	}

	public void removeLikeFromUser(int id, int userId) {
		var film = getById(id);
		var user = fetchUserFromStorage(userId);
		storage.removeLike(user.getId(), film.getId());
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
