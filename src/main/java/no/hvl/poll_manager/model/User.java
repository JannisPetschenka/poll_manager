package no.hvl.poll_manager.model;

import java.util.ArrayList;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.JsonIdentityReference;
import com.fasterxml.jackson.annotation.ObjectIdGenerators;

import lombok.Data;

@JsonIdentityInfo(generator = ObjectIdGenerators.PropertyGenerator.class, property = "username")
@Data
public class User {
	String username;
	String email;

	@JsonIdentityReference
	List<Poll> createdPolls;

	@JsonIdentityReference
	List<Vote> votesGiven;

	public User(String name, String email) {
		this.username = name;
		this.email = email;
		this.createdPolls = new ArrayList<>();
		this.votesGiven = new ArrayList<>();
	}
}
