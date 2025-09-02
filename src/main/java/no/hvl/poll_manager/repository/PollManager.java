package no.hvl.poll_manager.repository;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.springframework.stereotype.Component;

import no.hvl.poll_manager.controller.PollsController.PollRequest;
import no.hvl.poll_manager.model.Poll;
import no.hvl.poll_manager.model.User;
import no.hvl.poll_manager.model.Vote;

@Component
public class PollManager {
	List<User> users;
	List<Poll> polls;
	List<Vote> votes;

	public PollManager() {
		this.users = new ArrayList<>();
		this.polls = new ArrayList<>();
		this.votes = new ArrayList<>();
	}

	public boolean addUser(String username, String email) {
		User tmp = new User(users.size(), username, email);
		if (users.contains(tmp)) {
			return false;
		}
		users.add(tmp);
		return true;
	}

	public List<User> getUsers() {
		return users;
	}

	public void deleteUser(int id) {
		users = users.stream().filter(user -> user.getId() != id).toList();
	}

	public boolean addPoll(PollRequest poll) {
		Optional<User> creator = users.stream().filter(user -> user.getId() == poll.creatorId()).findFirst();
		if (creator.isPresent()) {
			polls.add(new Poll(polls.size(), poll.question(), poll.voteOptions(), poll.validUntil(), creator.get()));
			return true;
		}
		return false;
	}

	public List<Poll> getPolls() {
		return polls;
	}

	public void deletePoll(int id) {
		polls = polls.stream().filter(poll -> poll.getId() != id).toList();
	}
}
