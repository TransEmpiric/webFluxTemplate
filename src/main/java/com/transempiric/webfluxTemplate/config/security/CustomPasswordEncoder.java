package com.transempiric.webfluxTemplate.config.security;

import org.springframework.security.crypto.bcrypt.BCrypt;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.security.SecureRandom;
import java.util.Base64;

public class CustomPasswordEncoder implements PasswordEncoder {
    @Override
    public String encode(CharSequence rawPassword) {
        SecureRandom random = new SecureRandom();
        byte bytes[] = new byte[20];
        random.nextBytes(bytes);

        String hashed = BCrypt.hashpw(rawPassword.toString(), BCrypt.gensalt(14, random));
        return hashed;
    }

    @Override
    public boolean matches(CharSequence rawPassword, String encodedPassword) {
        String decodedString  = new String(Base64.getDecoder().decode(rawPassword.toString()));
        return BCrypt.checkpw(decodedString, encodedPassword);
    }
}
