package ru.yandex.practicum.filmorate.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.model.dto.UserDto;
import ru.yandex.practicum.filmorate.storage.InMemoryUserStorage;
import ru.yandex.practicum.filmorate.storage.interfaces.UserStorage;

import java.util.Collection;

@Service
public class UsersService {
	private final UserStorage storage;

	@Autowired
	UsersService(InMemoryUserStorage storage) {
		this.storage = storage;
	}

	public Collection<User> getAll() {
		return storage.getAll();
	}

	public User getById(int id) {
		return storage.getById(id);
	}

	public User create(UserDto dto) {
		return storage.create(dto);
	}

	public User update(int id, UserDto dto) {
		return storage.update(id, dto);
	}

	public User replace(User user) {
		return storage.replace(user);
	}

	public void addFriendToUser(int id, int friendId) {
		var user = storage.getById(id);
		var friend = storage.getById(friendId);
		user.addFriend(friendId);
		friend.addFriend(id);
	}

	public void removeFriendFromUser(int id, int friendId) {
		var user = storage.getById(id);
		var friend = storage.getById(friendId);
		user.removeFriend(friendId);
		friend.removeFriend(id);
	}

	public Collection<User> getFriendsForUser(int id) {
		return storage.getFriendsForUser(id);
	}

	public Collection<User> getCommonFriendsForUsers(int id, int otherId) {
		return storage.commonFriendsForUsers(id, otherId);
	}
}
