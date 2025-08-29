package no.hvl.poll_manager.repository;

import java.util.HashMap;

import org.springframework.stereotype.Component;

import no.hvl.poll_manager.model.Poll;
import no.hvl.poll_manager.model.User;

@Component
public class PollManager {
	HashMap<User, Poll> userPoll;
}
