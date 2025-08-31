package no.hvl.poll_manager.repository;

import java.util.HashMap;

import org.springframework.stereotype.Component;

import no.hvl.poll_manager.model.Poll;
import no.hvl.poll_manager.model.User;
import no.hvl.poll_manager.model.Vote;

@Component
public class PollManager {
	HashMap<Integer, User> users;
	HashMap<Integer, Poll> polls;
	HashMap<Integer, Vote> votes;

	public int addUser(User user) {
		if (users.containsValue(user)) {
			return -1;
		}
		users.put(1, user);
		return 1;
	}
}
