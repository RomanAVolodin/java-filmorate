package ru.yandex.practicum.filmorate.model;

import lombok.Data;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;

@Data
@RequiredArgsConstructor
public class Genre {

	@NonNull
	private Integer id;

	@NonNull
	private String name;
}
