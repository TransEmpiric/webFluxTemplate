package com.transempiric.webfluxTemplate.web.rest.v1;

import com.transempiric.webfluxTemplate.mongo.repository.UserReactiveCrudRepository;
import com.transempiric.webfluxTemplate.config.security.JwtAuthenticationRequest;
import com.transempiric.webfluxTemplate.config.security.JwtAuthenticationResponse;
import com.transempiric.webfluxTemplate.config.security.JwtTokenUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.AuthenticationException;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Mono;

import static org.springframework.http.MediaType.APPLICATION_JSON_UTF8;
import static org.springframework.http.MediaType.APPLICATION_JSON_UTF8_VALUE;
import static org.springframework.http.ResponseEntity.notFound;
import static org.springframework.http.ResponseEntity.ok;
import static org.springframework.web.bind.annotation.RequestMethod.POST;

@RestController
@RequestMapping(path = "/auth", produces = { APPLICATION_JSON_UTF8_VALUE })
public class AuthRestController {

	private UserReactiveCrudRepository repo;
	public AuthRestController(UserReactiveCrudRepository repo) {
		this.repo = repo;
	}

	@Autowired private JwtTokenUtil jwtTokenUtil;


	@RequestMapping(method = POST, value = "/token")
	@CrossOrigin("*")
	public Mono<ResponseEntity<JwtAuthenticationResponse>> token(@RequestBody JwtAuthenticationRequest authenticationRequest) throws AuthenticationException {
		String username =  authenticationRequest.getUsername();
		String password =  authenticationRequest.getPassword();

		return repo.findByUsername(authenticationRequest.getUsername())
			.map(user -> ok().contentType(APPLICATION_JSON_UTF8).body(
					new JwtAuthenticationResponse(jwtTokenUtil.generateToken(user), user.getUsername()))
			)
			.defaultIfEmpty(notFound().build());
	}
}
