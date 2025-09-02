package no.hvl.poll_manager.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.RequestBody;

import no.hvl.poll_manager.model.Vote;
import no.hvl.poll_manager.model.VoteOption;
import no.hvl.poll_manager.repository.PollManager;

@RestController
@RequestMapping("/api/v1")
public class VotesController {

	@Autowired
	private PollManager pollManager;

	@GetMapping("/votes/{id}")
	public ResponseEntity<List<Vote>> getVotes(@PathVariable("id") int id) {
		return ResponseEntity.ok(pollManager.getVotesForPoll(id));
	}

	@PostMapping(value = "/votes", consumes = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<Vote> createVote(@RequestBody VoteRequest voteRequest) {
		return pollManager.addVoteForPoll(voteRequest).map(v -> ResponseEntity.ok(v))
				.orElse(ResponseEntity.status(HttpStatus.CONFLICT).build());
	}

	public static record VoteRequest(Integer creatorId, Integer pollId, VoteOption voteOption) {
	}

}
