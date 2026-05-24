package com.chibchaweb.chibchaweb.acceso.infrastructure.security;

import org.springframework.stereotype.Component;
import com.chibchaweb.chibchaweb.acceso.domain.PasswordEncoder;

@Component
public class BcryptPasswordEncoder implements PasswordEncoder {

    private final org.springframework.security.crypto.password.PasswordEncoder delegate;

    public BcryptPasswordEncoder() {
        this.delegate = new org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder();
    }

    @Override
    public String encode(String rawPassword) {
        return delegate.encode(rawPassword);
    }

    @Override
    public boolean matches(String rawPassword, String encodedPassword) {
        return delegate.matches(rawPassword, encodedPassword);
    }
}
