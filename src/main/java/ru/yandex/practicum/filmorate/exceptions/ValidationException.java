package ru.yandex.practicum.filmorate.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.server.ResponseStatusException;

public class ValidationException extends ResponseStatusException {
	public ValidationException(String message) {
		super(HttpStatus.INTERNAL_SERVER_ERROR, message);
	}
}
