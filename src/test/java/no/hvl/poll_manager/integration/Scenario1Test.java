package no.hvl.poll_manager.integration;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.time.Instant;
import java.util.List;

import org.junit.jupiter.api.Test;
import no.hvl.poll_manager.controller.PollsController.PollRequest;
import no.hvl.poll_manager.controller.UsersController.UserRequest;
import no.hvl.poll_manager.model.VoteOption;

public class Scenario1Test extends ScenarioTest {

	UserRequest u1 = new UserRequest("u1", "e1");
	UserRequest u2 = new UserRequest("u2", "e2");
	UserRequest u3 = new UserRequest("u3", "e3");
	VoteOption vo1 = new VoteOption("yes", 0);
	VoteOption vo2 = new VoteOption("no", 1);
	PollRequest p1 = new PollRequest(0, "ok", List.of(vo1, vo2), Instant.now());
	PollRequest p2 = new PollRequest(5, "ok", List.of(vo1, vo2), Instant.now());

	@Test
	void scenario1() {
		createUser(u1, true);

		var response1 = getUsers();
		assertEquals(1, response1.size());

		createUser(u1, false);

		var response2 = getUsers();
		assertEquals(1, response2.size());

		createUser(u2, true);

		var response3 = getUsers();
		assertEquals(2, response3.size());

		createUser(u3, true);
		var response3_delete = getUsers();
		assertEquals(3, response3_delete.size());
		deleteUser(2);
		response3_delete = getUsers();
		assertEquals(2, response3_delete.size());

		createPoll(p1, true);

		var response4 = getPolls();
		assertEquals(1, response4.size());

		createPoll(p2, false);

		deletePoll(0);

		var response5 = getPolls();
		assertEquals(0, response5.size());
	}

	// @Test
	// void vote() {
	// ResponseEntity<String> response = restTemplate.postForEntity(
	// "/api/v1/votes",
	// null,
	// String.class);
	// assertEquals(HttpStatus.OK, response.getStatusCode());
	// }
	//
	// @Test
	// void updateVote() {
	// ResponseEntity<String> response = restTemplate.exchange(
	// "/api/v1/votes",
	// HttpMethod.PUT,
	// null,
	// String.class);
	// assertEquals(HttpStatus.OK, response.getStatusCode());
	// }
	//
	// @Test
	// void listVotes() {
	// ResponseEntity<Vote[]> response = restTemplate.getForEntity(
	// "/api/v1/votes",
	// Vote[].class);
	// assertEquals(HttpStatus.OK, response.getStatusCode());
	// assertEquals(new Vote(u1, p1, null), response.getBody()[0]);
	// }
	//
	// @Test
	// void deletePoll() {
	// restTemplate.delete("/api/v1/polls");
	// ResponseEntity<Poll[]> response = restTemplate.getForEntity("/api/v1/polls",
	// Poll[].class);
	// assertEquals(HttpStatus.OK, response.getStatusCode());
	// assertEquals(0, response.getBody().length);
	// }
}
