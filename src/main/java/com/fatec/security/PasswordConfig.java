package com.fatec.security;

import org.springframework.context.annotation.Bean;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Component;

@Component
public class PasswordConfig {
    @Bean
    public BCryptPasswordEncoder passwordEncoderSenhaUsuario() {
        return new BCryptPasswordEncoder();
    }
}
