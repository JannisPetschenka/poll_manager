package no.hvl.poll_manager.integration;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import no.hvl.poll_manager.controller.PollsController.PollRequest;
import no.hvl.poll_manager.controller.UsersController.UserRequest;

@SpringBootTest(webEnvironment = WebEnvironment.RANDOM_PORT)
public class ScenarioTest {

	@LocalServerPort
	private int port;

	@Autowired
	TestRestTemplate restTemplate;

	void createUser(UserRequest user, boolean shouldSucceed) {
		ResponseEntity<Void> response = restTemplate.postForEntity(
				"/api/v1/users",
				user, Void.class);
		if (shouldSucceed) {
			assertEquals(HttpStatus.OK, response.getStatusCode());
		} else {
			assertEquals(HttpStatus.CONFLICT, response.getStatusCode());
		}
	}

	List getUsers() {
		ResponseEntity<List> response = restTemplate.getForEntity(
				"/api/v1/users",
				List.class);
		assertEquals(HttpStatus.OK, response.getStatusCode());
		return response.getBody();
	}

	void deleteUser(int id) {
		restTemplate.delete("/api/v1/user/" + id);
	}

	void createPoll(PollRequest poll, boolean shouldSucceed) {
		ResponseEntity<String> response = restTemplate.postForEntity(
				"/api/v1/polls",
				poll,
				String.class);
		if (shouldSucceed) {
			assertEquals(HttpStatus.OK, response.getStatusCode());
		} else {
			assertEquals(HttpStatus.CONFLICT, response.getStatusCode());
		}
	}

	List getPolls() {
		ResponseEntity<List> response = restTemplate.getForEntity(
				"/api/v1/polls",
				List.class);
		assertEquals(HttpStatus.OK, response.getStatusCode());
		return response.getBody();
	}

	void deletePoll(int id) {
		restTemplate.delete("/api/v1/poll/" + id);
	}
}
