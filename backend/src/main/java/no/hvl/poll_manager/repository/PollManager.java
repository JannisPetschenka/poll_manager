package no.hvl.poll_manager.repository;

import java.time.Instant;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.stereotype.Component;

import no.hvl.poll_manager.controller.PollsController.PollRequest;
import no.hvl.poll_manager.controller.UsersController.UserRequest;
import no.hvl.poll_manager.controller.VotesController.VoteRequest;
import no.hvl.poll_manager.model.Poll;
import no.hvl.poll_manager.model.User;
import no.hvl.poll_manager.model.Vote;
import no.hvl.poll_manager.model.VoteOption;

@Component
public class PollManager {
	List<User> users;
	List<Poll> polls;
	Map<Integer, List<Vote>> votes;

	public PollManager() {
		this.users = new ArrayList<>();
		this.polls = new ArrayList<>();
		this.votes = new HashMap<>();
		this.addUser("admin", "admin@hvl.no");
		this.addUser("u1", "u1@hvl.no");
		this.addUser("u2", "u2@hvl.no");
		this.addPoll(new PollRequest(0, "Agree?", List.of(new VoteOption("yes", 0),
				new VoteOption("no", 1)),
				Instant.now()));
		this.addPoll(new PollRequest(0, "Again?", List.of(new VoteOption("yes", 0),
				new VoteOption("no", 1)),
				Instant.now()));
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

	public Optional<User> updateUser(int id, UserRequest user) {
		for (User u : users) {
			if (u.getId() == id) {
				if (user.username() != null)
					u.setUsername(user.username());
				if (user.email() != null)
					u.setEmail(user.email());
				return Optional.of(u);
			}
		}
		return Optional.empty();
	}

	public void deleteUser(int id) {
		users = users.stream().filter(user -> user.getId() != id).toList();
	}

	public Optional<Poll> addPoll(PollRequest poll) {
		Optional<User> creator = users.stream().filter(user -> user.getId() == poll.creatorId()).findFirst();
		if (creator.isPresent()) {
			if (poll.voteOptions().size() > 2) {
				return Optional.empty();
			}
			votes.put(polls.size(), new ArrayList<Vote>());
			Poll tmp = new Poll(polls.size(), poll.question(), poll.voteOptions(), poll.validUntil(), creator.get());
			polls.add(tmp);
			return Optional.of(tmp);
		}
		return Optional.empty();
	}

	public List<Poll> getPolls() {
		for (var poll : this.polls) {
			List<Vote> votes = getVotesForPoll(poll.getId());
			poll.setVotes(new HashMap<>());
			for (var vote : votes) {
				var caption = vote.getVoteOption().getCaption();
				if (poll.getVotes().containsKey(caption)) {
					poll.getVotes().replace(caption, poll.getVotes().get(caption) + 1);
				} else {
					poll.getVotes().put(caption, 1);
				}
			}
		}
		return polls;
	}

	public Optional<Poll> updatePoll(int id, PollRequest poll) {
		for (Poll p : polls) {
			if (p.getId() == id) {
				if (poll.question() != null)
					p.setQuestion(poll.question());
				if (poll.validUntil() != null)
					p.setValidUntil(poll.validUntil());
				if (poll.voteOptions() != null)
					p.setVoteOptions(poll.voteOptions());
				return Optional.of(p);
			}
		}
		return Optional.empty();
	}

	public void deletePoll(int id) {
		polls = polls.stream().filter(poll -> poll.getId() != id).collect(Collectors.toCollection(ArrayList::new));
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
