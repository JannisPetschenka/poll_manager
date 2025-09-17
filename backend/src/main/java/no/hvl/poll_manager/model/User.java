package no.hvl.poll_manager.model;

import java.time.Instant;
import java.util.ArrayList;
import java.util.Collections;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;

import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.JsonIdentityReference;
import com.fasterxml.jackson.annotation.ObjectIdGenerators;

import lombok.Data;

@JsonIdentityInfo(generator = ObjectIdGenerators.PropertyGenerator.class, property = "id")
@Data
public class User {
	Integer id;
	String username;
	String email;

	@JsonIdentityReference(alwaysAsId = true)
	Set<Poll> created;

	@JsonIdentityReference(alwaysAsId = true)
	Set<Vote> votesGiven;

	public User(Integer id, String name, String email) {
		this.id = id;
		this.username = name;
		this.email = email;
		this.created = new LinkedHashSet<>();
		this.votesGiven = new LinkedHashSet<>();
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null || getClass() != obj.getClass())
			return false;
		User user = (User) obj;
		return username.equals(user.username) || email.equals(user.email);
	}

	@Override
	public int hashCode() {
		return super.hashCode();
	}

	/**
	 * Creates a new User object with given username and email.
	 * The id of a new user object gets determined by the database.
	 */
	public User(String username, String email) {
		this.username = username;
		this.email = email;
		this.created = new LinkedHashSet<>();
	}

	/**
	 * Creates a new Poll object for this user
	 * with the given poll question
	 * and returns it.
	 */
	public Poll createPoll(String question) {
		Poll poll = new Poll(question, Collections.emptySet(), Instant.now(), this);
		return poll;
	}

	/**
	 * Creates a new Vote for a given VoteOption in a Poll
	 * and returns the Vote as an object.
	 */
	public Vote voteFor(VoteOption option) {
		Vote vote = new Vote(this, option);
		return vote;
	}
}
