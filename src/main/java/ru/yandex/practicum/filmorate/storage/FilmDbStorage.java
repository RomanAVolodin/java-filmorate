package ru.yandex.practicum.filmorate.storage;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.PreparedStatementCreator;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.exceptions.ItemNotFoundException;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.Genre;
import ru.yandex.practicum.filmorate.model.Mpa;
import ru.yandex.practicum.filmorate.storage.interfaces.FilmStorage;

import java.sql.*;
import java.sql.Date;
import java.util.*;
import java.util.stream.Collectors;

@Component
@Qualifier("database")
@Slf4j
public class FilmDbStorage implements FilmStorage {
	private final JdbcTemplate jdbcTemplate;

	public FilmDbStorage(JdbcTemplate jdbcTemplate) {
		this.jdbcTemplate = jdbcTemplate;
	}

	@Override
	public Collection<Film> getAll() {
		String sql = "select *, " +
				"(select STRING_AGG(user_id, ',') from likes where film_id = films.id) likes, " +
				"(select STRING_AGG(genre_id, ',') from film_genre where film_id = films.id) genres_ids, " +
				"mpas.id as mpa_id, mpas.name as mpa_name from films left join mpas " +
				"on films.mpa = mpas.id GROUP BY films.id order by films.id asc";
		return jdbcTemplate.query(sql, (rs, rowNum) -> parseFilm(rs));
	}

	@Override
	public Film create(Film film) {
		KeyHolder key = new GeneratedKeyHolder();
		jdbcTemplate.update(new PreparedStatementCreator() {
			@Override
			public PreparedStatement createPreparedStatement(Connection connection) throws SQLException {
				final PreparedStatement ps = connection.prepareStatement(
						"insert into films (name, description, releasedate, duration, rate, mpa) values (?, ?, ?, ?, ?, ?)",
						Statement.RETURN_GENERATED_KEYS);
				ps.setString(1, film.getName());
				ps.setString(2, film.getDescription());
				ps.setDate(3, Date.valueOf(film.getReleaseDate()));
				ps.setInt(4, film.getDuration());
				ps.setInt(5, film.getRate());
				ps.setInt(6, film.getMpaId());
				return ps;
			}
		}, key);

		film.setId((Integer) key.getKey());
		createFilmGenreConnection(film);
		return film;
	}

	@Override
	public Film replace(Film film) {
		jdbcTemplate.update(
				"update films " +
						"set name = ?, description = ?, releasedate = ? , duration = ? , rate = ?, mpa = ? " +
						"where id = ?",
				film.getName(),
				film.getDescription(),
				film.getReleaseDate(),
				film.getDuration(),
				film.getRate(),
				film.getMpaId(),
				film.getId()
		);

		createFilmGenreConnection(film);
		return film;
	}

	@Override
	public Optional<Film> getById(int id) {
		var sql = "select *, " +
				"(select STRING_AGG(user_id, ',') from likes where film_id = films.id) likes, " +
				"(select STRING_AGG(genre_id, ',') from film_genre where film_id = films.id) genres_ids, " +
				" mpas.id as mpa_id, mpas.name as mpa_name from films left join mpas " +
				"on films.mpa = mpas.id where films.id = ?";
		try {
			var film = jdbcTemplate.queryForObject(sql, (rs, rowNum) -> parseFilm(rs), id);
			return Optional.ofNullable(film);
		} catch (EmptyResultDataAccessException e) {
			return Optional.empty();
		}
	}

	@Override
	public Collection<Film> getPopularFilms(int count) {
		String sql = "select *, " +
				"(select STRING_AGG(user_id, ',') from likes where film_id = films.id) likes, " +
				"(select STRING_AGG(genre_id, ',') from film_genre where film_id = films.id) genres_ids, " +
				"mpas.id as mpa_id, mpas.name as mpa_name from films left join mpas " +
				"on films.mpa = mpas.id order by likesAmount desc limit ?";
		return jdbcTemplate.query(sql, (rs, rowNum) -> parseFilm(rs), count);
	}

	@Override
	public void addLike(int userId, int filmId) {
		var film = getById(filmId).orElseThrow(() -> new ItemNotFoundException("Film was not found by id"));
		if (film.getLikes().contains(userId)) {
			return;
		}
		jdbcTemplate.update("INSERT INTO likes (user_id, film_id) values (?, ?)", userId, filmId);
		recalculateLikes(filmId);
	}

	@Override
	public void removeLike(int userId, int filmId) {
		jdbcTemplate.update(
				"DELETE FROM likes WHERE user_id = ? and film_id = ?",
				userId,
				filmId
		);
		recalculateLikes(filmId);
	}

	private void recalculateLikes(int filmId) {
		int amount = jdbcTemplate.queryForObject(
				"SELECT count(*) FROM likes WHERE film_id = ?", Integer.class, filmId
		);
		jdbcTemplate.update("update films set likesAmount = ? where id = ?", amount, filmId);
	}

	private Film parseFilm(ResultSet rs) throws SQLException {
		var film = Film.builder()
				.id(rs.getInt("id"))
				.name(rs.getString("name"))
				.description(rs.getString("description"))
				.releaseDate(rs.getDate("releasedate").toLocalDate())
				.duration(rs.getInt("duration"))
				.rate(rs.getInt("rate"))
				.mpaId(rs.getInt("mpa"))
				.build();
		film.setLikesAmount(rs.getInt("likesAmount"));
		if (rs.getInt("mpa_id") > 0 && !rs.getString("mpa_name").isEmpty()) {
			film.setMpa(new Mpa(rs.getInt("mpa_id"), rs.getString("mpa_name")));
		}
		var likesArray = rs.getString("likes");
		if (likesArray != null) {
			var likes = Arrays.stream(likesArray.split(","))
					.map(Integer::parseInt)
					.collect(Collectors.toSet());
			film.setLikes(likes);
		}
		var genresIdsArray = rs.getString("genres_ids");
		if (genresIdsArray != null) {
			var ids = Arrays.stream(genresIdsArray.split(","))
					.map(Integer::parseInt)
					.collect(Collectors.toSet());
			List<Genre> genres = new ArrayList<>();
			for (var id : ids ) {
				Genre genre = jdbcTemplate.queryForObject(
						"SELECT * FROM genres WHERE id = ?",
						(res, rowNum) -> new Genre(res.getInt("id"), res.getString("name")),
						id
				);
				genres.add(genre);
			}
			film.setGenres(genres);
		}
		return film;
	}

	private void createFilmGenreConnection(Film film) {
		if (film.getGenres() != null) {
			jdbcTemplate.update("delete from film_genre where film_id = ?", film.getId());
			for (var genre : film.getGenres() ) {
				try {
					jdbcTemplate.update(
							"insert into film_genre (film_id, genre_id) values (?, ?)",
							film.getId(),
							genre.getId()
					);
				} catch (DuplicateKeyException e) {
					log.warn("Genre already added");
				}

			}
		}
	}
}
