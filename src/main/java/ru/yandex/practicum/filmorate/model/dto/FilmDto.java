package ru.yandex.practicum.filmorate.model.dto;

import lombok.Data;
import ru.yandex.practicum.filmorate.constraints.ReleaseDate;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Positive;
import javax.validation.constraints.Size;
import java.time.LocalDate;
import java.util.HashMap;
import java.util.List;

@Data
public class FilmDto {

	private Integer id;

	@NotBlank(message = "Film's title must not be blank")
	private final String name;

	@Size(max = 200, message = "Description should be less than 200 symbols")
	@NotBlank(message = "Film's description must not be blank")
	private final String description;

	@ReleaseDate(message = "Release date should be greater than 28.12.1895")
	private final LocalDate releaseDate;

	@Positive(message = "Duration must be positive")
	private final Integer duration;

	private final Integer rate;

	private HashMap<String, Integer> mpa;

    private List<HashMap<String, Integer>> genres;
}
