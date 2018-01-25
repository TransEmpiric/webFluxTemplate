package com.transempiric.webfluxTemplate.service;

import com.transempiric.webfluxTemplate.mongo.repository.UserReactiveCrudRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.ReactiveUserDetailsService;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

@Service
public class CustomReactiveUserDetailsService implements ReactiveUserDetailsService {

    @Autowired public UserReactiveCrudRepository userRepository;

    @Override
    public Mono<UserDetails> findByUsername(String username) {
        Mono<UserDetails> data = userRepository.findByUsername(username);
        return data;
    }
}