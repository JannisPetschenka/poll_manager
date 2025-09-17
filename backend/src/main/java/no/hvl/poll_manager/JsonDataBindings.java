package no.hvl.poll_manager;

import java.io.File;
import java.io.IOException;
import java.time.Instant;
import java.util.List;
import java.util.Set;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;

import no.hvl.poll_manager.model.Poll;
import no.hvl.poll_manager.model.User;
import no.hvl.poll_manager.model.Vote;
import no.hvl.poll_manager.model.VoteOption;

public class JsonDataBindings {

	private static ObjectMapper initJackson() {
		ObjectMapper mapper = new ObjectMapper();
		mapper.enable(SerializationFeature.INDENT_OUTPUT);
		return mapper;
	}

	public static void buildJson() {
		User user = new User(1, "hi", "mail");
		User user1 = new User(0, "hello", "there");
		VoteOption yes = new VoteOption("Yep", 0);
		VoteOption no = new VoteOption("Nope", 1);
		Poll poll = new Poll(0, "Is ok?", Set.of(yes, no), Instant.now(), user);
		Vote u1p1 = new Vote(user1, yes);

		ObjectMapper mapper = initJackson();

		try {
			mapper.writeValue(new File("test.json"), user1);
		} catch (IOException e) {
			throw new RuntimeException();
		}
	}
}
