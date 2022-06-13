package ru.yandex.practicum.filmorate.storage.interfaces;

import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.model.dto.UserDto;

import java.util.Collection;

public interface UserStorage {
	Collection<User> getAll();

	User getById(int id);

	User create(UserDto dto);

	User update(int id, UserDto dto);

	User replace(User user);

	Collection<User> getFriendsForUser(int id);

	Collection<User> commonFriendsForUsers(int id, int otherId);
}
