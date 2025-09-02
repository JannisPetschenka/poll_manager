package no.hvl.poll_manager.controller;

import java.time.Instant;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import no.hvl.poll_manager.model.Poll;
import no.hvl.poll_manager.model.VoteOption;
import no.hvl.poll_manager.repository.PollManager;

@RestController
@RequestMapping("/api/v1")
public class PollsController {

	@Autowired
	PollManager pollManager;

	@GetMapping("/polls")
	public ResponseEntity<List<Poll>> getPolls() {
		return ResponseEntity.ok(pollManager.getPolls());
	}

	@PostMapping(value = "/polls", consumes = { MediaType.APPLICATION_JSON_VALUE,
			MediaType.APPLICATION_XML_VALUE }, produces = { MediaType.APPLICATION_JSON_VALUE,
					MediaType.APPLICATION_XML_VALUE })
	public ResponseEntity<Poll> createPoll(@RequestBody PollRequest poll) {
		return pollManager.addPoll(poll).map(p -> ResponseEntity.ok(p))
				.orElse(ResponseEntity.status(HttpStatus.CONFLICT).build());
	}

	@PutMapping()
	public ResponseEntity<Void> updatePoll() {
		return ResponseEntity.ok().build();
	}

	@DeleteMapping("/poll/{id}")
	public ResponseEntity<Void> deletePoll(@PathVariable(name = "id") int id) {
		pollManager.deletePoll(id);
		return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
	}

	public static record PollRequest(Integer creatorId, String question, List<VoteOption> voteOptions,
			Instant validUntil) {
	}
}
