package ru.yandex.practicum.filmorate.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.exceptions.ItemNotFoundException;
import ru.yandex.practicum.filmorate.model.Genre;
import ru.yandex.practicum.filmorate.storage.interfaces.GenreStorage;

import java.util.Collection;

@Service
public class GenresService {

	private final GenreStorage storage;

	@Autowired
	GenresService(@Qualifier("database") GenreStorage storage) {
		this.storage = storage;
	}

	public Genre getById(int id) {
		return storage.getById(id).orElseThrow(() -> new ItemNotFoundException("Genre was not found by id"));
	}

	public Collection<Genre> getAll() {
		return storage.getAll();
	}

}
