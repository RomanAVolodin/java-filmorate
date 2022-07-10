package ru.yandex.practicum.filmorate.model;

import lombok.Data;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;

import java.time.LocalDate;
import java.util.HashSet;
import java.util.Set;

@Data
@RequiredArgsConstructor
public class User {
	@NonNull
	private int id;

	@NonNull
	private String email;

	@NonNull
	private String login;

	@NonNull
	private String name;

	@NonNull
	private LocalDate birthday;

	private Set<Integer> friends = new HashSet<>();

	public void addFriend(int id) {
		friends.add(id);
	}

	public void removeFriend(int id) {
		friends.remove(id);
	}
}
