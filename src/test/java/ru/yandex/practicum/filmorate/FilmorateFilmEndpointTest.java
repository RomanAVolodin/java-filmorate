package ru.yandex.practicum.filmorate;

import com.google.gson.Gson;
import lombok.extern.slf4j.Slf4j;
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

@Slf4j
@DisplayName("Тестирование ручки Фильмы")
class FilmorateFilmEndpointTest {
	private final String baseUrlString = "http://localhost:8080/films";
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
	@DisplayName("Проверка корректного добавления фильма")
	void checkFilmCreation() {
		URI url = URI.create(baseUrlString);
		Map<String, String> film =  Map.ofEntries(
				entry("name", "name"),
				entry("description", "description"),
				entry("releaseDate", "2022-12-29"),
				entry("duration", "PT2H")
		);
		var json = gson.toJson(film);
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
	@DisplayName("Проверка добавления фильма без названия")
	void checkFilmNoTitleCreation() {
		URI url = URI.create(baseUrlString);
		Map<String, String> film =  Map.ofEntries(
				entry("name", ""),
				entry("description", "description"),
				entry("releaseDate", "2022-12-29"),
				entry("duration", "PT2H")
		);
		var json = gson.toJson(film);
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
			assertEquals(response.body(), "{\"name\":\"Film title must not be blank\"}");
		} catch (IOException | InterruptedException e) {
			throw new RuntimeException(e.getMessage());
		}
	}

	@Test
	@DisplayName("Проверка добавления фильма с длинным описанием")
	void checkFilmWithLongDescriptionCreation() {
		URI url = URI.create(baseUrlString);
		Map<String, String> film =  Map.ofEntries(
				entry("name", "name"),
				entry("description", "description description description description description description " +
						"description description description description description description description " +
						"description description description description description description description " +
						"description description description description description description description " +
						"description description description description description description description " +
						"description description description description description description "),
				entry("releaseDate", "2022-12-29"),
				entry("duration", "PT2H")
		);
		var json = gson.toJson(film);
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
			assertEquals(response.body(), "{\"description\":\"Description should be less than 200 symbols\"}");
		} catch (IOException | InterruptedException e) {
			throw new RuntimeException(e.getMessage());
		}
	}

	@Test
	@DisplayName("Проверка добавления фильма с датой релиза раньше допустимой")
	void checkFilmWithIncorrectReleaseDateCreation() {
		URI url = URI.create(baseUrlString);
		Map<String, String> film =  Map.ofEntries(
				entry("name", "name"),
				entry("description", "description"),
				entry("releaseDate", "1894-12-29"),
				entry("duration", "PT2H")
		);
		var json = gson.toJson(film);
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
			assertEquals(response.body(), "{\"releaseDate\":\"Release date should be greater than 28.12.1895\"}");
		} catch (IOException | InterruptedException e) {
			throw new RuntimeException(e.getMessage());
		}
	}

	@Test
	@DisplayName("Проверка добавления фильма с отрицательной продолжительностью")
	void checkFilmWithNegativeDuration() {
		URI url = URI.create(baseUrlString);
		Map<String, String> film =  Map.ofEntries(
				entry("name", "name"),
				entry("description", "description"),
				entry("releaseDate", "2021-12-29"),
				entry("duration", "-PT2H")
		);
		var json = gson.toJson(film);
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
			assertEquals(response.body(), "{\"duration\":\"Duration must be positive\"}");
		} catch (IOException | InterruptedException e) {
			throw new RuntimeException(e.getMessage());
		}
	}

	@Test
	@DisplayName("Проверка добавления фильма без тела запроса")
	void checkFilmWithoutBody() {
		URI url = URI.create(baseUrlString);
		final HttpRequest.BodyPublisher body = HttpRequest.BodyPublishers.ofString("");
		var request = HttpRequest.newBuilder()
				.uri(url)
				.header("content-type", "application/json")
				.POST(body)
				.build();
		try {
			HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
			assertNotNull(response);
			assertEquals(response.statusCode(), HttpStatus.BAD_REQUEST.value());
		} catch (IOException | InterruptedException e) {
			throw new RuntimeException(e.getMessage());
		}
	}
}
