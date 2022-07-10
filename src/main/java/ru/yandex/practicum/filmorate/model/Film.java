package ru.yandex.practicum.filmorate.model;

import lombok.Data;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Data
@RequiredArgsConstructor
public class Film {
	@NonNull
	private int id;

	@NonNull
	private String name;

	@NonNull
	private String description;

	@NonNull
	private LocalDate releaseDate;

	@NonNull
	private int duration;

	private int likesAmount;

	@NonNull
	private int rate;

	private Set<Integer> likes = new HashSet<>();

	private List<Genre> genres = new ArrayList<>();

	private int mpaId;
	private Mpa mpa;

	public void addLike(int id) {
		likes.add(id);
	}

	public void removeLike(int id) {
		likes.remove(id);
	}

	public void increaseLikesAmount() {
		likesAmount++;
	}

	public void decreaseLikesAmount() {
		likesAmount--;
	}
}
