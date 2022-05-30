package ru.yandex.practicum.filmorate.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.model.dto.UserDto;
import ru.yandex.practicum.filmorate.services.UsersManageService;

import javax.validation.Valid;
import java.util.Collection;


@RestController
@Slf4j
@RequestMapping(value = "/users")
public class UserController {
	private final UsersManageService service;

	@Autowired
	UserController(UsersManageService service) {
		this.service = service;
	}

	@GetMapping(value = "")
	@ResponseBody
	public Collection<User> getUsersList() {
		return service.users.values();
	}

	@PostMapping(value = "", consumes = {"application/json"})
	@ResponseStatus(HttpStatus.CREATED)
	@ResponseBody
	public User create(@Valid @RequestBody UserDto dto) {
		log.info("User created: {}", dto);
		return service.create(dto);
	}

	@PatchMapping(value = "{id}", consumes = {"application/json"})
	@ResponseBody
	public User updateUser(@PathVariable(name = "id") int id, @Valid @RequestBody UserDto dto) {
		log.info("User updated: {}", dto);
		return service.update(id, dto);
	}

	@PutMapping(value = "")
	@ResponseBody
	public User editUser(@Valid @RequestBody User dto) {
		log.info("User is updating: {}", dto);
		return service.replace(dto);
	}
}
