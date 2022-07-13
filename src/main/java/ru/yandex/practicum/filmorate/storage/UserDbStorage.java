package ru.yandex.practicum.filmorate.storage;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.storage.interfaces.UserStorage;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.*;
import java.util.stream.Collectors;

@Component
@Qualifier("database")
public class UserDbStorage implements UserStorage {
	private final JdbcTemplate jdbcTemplate;

	public UserDbStorage(JdbcTemplate jdbcTemplate) {
		this.jdbcTemplate = jdbcTemplate;
	}

	@Override
	public Collection<User> getAll() {
		String sql = "select * from users order by id desc";
		return jdbcTemplate.query(sql, (rs, rowNum) -> parseUser(rs));
	}

	@Override
	public Optional<User> getById(int id) {
		var sql = "select * from users where id = ?";
		try {
			var film = jdbcTemplate.queryForObject(sql, (rs, rowNum) -> parseUser(rs), id);
			return Optional.ofNullable(film);
		} catch (EmptyResultDataAccessException e) {
			return Optional.empty();
		}
	}

	@Override
	public User create(User user) {
		jdbcTemplate.update(
				"insert into users (login, name, birthday, email) values (?, ?, ?, ?)",
				user.getLogin(),
				user.getName(),
				user.getBirthday(),
				user.getEmail()
		);
		int id = jdbcTemplate.queryForObject("SELECT id FROM users order by id desc limit 1", Integer.class);
		user.setId(id);
		return user;
	}

	@Override
	public User replace(User user) {
		jdbcTemplate.update(
				"update users set login = ?, name = ?, birthday = ? , email = ?  where id = ?",
				user.getLogin(),
				user.getName(),
				user.getBirthday(),
				user.getEmail(),
				user.getId()
		);
		return user;
	}

	@Override
	public Collection<User> getUsersByIdSet(Set<Integer> ids) {
		return getAll().stream().filter(x -> ids.contains(x.getId()))
				.collect(Collectors.toList());
	}

	@Override
	public void addFriend(int id, int friendId) {
		jdbcTemplate.update(
				"insert into friends (user_id, friend_id) values (?, ?)",
				id,
				friendId
		);
	}

	@Override
	public void removeFriend(int id, int friendId) {
		jdbcTemplate.update(
				"delete from friends where user_id = ? and friend_id =?",
				id,
				friendId
		);
	}

	@Override
	public Collection<User> getUserFriends(int id) {
		String sql = "select users.* from friends " +
				"inner join users on users.id = friends.friend_id " +
				"where friends.user_id = ?";
		return jdbcTemplate.query(sql, (rs, rowNum) -> parseUser(rs), id);
	}

	private User parseUser(ResultSet rs) throws SQLException {
		return User.builder()
				.id(rs.getInt("id"))
				.email(rs.getString("email"))
				.login(rs.getString("login"))
				.name(rs.getString("name"))
				.birthday(rs.getDate("birthday").toLocalDate())
				.build();
	}
}
