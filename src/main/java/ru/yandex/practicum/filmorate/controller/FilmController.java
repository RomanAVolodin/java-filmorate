package ru.yandex.practicum.filmorate.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.dto.FilmDto;
import ru.yandex.practicum.filmorate.service.FilmsService;

import javax.validation.Valid;
import javax.validation.constraints.Positive;
import java.util.Collection;

import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;

@RestController
@Validated
@Slf4j
@RequestMapping(value = "/films")
public class FilmController {
	private final FilmsService service;

	@Autowired
	public FilmController(FilmsService service) {
		this.service = service;
	}

	@GetMapping(value = "")
	public Collection<Film> getFilmsList() {
		return service.getAll();
	}

	@GetMapping(value = "popular")
	public Collection<Film> getPopularFilm(@Positive @RequestParam(name = "count", defaultValue = "10") int count) {
		return service.getPopularFilms(count);
	}

	@GetMapping(value = "{id}")
	public Film getFilm(@PathVariable(name = "id") int id) {
		return service.getById(id);
	}

	@PostMapping(value = "", consumes = {APPLICATION_JSON_VALUE})
	@ResponseStatus(HttpStatus.CREATED)
	public Film create(@Valid @RequestBody FilmDto dto) {
		log.info("Film created: {}", dto);
		return service.create(dto);
	}

	@PatchMapping(value = "{id}", consumes = {APPLICATION_JSON_VALUE})
	public Film updateFilm(@PathVariable(name = "id") int id, @Valid @RequestBody FilmDto dto) {
		log.info("Film is updating: {}", dto);
		return service.update(id, dto);
	}

	@PutMapping(value = "")
	public Film editFilm(@Valid @RequestBody FilmDto dto) {
		log.info("Film is updating: {}", dto);
		return service.replace(dto);
	}

	@PutMapping(value = "{id}/like/{userId}")
	@ResponseStatus(HttpStatus.OK)
	public void addLike(@PathVariable(name = "id") int id, @PathVariable(name = "userId") int userId) {
		service.addLikeFromUser(id, userId);
	}

	@DeleteMapping(value = "{id}/like/{userId}")
	@ResponseStatus(HttpStatus.OK)
	public void removeLike(@PathVariable(name = "id") int id, @PathVariable(name = "userId") int userId) {
		service.removeLikeFromUser(id, userId);
	}
}
