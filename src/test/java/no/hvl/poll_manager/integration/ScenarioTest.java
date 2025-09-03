package no.hvl.poll_manager.integration;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import no.hvl.poll_manager.controller.PollsController.PollRequest;
import no.hvl.poll_manager.controller.UsersController.UserRequest;
import no.hvl.poll_manager.controller.VotesController.VoteRequest;
import no.hvl.poll_manager.model.User;

@SpringBootTest(webEnvironment = WebEnvironment.RANDOM_PORT)
public class ScenarioTest {

	@LocalServerPort
	private int port;

	@Autowired
	TestRestTemplate restTemplate;

	void createUser(UserRequest user, boolean shouldSucceed) {
		ResponseEntity<String> response = restTemplate.postForEntity(
				"/api/v1/users",
				user, String.class);
		if (shouldSucceed) {
			assertEquals(HttpStatus.OK, response.getStatusCode());
			assertTrue(response.getBody().contains(user.username()));
			assertTrue(response.getBody().contains(user.email()));
		} else {
			assertEquals(HttpStatus.CONFLICT, response.getStatusCode());
		}
	}

	void updateUser(int id, UserRequest user, boolean shouldSucceed) {
		HttpEntity<UserRequest> entity = new HttpEntity<UserRequest>(user);
		ResponseEntity<String> response = restTemplate.exchange(
				"/api/v1/user/" + id,
				HttpMethod.PUT,
				entity,
				String.class);
		if (shouldSucceed) {
			assertEquals(HttpStatus.OK, response.getStatusCode());
			if (user.username() != null)
				assertTrue(response.getBody().contains(user.username()));
			if (user.email() != null)
				assertTrue(response.getBody().contains(user.email()));
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
			assertTrue(response.getBody().contains(poll.question()));
			assertTrue(response.getBody().contains(poll.validUntil().toString()));
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

	void updatePoll(int id, PollRequest poll, boolean shouldSucceed) {
		HttpEntity<PollRequest> entity = new HttpEntity<PollRequest>(poll);
		ResponseEntity<String> response = restTemplate.exchange(
				"/api/v1/poll/" + id,
				HttpMethod.PUT,
				entity,
				String.class);
		if (shouldSucceed) {
			assertEquals(HttpStatus.OK, response.getStatusCode());
			if (poll.question() != null)
				assertTrue(response.getBody().contains(poll.question()));
			if (poll.validUntil() != null)
				assertTrue(response.getBody().contains(poll.validUntil().toString()));
		} else {
			assertEquals(HttpStatus.CONFLICT, response.getStatusCode());
		}
	}

	void createVote(VoteRequest vote, boolean shouldSucceed) {
		ResponseEntity<String> response = restTemplate.postForEntity(
				"/api/v1/votes",
				vote,
				String.class);
		if (shouldSucceed) {
			assertEquals(HttpStatus.OK, response.getStatusCode());
		} else {
			assertEquals(HttpStatus.CONFLICT, response.getStatusCode());
		}

	}

	List getVotesForPoll(int id) {
		ResponseEntity<List> response = restTemplate.getForEntity("/api/v1/votes/" + id, List.class);
		assertEquals(HttpStatus.OK, response.getStatusCode());
		return response.getBody();
	}
}
