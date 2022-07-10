package ru.yandex.practicum.filmorate.storage;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.storage.interfaces.UserStorage;

import java.util.*;
import java.util.stream.Collectors;

@Component
@Qualifier("memory")
@Slf4j
public class InMemoryUserStorage implements UserStorage {
	private final HashMap<Integer, User> users = new HashMap<>();

	public Collection<User> getAll() {
		return users.values();
	}

	public Optional<User> getById(int id) {
		return Optional.ofNullable(users.get(id));
	}

	public User create(User user) {
		users.put(user.getId(), user);
		return user;
	}

	public User replace(User user) {
		var existingUser = getById(user.getId());
		users.put(user.getId(), user);
		return user;
	}

	public Collection<User> getUsersByIdSet(Set<Integer> ids) {
		return users.entrySet().stream().filter(x -> ids.contains(x.getKey()))
				.collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue)).values();
	}

	@Override
	public void addFriend(int id, int friendId) {

	}

	@Override
	public void removeFriend(int id, int friendId) {

	}

	@Override
	public Collection<User> getUserFriends(int id) {
		return null;
	}
}
