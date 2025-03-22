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

@Configuration // Marca a classe como uma classe de configuração do Spring
@EnableWebSecurity // Habilita a configuração de segurança da aplicação web
public class BasicSecurityConfig {

    @Autowired
    private JwtAuthFilter authFilter; // Filtro de autenticação JWT, injetado automaticamente pelo Spring

    // Define um Bean para o serviço de detalhes do usuário (UserDetailsService) que será usado pela autenticação
    @Bean
    UserDetailsService userDetailsService() {
        return new UserDetailsServiceImpl(); // Retorna uma implementação personalizada de UserDetailsService
    }

    // Define um Bean para o encoder de senha, que será usado para criptografar senhas
    @Bean
    PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder(); // Utiliza BCrypt para criptografar senhas
    }

    // Define um Bean para o provedor de autenticação que será usado para autenticar os usuários
    @Bean
    AuthenticationProvider authenticationProvider() {
        DaoAuthenticationProvider authenticationProvider = new DaoAuthenticationProvider();
        authenticationProvider.setUserDetailsService(userDetailsService()); // Define o serviço de detalhes de usuário
        authenticationProvider.setPasswordEncoder(passwordEncoder()); // Define o encoder de senha para o provedor de autenticação
        return authenticationProvider; // Retorna o provedor de autenticação configurado
    }

    // Define um Bean para o AuthenticationManager, que é responsável pela autenticação de usuários
    @Bean
    AuthenticationManager authenticationManager(AuthenticationConfiguration authenticationConfiguration)
            throws Exception {
        return authenticationConfiguration.getAuthenticationManager(); // Obtém e retorna o AuthenticationManager configurado
    }

    // Define um Bean para a configuração do filtro de segurança da aplicação web
    @Bean
    SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        // Configura o gerenciamento de sessões para Stateless, ou seja, não usa sessão no servidor
        http
                .sessionManagement(management -> management
                        .sessionCreationPolicy(SessionCreationPolicy.STATELESS)) // Define a política de sessão como STATELESS (sem estado)
                .csrf(csrf -> csrf.disable()) // Desabilita a proteção contra CSRF, necessária para APIs que usam tokens JWT
                .cors(withDefaults()); // Configura o CORS com as configurações padrão do Spring

        // Configura as permissões de acesso para diferentes URLs
        http
                .authorizeHttpRequests((auth) -> auth
                        .requestMatchers("/usuarios/logar").permitAll() // Permite acesso livre à rota de login
                        .requestMatchers("/usuarios/cadastrar").permitAll() // Permite acesso livre à rota de cadastro de usuário
                        .requestMatchers("/error/**").permitAll() // Permite acesso a qualquer rota que comece com "/error"
                        .requestMatchers(HttpMethod.OPTIONS).permitAll() // Permite acesso a requisições do tipo OPTIONS (CORS)
                        .anyRequest().authenticated()) // Exige autenticação para todas as outras requisições
                .authenticationProvider(authenticationProvider()) // Define o provedor de autenticação
                .addFilterBefore(authFilter, UsernamePasswordAuthenticationFilter.class) // Adiciona o filtro JWT antes do filtro de autenticação padrão
                .httpBasic(withDefaults()); // Habilita a autenticação básica HTTP, se necessário

        return http.build(); // Retorna a configuração de segurança final
    }
}
