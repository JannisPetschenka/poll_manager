package no.hvl.poll_manager.model;

import java.time.Instant;

import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.JsonIdentityReference;
import com.fasterxml.jackson.annotation.ObjectIdGenerators;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.Data;

@Entity
@Table(name = "votes")
@JsonIdentityInfo(generator = ObjectIdGenerators.IntSequenceGenerator.class, property = "_id")
@Data
public class Vote {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	Integer id;

	@ManyToOne
	@JsonIdentityReference
	User voter;

	Instant publishedAt;

	@ManyToOne
	VoteOption votesOn;

	public Vote(User voter, VoteOption voteOption) {
		this.voter = voter;
		this.votesOn = voteOption;
		this.publishedAt = Instant.now();
		this.voter.votesGiven.add(this);
	}
}
