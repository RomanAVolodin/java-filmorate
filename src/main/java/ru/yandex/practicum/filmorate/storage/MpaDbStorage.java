package ru.yandex.practicum.filmorate.storage;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.model.Mpa;
import ru.yandex.practicum.filmorate.storage.interfaces.MpaStorage;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Collection;
import java.util.Optional;

@Component
@Qualifier("database")
public class MpaDbStorage implements MpaStorage {

	private final JdbcTemplate jdbcTemplate;

	public MpaDbStorage(JdbcTemplate jdbcTemplate) {
		this.jdbcTemplate = jdbcTemplate;
	}

	@Override
	public Collection<Mpa> getAll() {
		String sql = "select * from mpas";
		return jdbcTemplate.query(sql, (rs, rowNum) -> parseMpa(rs));
	}

	@Override
	public Optional<Mpa> getById(int id) {
		var sql = "select * from mpas where id = ?";
		try {
			var film = jdbcTemplate.queryForObject(sql, (rs, rowNum) -> parseMpa(rs), id);
			return Optional.ofNullable(film);
		} catch (EmptyResultDataAccessException e) {
			return Optional.empty();
		}
	}

	private Mpa parseMpa(ResultSet rs) throws SQLException {
		return new Mpa(
				rs.getInt("id"),
				rs.getString("name")
		);
	}
}
