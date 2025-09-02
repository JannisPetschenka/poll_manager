package no.hvl.poll_manager.integration;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.time.Instant;
import java.util.List;

import org.junit.jupiter.api.Test;
import no.hvl.poll_manager.controller.PollsController.PollRequest;
import no.hvl.poll_manager.controller.UsersController.UserRequest;
import no.hvl.poll_manager.controller.VotesController.VoteRequest;
import no.hvl.poll_manager.model.VoteOption;

public class Scenario1Test extends ScenarioTest {

	UserRequest u1 = new UserRequest("u1", "e1");
	UserRequest u2 = new UserRequest("u2", "e2");
	UserRequest u3 = new UserRequest("u3", "e3");
	VoteOption vo1 = new VoteOption("yes", 0);
	VoteOption vo2 = new VoteOption("no", 1);
	PollRequest p1 = new PollRequest(0, "ok", List.of(vo1, vo2), Instant.now());
	PollRequest p2 = new PollRequest(5, "ok", List.of(vo1, vo2), Instant.now());
	VoteRequest v1 = new VoteRequest(0, 0, vo1);

	@Test
	void scenario1() {
		createUser(u1, true);

		var response = getUsers();
		assertEquals(1, response.size());

		createUser(u1, false);

		response = getUsers();
		assertEquals(1, response.size());

		createUser(u2, true);

		response = getUsers();
		assertEquals(2, response.size());

		createUser(u3, true);
		response = getUsers();
		assertEquals(3, response.size());
		deleteUser(2);
		response = getUsers();
		assertEquals(2, response.size());

		createPoll(p1, true);

		response = getPolls();
		assertEquals(1, response.size());

		createPoll(p2, false);

		response = getVotesForPoll(0);
		assertEquals(0, response.size());
		createVote(v1, true);
		response = getVotesForPoll(0);
		assertEquals(1, response.size());

		deletePoll(0);

		response = getPolls();
		assertEquals(0, response.size());
		response = getVotesForPoll(0);
		assertEquals(0, response.size());
	}

	// @Test
	// void updateVote() {
	// ResponseEntity<String> response = restTemplate.exchange(
	// "/api/v1/votes",
	// HttpMethod.PUT,
	// null,
	// String.class);
	// assertEquals(HttpStatus.OK, response.getStatusCode());
	// }
}
