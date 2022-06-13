package ru.yandex.practicum.filmorate.storage;

public class BaseIdCountable {
	protected int counter = 0;
	protected int getNextId() {
		return ++counter;
	}
}
