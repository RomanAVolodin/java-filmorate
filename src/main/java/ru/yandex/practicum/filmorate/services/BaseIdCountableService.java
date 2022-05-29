package ru.yandex.practicum.filmorate.services;

public class BaseIdCountableService {
	protected int counter = 0;
	protected int getNextId() {
		return ++counter;
	}
}
