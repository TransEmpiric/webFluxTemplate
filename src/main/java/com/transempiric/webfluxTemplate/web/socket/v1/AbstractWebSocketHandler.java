package com.transempiric.webfluxTemplate.web.socket.v1;


import com.transempiric.webfluxTemplate.config.security.JwtAuthenticationToken;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.web.reactive.socket.WebSocketHandler;
import org.springframework.web.reactive.socket.WebSocketSession;
import reactor.core.publisher.Mono;

import java.security.Principal;
import java.util.ArrayList;
import java.util.Collection;

abstract class AbstractWebSocketHandler implements WebSocketHandler {

    protected ArrayList<String> authorizedRoles = new ArrayList<String>();

    public AbstractWebSocketHandler(){
    }

    @Override
    public Mono<Void> handle(WebSocketSession session) {
        return session.getHandshakeInfo().getPrincipal().filter(this::isAuthorized).then(doHandle(session));
    }

    private boolean isAuthorized(Principal principal) {
        JwtAuthenticationToken jwtAuthenticationToken = null;
        if(principal != null && principal instanceof JwtAuthenticationToken) {
            jwtAuthenticationToken = (JwtAuthenticationToken) principal;
        } else {
            throw new AccessDeniedException("Invalid Token...");
        }

        if (jwtAuthenticationToken == null || !jwtAuthenticationToken.isAuthenticated()) throw new AccessDeniedException("Invalid Token...");

        boolean hasRoles = this.hasRoles(jwtAuthenticationToken.getAuthorities());
        if (!hasRoles) throw new AccessDeniedException("Not Authorized...");

        return true;
    }

    private boolean hasRoles(Collection<GrantedAuthority> grantedAuthorities) {
        if (this.authorizedRoles == null || this.authorizedRoles.isEmpty()) return true;
        if (grantedAuthorities == null || grantedAuthorities.isEmpty()) return false;

        for (String role : authorizedRoles) {
            for (GrantedAuthority grantedAuthority : grantedAuthorities) {
                if (role.equalsIgnoreCase(grantedAuthority.getAuthority())) return true;
            }
        }

        return false;
    }

    abstract Mono<Void> doHandle(WebSocketSession session);
}