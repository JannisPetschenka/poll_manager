package no.hvl.poll_manager.integration;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.time.Instant;
import java.util.List;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import no.hvl.poll_manager.model.Poll;
import no.hvl.poll_manager.model.User;
import no.hvl.poll_manager.model.Vote;

@SpringBootTest(webEnvironment = WebEnvironment.RANDOM_PORT)
public class Scenario1Test {

	@LocalServerPort
	private int port;

	@Autowired
	private TestRestTemplate restTemplate;

	User u1 = new User("u1", "e1");
	User u2 = new User("u2", "e2");
	Poll p1 = new Poll("ok", List.of(), Instant.now(), u1);

	@Test
	void createUser() {
		ResponseEntity<String> response = restTemplate.postForEntity(
				"/api/v1/users",
				u1,
				String.class);
		assertEquals(response.getStatusCode(), HttpStatus.OK);
	}

	@Test
	void listUsers() {
		ResponseEntity<User[]> response = restTemplate.getForEntity(
				"/api/v1/users",
				User[].class);
		assertEquals(response.getStatusCode(), HttpStatus.OK);
		assertEquals(response.getBody()[0], u1);
	}

	@Test
	void createSecondUser() {
		ResponseEntity<String> response = restTemplate.postForEntity(
				"/api/v1/users",
				u2,
				String.class);
		assertEquals(response.getStatusCode(), HttpStatus.OK);
	}

	@Test
	void listUsers2() {
		ResponseEntity<User[]> response = restTemplate.getForEntity(
				"/api/v1/users",
				User[].class);
		assertEquals(response.getStatusCode(), HttpStatus.OK);
		assertEquals(response.getBody()[0], u1);
		assertEquals(response.getBody()[1], u2);
	}

	@Test
	void createPoll() {
		ResponseEntity<String> response = restTemplate.postForEntity(
				"/api/v1/polls",
				p1,
				String.class);
		assertEquals(response.getStatusCode(), HttpStatus.OK);
	}

	@Test
	void listPolls() {
		ResponseEntity<Poll[]> response = restTemplate.getForEntity(
				"/api/v1/polls",
				Poll[].class);
		assertEquals(response.getStatusCode(), HttpStatus.OK);
		assertEquals(response.getBody()[0], p1);
	}

	@Test
	void vote() {
		ResponseEntity<String> response = restTemplate.postForEntity(
				"/api/v1/votes",
				null,
				String.class);
		assertEquals(response.getStatusCode(), HttpStatus.OK);
	}

	@Test
	void updateVote() {
		ResponseEntity<String> response = restTemplate.exchange(
				"/api/v1/votes",
				HttpMethod.PUT,
				null,
				String.class);
		assertEquals(response.getStatusCode(), HttpStatus.OK);
	}

	@Test
	void listVotes() {
		ResponseEntity<Vote[]> response = restTemplate.getForEntity(
				"/api/v1/votes",
				Vote[].class);
		assertEquals(response.getStatusCode(), HttpStatus.OK);
		assertEquals(response.getBody()[0], new Vote(u1, null));
	}

	@Test
	void deletePoll() {
		restTemplate.delete("/api/v1/polls");
		ResponseEntity<Poll[]> response = restTemplate.getForEntity("/api/v1/polls", Poll[].class);
		assertEquals(response.getStatusCode(), HttpStatus.OK);
		assertEquals(response.getBody().length, 0);
	}
}
