package no.hvl.poll_manager.model;

import java.time.Instant;
import java.util.HashMap;
import java.util.LinkedHashSet;
import java.util.Map;
import java.util.Set;

import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.JsonIdentityReference;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import com.fasterxml.jackson.annotation.ObjectIdGenerators;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;

@Entity
@Table(name = "polls")
@JsonIdentityInfo(generator = ObjectIdGenerators.IntSequenceGenerator.class, property = "id")
@Data
public class Poll {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	Integer id;

	@ManyToOne
	@JsonIdentityReference
	User createdBy;

	String question;

	Instant publishedAt;

	Instant validUntil;

	@OneToMany
	@JsonManagedReference
	Set<VoteOption> options;

	// Map<String, Integer> votes;

	public Poll() {
	}

	public Poll(Integer id, String question, Set<VoteOption> voteOptions, Instant validUntil, User creator) {
		this.id = id;
		this.question = question;
		this.options = new LinkedHashSet<>(voteOptions);
		this.validUntil = validUntil;
		this.createdBy = creator;
		this.publishedAt = Instant.now();
		this.createdBy.created.add(this);
		// this.votes = new HashMap<>();
	}

	public Poll(String question, Set<VoteOption> voteOptions, Instant validUntil, User creator) {
		this.question = question;
		this.options = new LinkedHashSet<>(voteOptions);
		this.validUntil = validUntil;
		this.createdBy = creator;
		this.publishedAt = Instant.now();
		this.createdBy.created.add(this);
		// this.votes = new HashMap<>();
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
		VoteOption voteOption = new VoteOption(caption, this);
		return voteOption;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((id == null) ? 0 : id.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Poll other = (Poll) obj;
		if (id == null) {
			if (other.id != null)
				return false;
		} else if (!id.equals(other.id))
			return false;
		return true;
	}
}
