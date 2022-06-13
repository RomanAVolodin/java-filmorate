package ru.yandex.practicum.filmorate.model.dto;

import lombok.Data;
import ru.yandex.practicum.filmorate.constraints.BirthDate;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import java.time.LocalDate;

@Data
public class UserDto {
	@NotBlank(message = "Email should be set")
	@Email(message = "Enter correct email")
	private final String email;

	@NotBlank(message = "Login should be set")
	private final String login;

	private final String name;

	@BirthDate
	private final LocalDate birthday;
}
