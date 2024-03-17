package com.in28minutes.rest.webservices.restfulwebservices.user;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

import java.net.URI;
import java.util.List;
import java.util.Optional;
import java.util.function.Predicate;

import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.server.mvc.WebMvcLinkBuilder;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import com.in28minutes.rest.webservices.restfulwebservices.jpa.PostRepository;
import com.in28minutes.rest.webservices.restfulwebservices.jpa.UserRepository;

import jakarta.validation.Valid;

@RestController
public class UserJpaResource {

	private UserDaoService service;

	private UserRepository repository;

	private PostRepository postRepository;

	public UserJpaResource(UserDaoService service, UserRepository repository,
			PostRepository postRepository) {
		this.service = service;
		this.repository = repository;
		this.postRepository = postRepository;
	}

	@GetMapping("/jpa/users")
	public List<User> getAllUsers() {
		return repository.findAll();
	}

	@GetMapping("/jpa/users/{id}")
	public EntityModel<User> getAllUsers(@PathVariable Integer id) throws UserNotFoundException {
		Optional<User> user = repository.findById(id);

		if (user.isEmpty()) {
			throw new UserNotFoundException("id:" + id);
		}

		EntityModel<User> entityModel = EntityModel.of(user.get());

		WebMvcLinkBuilder link = linkTo(methodOn(this.getClass()).getAllUsers());
		entityModel.add(link.withRel("all-userstest"));

		return entityModel;
	}

	@DeleteMapping("/jpa/users/{id}")
	public void deleteUsers(@PathVariable Integer id) throws UserNotFoundException {
		repository.deleteById(id);

	}

	@GetMapping("/jpa/users/{id}/posts")
	public List<Post> retrievePostForUser(@PathVariable Integer id) throws UserNotFoundException {
		Optional<User> user = repository.findById(id);

		if (user.isEmpty()) {
			throw new UserNotFoundException("id:" + id);
		}

		return user.get().getPosts();

	}

	@PostMapping("/jpa/users")
	public ResponseEntity<User> save(@Valid @RequestBody User user) {
		User savedUser = repository.save(user);

		URI location = ServletUriComponentsBuilder.fromCurrentRequest().path("/{id}").buildAndExpand(savedUser.getId())
				.toUri();
		return ResponseEntity.created(location).build();

	}
	
	@PostMapping("/jpa/users/{id}/posts")
	public ResponseEntity<User> createPost(@PathVariable int id, @Valid @RequestBody Post post) {
		Optional<User> user = repository.findById(id);

		if (user.isEmpty()) {
			throw new UserNotFoundException("id:" + id);
		}
		post.setUser(user.get());
		
		Post savedPost = postRepository.save(post);
				
		
		URI location = ServletUriComponentsBuilder
				.fromCurrentRequest()
				.path("/{id}")
				.buildAndExpand(savedPost.getId())
				.toUri();
		return ResponseEntity.created(location).build();

	}
	
	@GetMapping("/jpa/users/{userId}/posts/{postId}")
	public EntityModel<Post> getPostById(@PathVariable int postId, @PathVariable int userId) {
		Optional<User> user = repository.findById(userId);

		if (user.isEmpty()) {
			throw new UserNotFoundException("id:" + userId);
		}

		List<Post> posts = user.get().getPosts();
		
		
		Predicate<? super Post> predicate = post -> post.getId().equals(postId);
//		return users.stream().filter(predicate).findFirst().orElse(null);
		Optional<Post> post =posts.stream().filter(predicate).findFirst();
		

		if (post.isEmpty()) {
			throw new UserNotFoundException("postId:" + postId);
		}

		EntityModel<Post> entityModel = EntityModel.of(post.get());

		WebMvcLinkBuilder link = linkTo(methodOn(this.getClass()).getAllUsers());
		entityModel.add(link.withRel("all-userstest"));

		return entityModel;

	}
}