package no.hvl.poll_manager.model;

import java.util.Collection;
import java.util.Collections;

import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.JsonIdentityReference;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import com.fasterxml.jackson.annotation.ObjectIdGenerators;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.Data;

@Entity
@Table(name = "voteOptions")
@JsonIdentityInfo(generator = ObjectIdGenerators.IntSequenceGenerator.class, property = "id")
@Data
public class VoteOption {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	Integer id;

	String caption;

	Integer presentationOrder;

	@ManyToOne
	@JoinColumn(name = "poll_id")
	@JsonIdentityReference(alwaysAsId = true)
	Poll poll;

	public VoteOption(String caption, Integer presentationOrder) {
		this.caption = caption;
		this.presentationOrder = presentationOrder;
	}

	//
	public VoteOption(String caption, Poll poll) {
		this.caption = caption;
		this.presentationOrder = poll.options.size();
		poll.options.add(this);
		this.poll = poll;
	}

	public VoteOption() {
		this.caption = "";
		this.presentationOrder = 0;
	}
}
