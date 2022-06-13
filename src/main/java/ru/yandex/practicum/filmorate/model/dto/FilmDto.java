package ru.yandex.practicum.filmorate.model.dto;

import lombok.Data;
import ru.yandex.practicum.filmorate.constraints.ReleaseDate;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Positive;
import javax.validation.constraints.Size;
import java.time.LocalDate;

@Data
public class FilmDto {
	@NotBlank(message = "Film title must not be blank")
	private final String name;

	@Size(max = 200, message = "Description should be less than 200 symbols")
	private final String description;

	@ReleaseDate(message = "Release date should be greater than 28.12.1895")
	private final LocalDate releaseDate;

	@Positive(message = "Duration must be positive")
	private final int duration;
}
