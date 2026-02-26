package com.rkey.rkms_backend.security;

import org.springframework.context.annotation.Bean;
import org.springframework.security.crypto.argon2.Argon2PasswordEncoder;


public class SecurityConfig {
    
    @Bean
    public Argon2PasswordEncoder passwordEncoder(){
        return new Argon2PasswordEncoder(16,32, 1, 16384, 3);
    }
    
}
