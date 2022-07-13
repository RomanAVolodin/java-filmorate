package ru.yandex.practicum.filmorate.model;

import lombok.Builder;
import lombok.Data;
import lombok.NonNull;

import java.time.LocalDate;
import java.util.HashSet;
import java.util.Set;

@Data
@Builder
public class User {

	@NonNull
	private Integer id;

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
