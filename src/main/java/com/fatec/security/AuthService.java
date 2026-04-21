package com.fatec.security;

import com.fatec.model.Usuario;
import com.fatec.model.UsuarioLogin;
import com.fatec.repository.UsuarioRepository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Component;

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

        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        usuarioLogin.getEmail(),
                        usuarioLogin.getSenha()
                )
        );

        if (!authentication.isAuthenticated()) {
            return null;
        }

        UserDetails userDetails = (UserDetails) authentication.getPrincipal();

        Usuario usuario = usuarioRepository.findByEmail(userDetails.getUsername())
                .orElse(null);

        if (usuario == null) {
            return null;
        }

        usuarioLogin.setId(usuario.getId());
        usuarioLogin.setNome(usuario.getNome());

        usuarioLogin.setToken(jwtService.generateToken(usuario.getId()));

        usuarioLogin.setSenha("");

        return usuarioLogin;
    }

    public String criptografarSenha(String senha) {
        return passwordEncoder.encode(senha);
    }
}