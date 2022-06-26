package ru.yandex.practicum.filmorate;

import com.google.gson.Gson;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.Map;

import static java.util.Map.entry;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

@DisplayName("Тестирование ручки Пользователи")
public class UserEndpointsTest {
	private final String baseUrlString = "http://localhost:8080/users";
	private final HttpClient client = HttpClient.newHttpClient();

	private final Gson gson = new Gson();

	@Test
	@DisplayName("Проверка получения списка")
	void checkFilmsList() {
		URI url = URI.create(baseUrlString);
		var request = HttpRequest.newBuilder().uri(url).GET().build();
		try {
			HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
			assertNotNull(response);
			assertEquals(response.statusCode(), HttpStatus.OK.value());
		} catch (IOException | InterruptedException e) {
			throw new RuntimeException(e.getMessage());
		}
	}

	@Test
	@DisplayName("Проверка корректного добавления пользователя")
	void checkUserCreation() {
		URI url = URI.create(baseUrlString);
		Map<String, String> user =  Map.ofEntries(
				entry("email", "email@mail.ru"),
				entry("login", "login"),
				entry("name", "name"),
				entry("birthday", "2021-12-29")
		);
		var json = gson.toJson(user);
		final HttpRequest.BodyPublisher body = HttpRequest.BodyPublishers.ofString(json);
		var request = HttpRequest.newBuilder()
				.uri(url)
				.header("content-type", "application/json")
				.POST(body)
				.build();
		try {
			HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
			assertNotNull(response);
			assertEquals(response.statusCode(), HttpStatus.CREATED.value());
		} catch (IOException | InterruptedException e) {
			throw new RuntimeException(e.getMessage());
		}
	}

	@Test
	@DisplayName("Проверка добавления пользователя с некорректным email")
	void checkInvalidEmailUserCreation() {
		URI url = URI.create(baseUrlString);
		Map<String, String> user =  Map.ofEntries(
				entry("email", "email_incorrect"),
				entry("login", "login"),
				entry("name", "name"),
				entry("birthday", "2021-12-29")
		);
		var json = gson.toJson(user);
		final HttpRequest.BodyPublisher body = HttpRequest.BodyPublishers.ofString(json);
		var request = HttpRequest.newBuilder()
				.uri(url)
				.header("content-type", "application/json")
				.POST(body)
				.build();
		try {
			HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
			assertNotNull(response);
			assertEquals(response.statusCode(), HttpStatus.BAD_REQUEST.value());
			assertEquals(response.body(), "{\"error\":\"email: Enter correct email\"}");
		} catch (IOException | InterruptedException e) {
			throw new RuntimeException(e.getMessage());
		}
	}

	@Test
	@DisplayName("Проверка добавления пользователя с пустым логином")
	void checkInvalidLoginUserCreation() {
		URI url = URI.create(baseUrlString);
		Map<String, String> user =  Map.ofEntries(
				entry("email", "email@mail.ru"),
				entry("login", ""),
				entry("name", "name"),
				entry("birthday", "2021-12-29")
		);
		var json = gson.toJson(user);
		final HttpRequest.BodyPublisher body = HttpRequest.BodyPublishers.ofString(json);
		var request = HttpRequest.newBuilder()
				.uri(url)
				.header("content-type", "application/json")
				.POST(body)
				.build();
		try {
			HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
			assertNotNull(response);
			assertEquals(response.statusCode(), HttpStatus.BAD_REQUEST.value());
			assertEquals(response.body(), "{\"error\":\"login: Login should be set\"}");
		} catch (IOException | InterruptedException e) {
			throw new RuntimeException(e.getMessage());
		}
	}

	@Test
	@DisplayName("Проверка добавления пользователя с пустым логином")
	void checkInvalidBirthdateUserCreation() {
		URI url = URI.create(baseUrlString);
		Map<String, String> user =  Map.ofEntries(
				entry("email", "email@mail.ru"),
				entry("login", "login"),
				entry("name", "name"),
				entry("birthday", "2025-12-29")
		);
		var json = gson.toJson(user);
		final HttpRequest.BodyPublisher body = HttpRequest.BodyPublishers.ofString(json);
		var request = HttpRequest.newBuilder()
				.uri(url)
				.header("content-type", "application/json")
				.POST(body)
				.build();
		try {
			HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
			assertNotNull(response);
			assertEquals(response.statusCode(), HttpStatus.BAD_REQUEST.value());
			assertEquals(response.body(), "{\"error\":\"birthday: Date of birth should be in the past\"}");
		} catch (IOException | InterruptedException e) {
			throw new RuntimeException(e.getMessage());
		}
	}
}
