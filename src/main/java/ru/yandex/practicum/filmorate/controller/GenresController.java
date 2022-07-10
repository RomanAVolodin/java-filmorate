package ru.yandex.practicum.filmorate.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.model.Genre;
import ru.yandex.practicum.filmorate.service.GenresService;

import java.util.Collection;

@RestController
@RequestMapping(value = "/genres")
public class GenresController {
	private final GenresService service;

	@Autowired
	GenresController(GenresService service) {
		this.service = service;
	}

	@GetMapping(value = "")
	public Collection<Genre> getGenresList() {
		return service.getAll();
	}


	@GetMapping(value = "{id}")
	public Genre getGenre(@PathVariable(name = "id") int id) {
		return service.getById(id);
	}
}
