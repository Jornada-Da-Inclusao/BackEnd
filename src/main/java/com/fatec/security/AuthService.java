package com.fatec.security;

import com.fatec.model.Usuario;
import com.fatec.model.UsuarioLogin;
import com.fatec.repository.UsuarioRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Component;

import java.util.Optional;

@Component
public class AuthService {

    private final UsuarioRepository usuarioRepository;

    private final AuthenticationManager authenticationManager;
    private final JwtService jwtService;

    private final BCryptPasswordEncoder passwordEncoder;

    @Autowired
    public AuthService(UsuarioRepository usuarioRepository,
                       AuthenticationManager authenticationManager,
                       JwtService jwtService,
                       BCryptPasswordEncoder passwordEncoder) {
        this.usuarioRepository = usuarioRepository;
        this.authenticationManager = authenticationManager;
        this.jwtService = jwtService;
        this.passwordEncoder = passwordEncoder;
    }

    public UsuarioLogin retornaUsuarioAutenticado(UsuarioLogin usuarioLogin) {
        // Autentica as credenciais do usuário
        var credenciais = new UsernamePasswordAuthenticationToken(usuarioLogin.getEmail(), usuarioLogin.getSenha());
        Authentication authentication = authenticationManager.authenticate(credenciais);

        if (authentication.isAuthenticated()) {
            // Recupera o usuário do banco de dados
            Optional<Usuario> usuario = usuarioRepository.findByEmail(usuarioLogin.getEmail());

            if (usuario.isPresent()) {
                // Define os dados do usuário autenticado no objeto UsuarioLogin
                Usuario u = usuario.get();
                usuarioLogin.setId(u.getId());
                usuarioLogin.setNome(u.getNome());
                usuarioLogin.setToken(jwtService.generateToken(u.getId()));
                usuarioLogin.setSenha("");  // Limpa a senha antes de retornar
                return usuarioLogin;
            }
        }
        return null; // Retorna null se as credenciais não forem válidas
    }

    public String criptografarSenha(String senha) {
        return passwordEncoder.encode(senha);
    }
}