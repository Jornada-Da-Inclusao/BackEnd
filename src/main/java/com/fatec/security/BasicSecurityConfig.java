package com.fatec.security;

import static org.springframework.security.config.Customizer.withDefaults;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
@EnableWebSecurity
public class BasicSecurityConfig {

    @Autowired
    private JwtAuthFilter authFilter;

    // Configuração do UserDetailsService
    @Bean
    UserDetailsService userDetailsService() {
        return new UserDetailsServiceImpl();
    }

    // Configuração do PasswordEncoder
    @Bean
    PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    // Configuração do AuthenticationProvider
    @Bean
    AuthenticationProvider authenticationProvider() {
        DaoAuthenticationProvider provider = new DaoAuthenticationProvider();
        provider.setUserDetailsService(userDetailsService());
        provider.setPasswordEncoder(passwordEncoder());
        return provider;
    }

    // Configuração do AuthenticationManager
    @Bean
    AuthenticationManager authenticationManager(AuthenticationConfiguration authenticationConfiguration) throws Exception {
        return authenticationConfiguration.getAuthenticationManager();
    }

    // Configuração do SecurityFilterChain
    @Bean
    SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http
                // Política de gerenciamento de sessão sem estado (stateless) para uso de JWT
                .sessionManagement(session -> session
                        .sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                // Desabilita CSRF, adequado para APIs que usam autenticação com tokens JWT
                .csrf(csrf -> csrf.disable())
                // Habilita o CORS com as configurações padrão
                .cors(withDefaults())
                // Define as permissões de acesso às URLs
                .authorizeHttpRequests(auth -> auth
                        .requestMatchers(HttpMethod.POST, "/usuarios/**").permitAll()
                        .requestMatchers(HttpMethod.POST,"/usuarios/logar").permitAll()
                        .requestMatchers("/error/**").permitAll()
                        .requestMatchers("/emailApi/**").permitAll()
                        .requestMatchers("/senha/**").permitAll()
                        .requestMatchers(HttpMethod.OPTIONS).permitAll() // Permite requisições OPTIONS (CORS)
                        .anyRequest().authenticated() // Exige autenticação para todas as outras requisições
                )
                // Configura o provedor de autenticação
                .authenticationProvider(authenticationProvider())
                // Adiciona o filtro de autenticação JWT antes do filtro padrão de autenticação de usuário
                .addFilterBefore(authFilter, UsernamePasswordAuthenticationFilter.class)
                // Habilita a autenticação básica HTTP, caso necessário
                .httpBasic(withDefaults());

        return http.build();
    }
}