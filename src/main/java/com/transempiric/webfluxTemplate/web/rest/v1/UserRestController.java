package com.transempiric.webfluxTemplate.web.rest.v1;

import com.transempiric.webfluxTemplate.error.UserServiceException;
import com.transempiric.webfluxTemplate.model.User;
import com.transempiric.webfluxTemplate.mongo.repository.UserReactiveCrudRepository;
import org.bson.types.ObjectId;
import org.reactivestreams.Publisher;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import java.net.URI;
import java.util.List;

import static java.lang.String.format;
import static org.springframework.http.MediaType.APPLICATION_JSON_UTF8_VALUE;
import static org.springframework.http.ResponseEntity.*;
import static org.springframework.web.bind.annotation.RequestMethod.*;

@RestController
@RequestMapping(path = "/api/rest/user", produces = { APPLICATION_JSON_UTF8_VALUE })
public class UserRestController {

	private UserReactiveCrudRepository repo;
	public UserRestController(UserReactiveCrudRepository repo) {
		this.repo = repo;
	};

	/**
	 * Query for all users.
	 * <p>
	 * This method is idempotent.
	 * 
	 * @return HTTP 200 if users found or HTTP 204 otherwise.
	 */
	//@PreAuthorize("hasRole('ADMIN')")
	@RequestMapping(method = GET)
	public Mono<ResponseEntity<List<User>>> allUsers() {

		return repo.findAll().collectList()
			.filter(users -> users.size() > 0)
			.map(users -> ok(users))
			.defaultIfEmpty(noContent().build());
	}

	/**
	 * Create a new user.
	 * 
	 * @param newUser
	 *            The user to create.
	 * 
	 * @return HTTP 201, the header Location contains the URL of the created
	 *         user.
	 */
	@RequestMapping(method = POST, consumes = { APPLICATION_JSON_UTF8_VALUE })
	public Mono<ResponseEntity<?>> addUser(@RequestBody @Valid User newUser) {

		return Mono.justOrEmpty(newUser.getId())
			.flatMap(id -> repo.existsById(id))
			.defaultIfEmpty(Boolean.FALSE)
			.flatMap(exists -> {

				if (exists) {
					throw new UserServiceException(HttpStatus.BAD_REQUEST,
						"User already exists, to update an existing user use PUT instead.");
				}

				return repo.save(newUser).map(saved -> {
					return created(URI.create(format("/users/%s", saved.getId()))).build();
				});
			});
	}

	/**
	 * Update an existing user.
	 * <p>
	 * This method is idempotent.
	 * <p>
	 * 
	 * @param id
	 *            The id of the user to update.
	 * @param userToUpdate
	 *            The User object containing the updated version to be
	 *            persisted.
	 * 
	 * @return HTTP 204 otherwise HTTP 400 if the user does not exist.
	 */
	@RequestMapping(method = PUT, value = "/{id}", consumes = { APPLICATION_JSON_UTF8_VALUE })
	public Mono<ResponseEntity<?>> updateUser(@PathVariable @NotNull ObjectId id,
			@RequestBody @Valid User userToUpdate) {

		return repo.existsById(id).flatMap(exists -> {

			if (!exists) {
				throw new UserServiceException(HttpStatus.BAD_REQUEST,
					"User does not exist, to create a new user use POST instead.");
			}

			return repo.save(userToUpdate).then(Mono.just(noContent().build()));
		});
	}

	/**
	 * Delete a user.
	 * <p>
	 * This method is idempotent, if it's called multiples times with the same
	 * id then the first call will delete the user and subsequent calls will
	 * be silently ignored.
	 * 
	 * @param id
	 *            The id of the user to delete.
	 * @return HTTP 204
	 */
	@RequestMapping(method = DELETE, value = "/{id}")
	public Mono<ResponseEntity<?>> deleteUser(@PathVariable @NotNull ObjectId id) {

		final Mono<ResponseEntity<?>> noContent = Mono.just(noContent().build());

		return repo.existsById(id)
			.filter(Boolean::valueOf) // Delete only if user exists
			.flatMap(exists -> repo.deleteById(id).then(noContent))
			.switchIfEmpty(noContent);
	}

	@PostMapping("/create")
	Mono<Void> create(@RequestBody Publisher<User> userStream) {
		return this.repo.saveAll(userStream).then();
	}

	@GetMapping("/list")
	@PreAuthorize("hasRole('ROLE_ADMIN')")
	Flux<User> list() {
		return this.repo.findAll();
	}

	@GetMapping("/{id}")
	Mono<User> findById(@PathVariable ObjectId id) {
		return this.repo.findById(id);
	}
}
