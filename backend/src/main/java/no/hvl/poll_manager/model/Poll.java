package no.hvl.poll_manager.model;

import java.time.Instant;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.JsonIdentityReference;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import com.fasterxml.jackson.annotation.ObjectIdGenerators;

import lombok.Data;

@JsonIdentityInfo(generator = ObjectIdGenerators.IntSequenceGenerator.class, property = "id")
@Data
public class Poll {
	Integer id;
	@JsonIdentityReference
	User creator;

	String question;

	Instant publishedAt;

	Instant validUntil;

	@JsonManagedReference
	Set<VoteOption> voteOptions;

	Map<String, Integer> votes;

	public Poll(Integer id, String question, Set<VoteOption> voteOptions, Instant validUntil, User creator) {
		this.id = id;
		this.question = question;
		this.voteOptions = voteOptions;
		this.validUntil = validUntil;
		this.creator = creator;
		this.publishedAt = Instant.now();
		this.creator.created.add(this);
		this.votes = new HashMap<>();
	}

	public Poll(String question, Set<VoteOption> voteOptions, Instant validUntil, User creator) {
		this.question = question;
		this.voteOptions = voteOptions;
		this.validUntil = validUntil;
		this.creator = creator;
		this.publishedAt = Instant.now();
		this.creator.created.add(this);
		this.votes = new HashMap<>();
	}

	/**
	 *
	 * Adds a new option to this Poll and returns the respective
	 * VoteOption object with the given caption.
	 * The value of the presentationOrder field gets determined
	 * by the size of the currently existing VoteOptions for this Poll.
	 * I.e. the first added VoteOption has presentationOrder=0, the secondly
	 * registered VoteOption has presentationOrder=1 ans so on.
	 */
	public VoteOption addVoteOption(String caption) {
		VoteOption voteOption = new VoteOption(caption, this.voteOptions.size() - 1);
		this.voteOptions.add(voteOption);
		return voteOption;
	}
}
