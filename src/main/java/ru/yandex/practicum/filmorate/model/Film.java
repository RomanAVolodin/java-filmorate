package ru.yandex.practicum.filmorate.model;

import lombok.Data;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;

import java.time.LocalDate;
import java.util.HashSet;
import java.util.Set;

@Data
@RequiredArgsConstructor
public class Film {
	@NonNull
	private final int id;

	@NonNull
	private String name;

	@NonNull
	private String description;

	@NonNull
	private LocalDate releaseDate;

	@NonNull
	private int duration;

	private Set<Integer> likes = new HashSet<>();

	public void addLike(int id) {
		likes.add(id);
	}

	public void removeLike(int id) {
		likes.remove(id);
	}

	private int likesAmount;

	public void increaseLikesAmount() {
		likesAmount++;
	}

	public void decreaseLikesAmount() {
		likesAmount--;
	}
}
