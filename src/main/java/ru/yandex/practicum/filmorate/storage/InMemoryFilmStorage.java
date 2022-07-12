package ru.yandex.practicum.filmorate.storage;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.storage.interfaces.FilmStorage;

import java.util.*;
import java.util.stream.Collectors;

@Component
@Qualifier("memory")
@Slf4j
public class InMemoryFilmStorage implements FilmStorage {
	private final HashMap<Integer, Film> films = new HashMap<>();

	public Collection<Film> getAll() {
		return films.values();
	}

	public Film create(Film film) {
		films.put(film.getId(), film);
		return film;
	}

	public Optional<Film> getById(int id) {
		return Optional.ofNullable(films.get(id));
	}

	@Override
	public List<Film> getPopularFilms(int count) {
		return getAll().stream()
				.sorted(Comparator.comparing(Film::getLikesAmount).reversed())
				.limit(count)
				.collect(Collectors.toList());
	}

	@Override
	public void addLike(int userId, int filmId) {
		var film = films.get(filmId);
		film.addLike(userId);
	}

	@Override
	public void removeLike(int userId, int filmId) {
		var film = films.get(filmId);
		film.removeLike(userId);
	}

	public Film replace(Film dto) {
		var film = Film.builder()
				.id(dto.getId())
				.name(dto.getName())
				.description(dto.getDescription())
				.releaseDate(dto.getReleaseDate())
				.duration(dto.getDuration())
				.rate(dto.getRate())
				.build();
		films.put(dto.getId(), film);
		return film;
	}
}
