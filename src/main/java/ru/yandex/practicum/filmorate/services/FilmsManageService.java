package ru.yandex.practicum.filmorate.services;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.exceptions.ItemNotFoundException;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.dto.FilmDto;

import java.util.HashMap;

@Service
@Slf4j
public class FilmsManageService extends BaseIdCountableService {
	public final HashMap<Integer, Film> films = new HashMap<>();

	public Film create(FilmDto dto) {
		var film = new Film(getNextId(), dto.getName(), dto.getDescription(), dto.getReleaseDate(), dto.getDuration());
		films.put(film.getId(), film);
		return film;
	}

	public Film update(int id, FilmDto dto) {
		var film = films.get(id);
		if (film == null) {
			log.error("Film was not found by this id");
			throw new ItemNotFoundException("Film was not found by this id");
		}
		film.setName(dto.getName());
		film.setDescription(dto.getDescription());
		film.setReleaseDate(dto.getReleaseDate());
		film.setDuration(dto.getDuration());
		return film;
	}

	public Film replace(Film film) {
		films.put(film.getId(), film);
		return film;
	}
}
