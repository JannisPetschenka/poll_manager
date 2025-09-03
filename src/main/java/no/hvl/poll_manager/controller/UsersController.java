package no.hvl.poll_manager.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import no.hvl.poll_manager.model.User;
import no.hvl.poll_manager.repository.PollManager;

@RestController
@RequestMapping("/api/v1")
public class UsersController {

	@Autowired
	PollManager pollManager;

	@GetMapping("/users")
	public ResponseEntity<List<User>> getUsers() {
		return ResponseEntity.ok(pollManager.getUsers());
	}

	@PostMapping(value = "/users", consumes = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<User> createUser(@RequestBody UserRequest user) {
		return pollManager.addUser(user.username, user.email).map(u -> ResponseEntity.ok(u))
				.orElse(ResponseEntity.status(HttpStatus.CONFLICT).build());
	}

	@PutMapping("/user/{id}")
	public ResponseEntity<User> updateUser(@PathVariable(name = "id") int id, @RequestBody UserRequest user) {
		return pollManager.updateUser(id, user).map(u -> ResponseEntity.ok(u))
				.orElse(ResponseEntity.status(HttpStatus.CONFLICT).build());
	}

	@DeleteMapping("/user/{id}")
	public ResponseEntity<Void> deleteUser(@PathVariable(name = "id") int id) {
		pollManager.deleteUser(id);
		return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
	}

	public static record UserRequest(String username, String email) {
	}
}
