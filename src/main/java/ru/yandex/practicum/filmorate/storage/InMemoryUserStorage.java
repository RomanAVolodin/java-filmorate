package ru.yandex.practicum.filmorate.storage;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.exceptions.ItemNotFoundException;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.model.dto.UserDto;
import ru.yandex.practicum.filmorate.storage.interfaces.UserStorage;

import java.util.*;
import java.util.stream.Collectors;

@Component
@Slf4j
public class InMemoryUserStorage extends BaseIdCountable implements UserStorage {
	private final HashMap<Integer, User> users = new HashMap<>();

	public Collection<User> getAll() {
		return users.values();
	}

	public User getById(int id) {
		var user = users.get(id);
		if (user == null) {
			log.error("User was not found by this id");
			throw new ItemNotFoundException("User was not found by this id");
		}
		return user;
	}

	public User create(UserDto dto) {
		var user = new User(getNextId(), dto.getEmail(), dto.getLogin(), calculateName(dto), dto.getBirthday());
		users.put(user.getId(), user);
		return user;
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
		var existingUser = getById(user.getId());
		users.put(user.getId(), user);
		return user;
	}

	@Override
	public Collection<User> getFriendsForUser(int id) {
		var user = getById(id);
		var usersIds = user.getFriends();
		return getUsersByIdSet(usersIds);
	}

	@Override
	public Collection<User> commonFriendsForUsers(int id, int otherId) {
		var firstUser = getById(id);
		var secondUser = getById(otherId);

		Set<Integer> commonIds = new HashSet<>(firstUser.getFriends());
		commonIds.retainAll(secondUser.getFriends());
		return getUsersByIdSet(commonIds);
	}

	private Collection<User> getUsersByIdSet(Set<Integer> ids) {
		return users.entrySet().stream().filter(x -> ids.contains(x.getKey()))
				.collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue)).values();
	}

	private String calculateName(UserDto dto) {
		return dto.getName() != null && !dto.getName().isBlank() ? dto.getName() : dto.getLogin();
	}
}
