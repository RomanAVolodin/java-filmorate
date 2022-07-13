package ru.yandex.practicum.filmorate.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import ru.yandex.practicum.filmorate.model.Mpa;
import ru.yandex.practicum.filmorate.service.MpaService;

import java.util.Collection;

@RestController
@RequestMapping(value = "/mpa")
public class MpaController {
	private final MpaService service;

	@Autowired
	public MpaController(MpaService service) {
		this.service = service;
	}

	@GetMapping(value = "")
	public Collection<Mpa> getMpaList() {
		return service.getAll();
	}


	@GetMapping(value = "{id}")
	public Mpa getMpa(@PathVariable(name = "id") int id) {
		return service.getById(id);
	}
}
