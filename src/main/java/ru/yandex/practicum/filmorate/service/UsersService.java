package ru.yandex.practicum.filmorate.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.exceptions.ItemNotFoundException;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.model.dto.UserDto;
import ru.yandex.practicum.filmorate.storage.BaseIdCountable;
import ru.yandex.practicum.filmorate.storage.interfaces.UserStorage;

import java.util.Collection;
import java.util.Set;
import java.util.stream.Collectors;

@Service
public class UsersService {
	private final BaseIdCountable idGenerator;
	private final UserStorage storage;

	@Autowired
	public UsersService(@Qualifier("database") UserStorage storage, BaseIdCountable idGenerator) {
		this.storage = storage;
		this.idGenerator = idGenerator;
	}

	public Collection<User> getAll() {
		return storage.getAll();
	}

	public User getById(int id) {
		return storage.getById(id).orElseThrow(() -> new ItemNotFoundException("User was not found by id"));
	}

	public User create(UserDto dto) {
		var user = User.builder()
				.id(idGenerator.getNextId())
				.email(dto.getEmail())
				.login(dto.getLogin())
				.name(calculateName(dto))
				.birthday(dto.getBirthday())
				.build();
		return storage.create(user);
	}

	public User update(int id, UserDto dto) {
		var user = getById(id);
		user.setName(calculateName(dto));
		user.setLogin(dto.getLogin());
		user.setEmail(dto.getEmail());
		user.setBirthday(dto.getBirthday());
		return user;
	}

	public User replace(User user) {
		var oldUser = getById(user.getId());
		return storage.replace(user);
	}

	public void addFriendToUser(int id, int friendId) {
		var user = getById(id);
		var friend = getById(friendId);
		storage.addFriend(user.getId(), friend.getId());
	}

	public void removeFriendFromUser(int id, int friendId) {
		storage.removeFriend(id, friendId);
	}

	public Collection<User> getFriendsForUser(int id) {
		var user = getById(id);
		return storage.getUserFriends(user.getId());
	}

	public Collection<User> getCommonFriendsForUsers(int id, int otherId) {
		var firstUser = getById(id);
		var secondUser = getById(otherId);

		Set<Integer> commonIds = getFriendsForUser(firstUser.getId()).stream().map(User::getId).collect(Collectors.toSet());
		commonIds.retainAll(getFriendsForUser(secondUser.getId()).stream().map(User::getId).collect(Collectors.toSet()));
		return storage.getUsersByIdSet(commonIds);
	}

	private String calculateName(UserDto dto) {
		return dto.getName() != null && !dto.getName().isBlank() ? dto.getName() : dto.getLogin();
	}
}
