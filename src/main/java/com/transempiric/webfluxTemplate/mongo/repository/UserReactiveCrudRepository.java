package com.transempiric.webfluxTemplate.mongo.repository;

import com.transempiric.webfluxTemplate.model.User;
import org.bson.types.ObjectId;
import org.springframework.data.repository.reactive.ReactiveCrudRepository;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Mono;

@Repository
public interface UserReactiveCrudRepository extends ReactiveCrudRepository<User, ObjectId> {
    Mono<UserDetails> findByUsername(String username);
    Mono<User> findUserByUsername(String username);
}