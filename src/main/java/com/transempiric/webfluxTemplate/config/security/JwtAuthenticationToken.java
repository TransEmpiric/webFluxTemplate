package com.transempiric.webfluxTemplate.config.security;

import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.GrantedAuthority;

import java.util.Collection;

public class JwtAuthenticationToken extends UsernamePasswordAuthenticationToken {
    private String token;

    public JwtAuthenticationToken(String token, String username, Collection<? extends GrantedAuthority> authorities) {
        super(username, null, authorities);
        this.token = token;
    }

    public JwtAuthenticationToken(String username, Collection<? extends GrantedAuthority> authorities) {
        super(username, null, authorities);
    }

    public String getToken() {
        return token;
    }

    @Override
    public Object getCredentials() {
        return null;
    }
}
