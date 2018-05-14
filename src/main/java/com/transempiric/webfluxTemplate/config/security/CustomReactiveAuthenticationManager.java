package com.transempiric.webfluxTemplate.config.security;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.ReactiveAuthenticationManager;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.ReactiveUserDetailsService;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.factory.PasswordEncoderFactories;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;
import org.springframework.util.Assert;
import reactor.core.publisher.Mono;
import reactor.core.scheduler.Schedulers;

@Component
public class CustomReactiveAuthenticationManager implements ReactiveAuthenticationManager {
    private final Logger logger = LoggerFactory.getLogger(this.getClass());

    private final ReactiveUserDetailsService userDetailsService;
    private final JwtTokenUtil jwtTokenUtil;
    private PasswordEncoder passwordEncoder = PasswordEncoderFactories.createDelegatingPasswordEncoder();

    public CustomReactiveAuthenticationManager(ReactiveUserDetailsService userDetailsService, JwtTokenUtil jwtTokenUtil) {
        Assert.notNull(userDetailsService, "userDetailsService cannot be null");
        Assert.notNull(jwtTokenUtil, "jwtTokenUtil cannot be null");

        this.userDetailsService = userDetailsService;
        this.jwtTokenUtil = jwtTokenUtil;
    }

    @Override
    public Mono<Authentication> authenticate(final Authentication authentication) {
        if (authentication instanceof JwtPreAuthenticationToken) {
            return Mono.just(authentication)
                    .switchIfEmpty(Mono.defer(this::raiseBadCredentials))
                    .cast(JwtPreAuthenticationToken.class)
                    .flatMap(this::authenticateToken)
                    .publishOn(Schedulers.parallel())
                    .onErrorResume(e -> raiseBadCredentials())
                    .map(u -> new JwtAuthenticationToken(u.getUsername(), u.getAuthorities()));
        }

        return Mono.just(authentication);
    }

    private <T> Mono<T> raiseBadCredentials() {
        return Mono.error(new BadCredentialsException("Invalid Credentials"));
    }

    private Mono<UserDetails> authenticateToken(final JwtPreAuthenticationToken jwtPreAuthenticationToken) {
        try {
            String authToken = jwtPreAuthenticationToken.getAuthToken();
            String username = jwtPreAuthenticationToken.getUsername();
            String bearerRequestHeader = jwtPreAuthenticationToken.getBearerRequestHeader();

            logger.info("checking authentication for user " + username);
            if (username != null && SecurityContextHolder.getContext().getAuthentication() == null) {
                if (jwtTokenUtil.validateToken(authToken)) {
                    logger.info("authenticated user " + username + ", setting security context");
                    final String token = authToken;
                    return this.userDetailsService.findByUsername(username);
                }
            }
        } catch (Exception e) {
            throw new BadCredentialsException("Invalid token...");
        }

        return null;
    }

    public void setPasswordEncoder(PasswordEncoder passwordEncoder) {
        Assert.notNull(passwordEncoder, "passwordEncoder cannot be null");
        this.passwordEncoder = passwordEncoder;
    }
}