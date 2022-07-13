package ru.yandex.practicum.filmorate.model;

import lombok.Builder;
import lombok.Data;
import lombok.NonNull;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Data
@Builder
public class Film {

	private Integer id;

	@NonNull
	private String name;

	@NonNull
	private String description;

	@NonNull
	private LocalDate releaseDate;

	@NonNull
	private Integer duration;

	private Integer likesAmount;

	private Integer rate;

	@Builder.Default
	private Set<Integer> likes = new HashSet<>();

	@Builder.Default
	private List<Genre> genres = new ArrayList<>();

	@Builder.Default
	private Integer mpaId = null;

	@Builder.Default
	private Mpa mpa = null;

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
