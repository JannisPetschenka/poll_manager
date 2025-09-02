package no.hvl.poll_manager.model;

import java.time.Instant;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.JsonIdentityReference;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import com.fasterxml.jackson.annotation.ObjectIdGenerators;

import lombok.Data;

@JsonIdentityInfo(generator = ObjectIdGenerators.IntSequenceGenerator.class, property = "_id")
@Data
public class Poll {
	Integer id;
	@JsonIdentityReference
	User creator;

	String question;

	Instant publishedAt;

	Instant validUntil;

	@JsonManagedReference
	List<VoteOption> voteOptions;

	Set<Vote> votes;

	public Poll(Integer id, String question, List<VoteOption> voteOptions, Instant validUntil, User creator) {
		this.id = id;
		this.question = question;
		this.voteOptions = voteOptions;
		this.validUntil = validUntil;
		this.creator = creator;
		this.publishedAt = Instant.now();
		this.votes = new HashSet<>();
		this.creator.createdPolls.add(this);
	}
}
