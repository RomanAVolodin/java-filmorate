package ru.yandex.practicum.filmorate.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.model.dto.UserDto;
import ru.yandex.practicum.filmorate.service.UsersService;

import javax.validation.Valid;
import java.util.Collection;


@RestController
@Slf4j
@RequestMapping(value = "/users")
public class UserController {
	private final UsersService service;

	@Autowired
	UserController(UsersService service) {
		this.service = service;
	}

	@GetMapping(value = "")
	public Collection<User> getUsersList() {
		return service.getAll();
	}

	@GetMapping(value = "{id}")
	public User getById(@PathVariable(name = "id") int id) {
		return service.getById(id);
	}

	@PostMapping(value = "", consumes = {"application/json"})
	@ResponseStatus(HttpStatus.CREATED)
	public User create(@Valid @RequestBody UserDto dto) {
		log.info("User created: {}", dto);
		return service.create(dto);
	}

	@PatchMapping(value = "{id}", consumes = {"application/json"})
	public User updateUser(@PathVariable(name = "id") int id, @Valid @RequestBody UserDto dto) {
		log.info("User updated: {}", dto);
		return service.update(id, dto);
	}

	@PutMapping(value = "")
	public User editUser(@Valid @RequestBody User dto) {
		log.info("User is updating: {}", dto);
		return service.replace(dto);
	}

	@PutMapping(value = "{id}/friends/{friendId}")
	@ResponseStatus(HttpStatus.OK)
	public void addFriend(@PathVariable(name = "id") int id, @PathVariable(name = "friendId") int friendId) {
		service.addFriendToUser(id, friendId);
	}

	@DeleteMapping (value = "{id}/friends/{friendId}")
	@ResponseStatus(HttpStatus.OK)
	public void removeFriend(@PathVariable(name = "id") int id, @PathVariable(name = "friendId") int friendId) {
		service.removeFriendFromUser(id, friendId);
	}

	@GetMapping(value = "{id}/friends")
	public Collection<User> getUserFriends(@PathVariable(name = "id") int id) {
		return service.getFriendsForUser(id);
	}

	@GetMapping(value = "{id}/friends/common/{otherId}")
	public Collection<User> getUserFriends(
			@PathVariable(name = "id") int id, @PathVariable(name = "otherId") int otherId
	) {
		return service.getCommonFriendsForUsers(id, otherId);
	}
}
