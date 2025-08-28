package no.hvl.poll_manager.controller;

import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/user")
public class UserController {
	@GetMapping()
	public String getUser() {
		return "Hello there";
	}

	@PostMapping()
	public String createUser() {
		return "DONE";
	}

	@PutMapping()
	public String putUser() {
		return "DONE";
	}

	@DeleteMapping()
	public String deleteUser() {
		return "DONE";
	}
}
