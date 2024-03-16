package com.in28minutes.rest.webservices.restfulwebservices.user;

import java.net.URI;
import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import jakarta.validation.Valid;

@RestController
public class UserResource {
	
	private UserDaoService service;
	
	public UserResource(UserDaoService service) {
		this.service = service; 
	}
	
	@GetMapping("/users")
	public List<User> getAllUsers() {
		return service.findAll();
	}
	
	
	@GetMapping("/users/{id}")
	public User getAllUsers(@PathVariable Integer id) throws UserNotFoundException {
		User user = service.findById(id);
		
		if(user == null) {
			throw new UserNotFoundException("id"+id);
		}
		
		return user;
	}
	
	@DeleteMapping("/users/{id}")
	public void deleteUsers(@PathVariable Integer id) throws UserNotFoundException {
		service.deleteById(id);

	}
	
	@PostMapping("users")
	public ResponseEntity<User> save(@Valid @RequestBody User user){
		User savedUser = service.save(user);
		
		
				
		URI location = ServletUriComponentsBuilder.fromCurrentRequest().path("/{id}").buildAndExpand(savedUser.getId()).toUri();
		return ResponseEntity.created(location).build();
		
		
	}
}