package no.hvl.poll_manager.model;

import java.time.Instant;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.JsonIdentityReference;
import com.fasterxml.jackson.annotation.ObjectIdGenerators;

import lombok.Data;

@JsonIdentityInfo(generator = ObjectIdGenerators.IntSequenceGenerator.class, property = "_id")
@Data
public class Poll {
	@JsonIdentityReference
	User creator;

	String question;

	Instant publishedAt;

	Instant validUntil;

	List<VoteOption> voteOptions;

	public Poll(String question, List<VoteOption> voteOptions, Instant validUntil, User creator) {
		this.question = question;
		this.voteOptions = voteOptions;
		this.validUntil = validUntil;
		this.creator = creator;
		this.publishedAt = Instant.now();
		this.creator.createdPolls.add(this);
	}
}
