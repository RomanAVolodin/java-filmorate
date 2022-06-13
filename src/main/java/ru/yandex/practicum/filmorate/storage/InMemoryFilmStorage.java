package ru.yandex.practicum.filmorate.storage;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.exceptions.ItemNotFoundException;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.dto.FilmDto;
import ru.yandex.practicum.filmorate.storage.interfaces.FilmStorage;

import java.util.Collection;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.stream.Collectors;

@Component
@Slf4j
public class InMemoryFilmStorage extends BaseIdCountable implements FilmStorage {
	private final HashMap<Integer, Film> films = new HashMap<>();

	public Collection<Film> getAll() {
		return films.values();
	}

	public Film create(FilmDto dto) {
		var film = new Film(getNextId(), dto.getName(), dto.getDescription(), dto.getReleaseDate(), dto.getDuration());
		films.put(film.getId(), film);
		return film;
	}

	public Film getById(int id) {
		var film = films.get(id);
		if (film == null) {
			log.error("Film was not found by this id");
			throw new ItemNotFoundException("Film was not found by this id");
		}
		return film;
	}

	@Override
	public List<Film> getPopularFilms(int count) {
		return getAll().stream()
				.sorted(Comparator.comparing(Film::getLikesAmount).reversed())
				.limit(count)
				.collect(Collectors.toList());
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
		var existingFilm = getById(film.getId());
		films.put(film.getId(), film);
		return film;
	}
}
