package com.transempiric.webfluxTemplate.web.test;

import com.transempiric.webfluxTemplate.model.User;
import com.transempiric.webfluxTemplate.mongo.repository.UserReactiveCrudRepository;
import org.bson.types.ObjectId;
import org.reactivestreams.Publisher;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import static org.springframework.http.MediaType.APPLICATION_JSON_UTF8_VALUE;
import static org.springframework.web.bind.annotation.RequestMethod.GET;

@RestController
@RequestMapping(path = "/test", produces = { APPLICATION_JSON_UTF8_VALUE })
public class TestRestController {

    private UserReactiveCrudRepository userReactiveCrudRepository;
    public TestRestController(UserReactiveCrudRepository userReactiveCrudRepository) {
        this.userReactiveCrudRepository = userReactiveCrudRepository;
    };

    @PostMapping("/create")
    Mono<Void> create(@RequestBody Publisher<User> userStream) {
        return this.userReactiveCrudRepository.saveAll(userStream).then();
    }

    @GetMapping("/user/list")
    Flux<User> list() {
        return this.userReactiveCrudRepository.findAll();
    }

    @GetMapping("/{id}")
    Mono<User> findById(@PathVariable ObjectId id) {
        return this.userReactiveCrudRepository.findById(id);
    }

    @RequestMapping(method = GET, value = "/user/{username}", produces = { APPLICATION_JSON_UTF8_VALUE })
    Mono<User> findById(@PathVariable String username) {
        return this.userReactiveCrudRepository.findUserByUsername(username);
    }
}
