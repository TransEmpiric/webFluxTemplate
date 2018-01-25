package com.transempiric.webfluxTemplate.web.socket.v1;

import org.springframework.web.reactive.socket.WebSocketMessage;
import org.springframework.web.reactive.socket.WebSocketSession;
import reactor.core.publisher.Mono;

import java.time.Duration;
import java.util.Arrays;

public class DefaultWebSocketHandler extends AbstractWebSocketHandler {
    public DefaultWebSocketHandler(){
        this.authorizedRoles.addAll(Arrays.asList("ROLE_ADMIN"));
    }

    public Mono<Void> doHandle(WebSocketSession session) {
        // Use retain() for Reactor Netty
        return session.send(session.receive().doOnNext(WebSocketMessage::retain).delayElements(Duration.ofSeconds(2)));
    }
}