package ru.yandex.practicum.filmorate.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.exceptions.ItemNotFoundException;
import ru.yandex.practicum.filmorate.model.Mpa;
import ru.yandex.practicum.filmorate.storage.interfaces.MpaStorage;

import java.util.Collection;

@Service
public class MpaService {

	private final MpaStorage storage;

	@Autowired
	MpaService(@Qualifier("database") MpaStorage storage) {
		this.storage = storage;
	}

	public Mpa getById(int id) {
		return storage.getById(id).orElseThrow(() -> new ItemNotFoundException("Mpa was not found by id"));
	}

	public Collection<Mpa> getAll() {
		return storage.getAll();
	}

}
