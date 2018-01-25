package com.transempiric.webfluxTemplate.error;

import org.springframework.security.core.AuthenticationException;
/**
 * Thrown when token cannot be parsed
 */
public class JwtTokenMalformedException extends AuthenticationException {
    public JwtTokenMalformedException(String msg) {
        super(msg);
    }

    public JwtTokenMalformedException(String msg, Throwable throwable) {
        super(msg, throwable);
    }
}