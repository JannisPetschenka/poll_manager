package no.hvl.poll_manager.controller;

import java.util.List;

import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import no.hvl.poll_manager.model.User;

@RestController
@RequestMapping("/api/v1/users")
public class UsersController {

	@GetMapping()
	public ResponseEntity<List<User>> getUsers() {
		return ResponseEntity.ok(List.of(new User("u1", "e1")));
	}

	@PostMapping(consumes = { MediaType.APPLICATION_JSON_VALUE,
			MediaType.APPLICATION_XML_VALUE }, produces = { MediaType.APPLICATION_JSON_VALUE,
					MediaType.APPLICATION_XML_VALUE })
	public ResponseEntity<Void> createUser(@RequestBody User user) {
		System.out.println(user);
		return ResponseEntity.ok().build();
	}

	@PutMapping()
	public ResponseEntity<Void> updateUser() {
		return ResponseEntity.ok().build();
	}

	@DeleteMapping()
	public String deleteUser() {
		return "DONE";
	}
}
