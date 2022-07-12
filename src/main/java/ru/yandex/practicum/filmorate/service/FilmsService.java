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
import ru.yandex.practicum.filmorate.storage.interfaces.MpaStorage;
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
	private final MpaStorage mpaStorage;

	@Autowired
	public FilmsService(@Qualifier("database") FilmStorage storage,
				 @Qualifier("database") UserStorage usersStorage,
				 @Qualifier("database") GenreStorage genresStorage,
				 @Qualifier("database") MpaStorage mpaStorage,
				 BaseIdCountable idGenerator
	) {
		this.storage = storage;
		this.usersStorage = usersStorage;
		this.genresStorage = genresStorage;
		this.mpaStorage = mpaStorage;
		this.idGenerator = idGenerator;
	}

	public Film getById(int id) {
		return storage.getById(id).orElseThrow(() -> new ItemNotFoundException("Film was not found by id: " + id));
	}

	public Collection<Film> getAll() {
		return storage.getAll();
	}

	public Film create(FilmDto dto) {
		Film film = buildFilmFromDto(dto);

		return storage.create(film);
	}

	public Film update(int id, FilmDto dto) {
		dto.setId(id);
		return replace(dto);
	}

	public Film replace(FilmDto dto) {
		var oldFilm = getById(dto.getId());
		var film = buildFilmFromDto(dto);
		return storage.replace(film);
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

	private Film buildFilmFromDto(FilmDto dto) {
		var film = Film.builder()
				.id(dto.getId())
				.name(dto.getName())
				.description(dto.getDescription())
				.releaseDate(dto.getReleaseDate())
				.duration(dto.getDuration())
				.rate(dto.getRate())
				.mpaId(dto.getMpa().get("id"))
				.build();

		List<Genre> genres = new ArrayList<>();
		if (dto.getGenres() != null) {
			for (var genreHash : dto.getGenres()) {
				var genre = genresStorage.getById(genreHash.get("id")).orElseThrow(
						() -> new ItemNotFoundException("Genre was not found by id")
				);
				if (!genres.stream().anyMatch(g -> g.getId().equals(genreHash.get("id")))) {
					genres.add(genre);
				}

			}
			film.setGenres(genres);
		}
		if (dto.getMpa() != null) {
			var mpa = mpaStorage.getById(dto.getMpa().get("id")).orElseThrow(
					() -> new ItemNotFoundException("Mpa was not found by id")
			);
			film.setMpa(mpa);
		}
		return film;
	}
}
