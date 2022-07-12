package ru.yandex.practicum.filmorate.storage;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.model.Genre;
import ru.yandex.practicum.filmorate.storage.interfaces.GenreStorage;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Collection;
import java.util.Optional;

@Component
@Qualifier("database")
public class GenreDbStorage implements GenreStorage {
	private final JdbcTemplate jdbcTemplate;

	public GenreDbStorage(JdbcTemplate jdbcTemplate) {
		this.jdbcTemplate = jdbcTemplate;
	}

	@Override
	public Collection<Genre> getAll() {
		String sql = "select * from genres";
		return jdbcTemplate.query(sql, (rs, rowNum) -> parseGenre(rs));
	}

	@Override
	public Optional<Genre> getById(int id) {
		var sql = "select * from genres where id = ?";
		try {
			var film = jdbcTemplate.queryForObject(sql, (rs, rowNum) -> parseGenre(rs), id);
			return Optional.ofNullable(film);
		} catch (EmptyResultDataAccessException e) {
			return Optional.empty();
		}
	}

	private Genre parseGenre(ResultSet rs) throws SQLException {
		return new Genre(
				rs.getInt("id"),
				rs.getString("name")
		);
	}
}
