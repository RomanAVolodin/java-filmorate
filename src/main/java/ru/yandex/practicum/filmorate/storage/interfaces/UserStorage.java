package ru.yandex.practicum.filmorate.storage.interfaces;

import ru.yandex.practicum.filmorate.model.User;

import java.util.Collection;
import java.util.Optional;
import java.util.Set;

public interface UserStorage {
	Collection<User> getAll();

	Optional<User> getById(int id);

	User create(User user);

	User replace(User user);

	Collection<User> getUsersByIdSet(Set<Integer> ids);

	void addFriend(int id, int friendId);

	void removeFriend(int id, int friendId);

	Collection<User> getUserFriends(int id);
}
