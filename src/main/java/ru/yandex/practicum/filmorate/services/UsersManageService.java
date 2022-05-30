package ru.yandex.practicum.filmorate.services;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.exceptions.ItemNotFoundException;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.model.dto.UserDto;

import java.util.HashMap;

@Service
@Slf4j
public class UsersManageService extends BaseIdCountableService{
	public final HashMap<Integer, User> users = new HashMap<>();

	public User create(UserDto dto) {
		var user = new User(getNextId(), dto.getEmail(), dto.getLogin(), calculateName(dto), dto.getBirthday());
		users.put(user.getId(), user);
		return user;
	}

	public User update(int id, UserDto dto) {
		var user = users.get(id);
		if (user == null) {
			log.error("User was not found by this id");
			throw new ItemNotFoundException("User was not found by this id");
		}
		user.setName(calculateName(dto));
		user.setLogin(dto.getLogin());
		user.setEmail(dto.getEmail());
		user.setBirthday(dto.getBirthday());
		return user;
	}

	public User replace(User user) {
		users.put(user.getId(), user);
		return user;
	}

	private String calculateName(UserDto dto) {
		return dto.getName() != null && !dto.getName().isBlank() ? dto.getName() : dto.getLogin();
	}
}
