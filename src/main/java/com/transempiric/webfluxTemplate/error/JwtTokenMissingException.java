package com.transempiric.webfluxTemplate.error;

import org.springframework.security.core.AuthenticationException;
/**
 * Thrown when token cannot be found in the request header
 */
public class JwtTokenMissingException extends AuthenticationException {
    public JwtTokenMissingException(String msg) {
        super(msg);
    }
}