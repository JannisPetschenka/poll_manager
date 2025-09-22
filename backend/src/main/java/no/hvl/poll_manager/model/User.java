package no.hvl.poll_manager.model;

import java.time.Instant;
import java.util.Collections;
import java.util.LinkedHashSet;
import java.util.Set;

import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.JsonIdentityReference;
import com.fasterxml.jackson.annotation.ObjectIdGenerators;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import lombok.Data;

@Entity
@Table(name = "users")
@JsonIdentityInfo(generator = ObjectIdGenerators.PropertyGenerator.class, property = "id")
@Data
public class User {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	Integer id;

	String username;

	String email;

	@OneToMany(cascade = CascadeType.ALL, fetch = FetchType.EAGER)
	@JsonIdentityReference(alwaysAsId = true)
	Set<Poll> created;

	@OneToMany(cascade = CascadeType.ALL, fetch = FetchType.EAGER)
	@JsonIdentityReference(alwaysAsId = true)
	Set<Vote> votesGiven;

	public User() {

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
		final int prime = 31;
		int result = 1;
		result = prime * result + ((id == null) ? 0 : id.hashCode());
		return result;
	}

	/**
	 * Creates a new User object with given username and email.
	 * The id of a new user object gets determined by the database.
	 */
	public User(String username, String email) {
		this.username = username;
		this.email = email;
		this.created = new LinkedHashSet<>();
		this.votesGiven = new LinkedHashSet<>();
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
