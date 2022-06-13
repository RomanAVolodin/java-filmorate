package ru.yandex.practicum.filmorate.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.dto.FilmDto;
import ru.yandex.practicum.filmorate.services.FilmsManageService;

import javax.validation.Valid;
import java.util.Collection;


@RestController
@Slf4j
@RequestMapping(value = "/films")
public class FilmController {
	private final FilmsManageService service;

	@Autowired
	FilmController(FilmsManageService service) {
		this.service = service;
	}

	@GetMapping(value = "")
	public Collection<Film> getFilmsList() {
		return service.films.values();
	}

	@PostMapping(value = "", consumes = {"application/json"})
	@ResponseStatus(HttpStatus.CREATED)
	public Film create(@Valid @RequestBody FilmDto dto) {
		log.info("Film created: {}", dto);
		return service.create(dto);
	}

	@PatchMapping(value = "{id}", consumes = {"application/json"})
	public Film updateFilm(@PathVariable(name = "id") int id, @Valid @RequestBody FilmDto dto) {
		log.info("Film is updating: {}", dto);
		return service.update(id, dto);
	}

	// Ниже детский код для прохождения тестов. Я такое заворачиваю, если кто пришлет на ревью
	@PutMapping(value = "")
	public Film editFilm(@Valid @RequestBody Film dto) {
		log.info("Film is updating: {}", dto);
		return service.replace(dto);
	}
}
