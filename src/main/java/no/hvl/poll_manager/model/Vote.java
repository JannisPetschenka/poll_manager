package no.hvl.poll_manager.model;

import java.time.Instant;

import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.JsonIdentityReference;
import com.fasterxml.jackson.annotation.ObjectIdGenerators;

import lombok.Data;

@JsonIdentityInfo(generator = ObjectIdGenerators.IntSequenceGenerator.class, property = "_id")
@Data
public class Vote {
	@JsonIdentityReference
	User voter;

	Instant publishedAt;

	VoteOption voteOption;

	public Vote(User voter, VoteOption voteOption) {
		this.voter = voter;
		this.voteOption = voteOption;
		this.publishedAt = Instant.now();
		this.voter.votesGiven.add(this);
	}
}
