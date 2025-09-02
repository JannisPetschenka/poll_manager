package no.hvl.poll_manager.repository;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import org.springframework.stereotype.Component;

import no.hvl.poll_manager.controller.PollsController.PollRequest;
import no.hvl.poll_manager.controller.VotesController.VoteRequest;
import no.hvl.poll_manager.model.Poll;
import no.hvl.poll_manager.model.User;
import no.hvl.poll_manager.model.Vote;

@Component
public class PollManager {
	List<User> users;
	List<Poll> polls;
	Map<Integer, List<Vote>> votes;

	public PollManager() {
		this.users = new ArrayList<>();
		this.polls = new ArrayList<>();
		this.votes = new HashMap<>();
	}

	public Optional<User> addUser(String username, String email) {
		User tmp = new User(users.size(), username, email);
		if (users.contains(tmp)) {
			return Optional.empty();
		}
		users.add(tmp);
		return Optional.of(tmp);
	}

	public List<User> getUsers() {
		return users;
	}

	public void deleteUser(int id) {
		users = users.stream().filter(user -> user.getId() != id).toList();
	}

	public Optional<Poll> addPoll(PollRequest poll) {
		Optional<User> creator = users.stream().filter(user -> user.getId() == poll.creatorId()).findFirst();
		if (creator.isPresent()) {
			votes.put(polls.size(), new ArrayList<Vote>());
			Poll tmp = new Poll(polls.size(), poll.question(), poll.voteOptions(), poll.validUntil(), creator.get());
			polls.add(tmp);
			return Optional.of(tmp);
		}
		return Optional.empty();
	}

	public List<Poll> getPolls() {
		return polls;
	}

	public void deletePoll(int id) {
		polls = polls.stream().filter(poll -> poll.getId() != id).toList();
		votes.remove(id);
	}

	public List<Vote> getVotesForPoll(int id) {
		if (votes.containsKey(id)) {
			return votes.get(id);
		}
		return Collections.emptyList();
	}

	public Optional<Vote> addVoteForPoll(VoteRequest vote) {
		Optional<User> creator = users.stream().filter(user -> user.getId() == vote.creatorId()).findFirst();
		if (creator.isPresent() && votes.containsKey(vote.pollId())) {
			Vote tmp = new Vote(creator.get(), vote.voteOption());
			votes.get(vote.pollId()).add(tmp);
			return Optional.of(tmp);
		}
		return Optional.empty();
	}
}
