package no.hvl.poll_manager.model;

import java.util.ArrayList;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.JsonIdentityReference;
import com.fasterxml.jackson.annotation.ObjectIdGenerators;

import lombok.Data;

@JsonIdentityInfo(generator = ObjectIdGenerators.PropertyGenerator.class, property = "id")
@Data
public class User {
	Integer id;
	String username;
	String email;

	@JsonIdentityReference
	List<Poll> createdPolls;

	@JsonIdentityReference
	List<Vote> votesGiven;

	public User(Integer id, String name, String email) {
		this.id = id;
		this.username = name;
		this.email = email;
		this.createdPolls = new ArrayList<>();
		this.votesGiven = new ArrayList<>();
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
		return super.hashCode();
	}
}
