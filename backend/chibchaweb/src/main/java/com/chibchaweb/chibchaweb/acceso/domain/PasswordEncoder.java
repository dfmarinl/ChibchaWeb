package com.chibchaweb.chibchaweb.acceso.domain;

public interface PasswordEncoder {

    String encode(String rawPassword);
    boolean matches(String rawPassword, String encodedPassword);

}
