package no.hvl.poll_manager.model;

import java.time.Instant;

public class Poll {
	User creator;
	String question;
	Instant publishedAt;
	Instant validUntil;
	VoteOption votes;
}
