package com.transempiric.webfluxTemplate.mongo;

import com.transempiric.webfluxTemplate.model.User;
import com.transempiric.webfluxTemplate.mongo.repository.UserReactiveCrudRepository;
import org.bson.types.ObjectId;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Component;
import org.springframework.util.Assert;
import reactor.core.publisher.Flux;

import java.util.Arrays;
import java.util.Date;

@Component
public class CmdLineRunner {
    private final UserReactiveCrudRepository userReactiveCrudRepository;

    public CmdLineRunner(UserReactiveCrudRepository userReactiveCrudRepository) {
        Assert.notNull(userReactiveCrudRepository, "userReactiveCrudRepository cannot be null");
        this.userReactiveCrudRepository = userReactiveCrudRepository;
    }

    @Bean
    public CommandLineRunner initDatabase() {
        Flux<User> people = Flux.just(
                new User(new ObjectId(), "jdev", "Joe", "Developer", "dev@transempiric.com", "{noop}dev", Arrays.asList("ROLE_ADMIN"), true, new Date())
        );

        return args -> {
            this.userReactiveCrudRepository.deleteAll().thenMany(userReactiveCrudRepository.saveAll(people)).blockLast();
        };
    }
}
