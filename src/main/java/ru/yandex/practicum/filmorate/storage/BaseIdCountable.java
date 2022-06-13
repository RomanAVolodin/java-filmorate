package ru.yandex.practicum.filmorate.storage;

import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

@Component
@Scope("prototype")
public class BaseIdCountable {
	protected int counter = 0;
	public int getNextId() {
		return ++counter;
	}
}
