package com.fatec.service;

import java.util.Objects;

import com.fatec.dto.NovaSenhaDTO;
import com.fatec.dto.UsuarioUpdateDTO;
import com.fatec.exception.AppException;
import com.fatec.exception.ErrorCode;
import com.fatec.model.EmailVerify;
import com.fatec.model.Usuario;
import com.fatec.repository.EmailRepository;
import com.fatec.repository.UsuarioRepository;
import com.fatec.security.AuthService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class UsuarioService {

    private final UsuarioRepository usuarioRepository;
    private final EmailRepository emailRepository;
    private final EmailService emailService;
    private final AuthService authService;
    private final BCryptPasswordEncoder passwordEncoder;

    @Autowired
    public UsuarioService(UsuarioRepository usuarioRepository, EmailRepository emailRepository,
                          EmailService emailService, AuthService authService, BCryptPasswordEncoder passwordEncoder) {
        this.usuarioRepository = usuarioRepository;
        this.emailRepository = emailRepository;
        this.emailService = emailService;
        this.authService = authService;
        this.passwordEncoder = passwordEncoder;
    }

    private Usuario buscarUsuarioPorId(Long id) {
        return usuarioRepository.findById(id)
                .orElseThrow(() -> new AppException(ErrorCode.USER_NOT_FOUND.getCode(),
                        "Usuário não encontrado", HttpStatus.NOT_FOUND));
    }

    private Usuario buscarUsuarioPorEmail(String email) {
        return usuarioRepository.findByEmail(email)
                .orElseThrow(() -> new AppException(ErrorCode.USER_NOT_FOUND.getCode(),
                        "Usuário não encontrado", HttpStatus.NOT_FOUND));
    }

    private void validarIdPathEBody(Long idPath, Long idBody) {
        if (!Objects.equals(idPath, idBody)) {
            throw new AppException(ErrorCode.INVALID_REQUEST.getCode(),
                    "ID do path diferente do ID do body", HttpStatus.BAD_REQUEST);
        }
    }

    private void validarEmailExistente(String email) {
        if (usuarioRepository.existsByEmail(email)) {
            throw new AppException(ErrorCode.INVALID_REQUEST.getCode(),
                    "Usuário já existe!", HttpStatus.BAD_REQUEST);
        }
    }

    // ========================== CRUD ==========================

    public Usuario cadastrarUsuario(Usuario usuario) {
        validarEmailExistente(usuario.getEmail());
        usuario.setSenha(authService.criptografarSenha(usuario.getSenha()));
        return usuarioRepository.save(usuario);
    }

    public Usuario atualizarUsuario(Usuario usuario, Long idPath) {
        Usuario usuarioBanco = buscarUsuarioPorId(idPath);
        validarIdPathEBody(idPath, usuario.getId());

        if (!usuarioBanco.getEmail().equals(usuario.getEmail()) && usuarioRepository.existsByEmail(usuario.getEmail())) {
            throw new AppException(ErrorCode.INVALID_REQUEST.getCode(),
                    "Email já cadastrado para outro usuário", HttpStatus.BAD_REQUEST);
        }

        usuarioBanco.setNome(usuario.getNome());
        usuarioBanco.setEmail(usuario.getEmail());
        usuarioBanco.setSenha(authService.criptografarSenha(usuario.getSenha()));
        return usuarioRepository.save(usuarioBanco);
    }

    public Usuario atualizarParcial(UsuarioUpdateDTO usuarioDto, Long idPath) {
        Usuario usuarioBanco = buscarUsuarioPorId(idPath);
        validarIdPathEBody(idPath, usuarioDto.getId());

        if (usuarioDto.getNome() != null) usuarioBanco.setNome(usuarioDto.getNome());

        if (usuarioDto.getEmail() != null && !usuarioDto.getEmail().equals(usuarioBanco.getEmail())) {
            if (usuarioRepository.existsByEmail(usuarioDto.getEmail())) {
                throw new AppException(ErrorCode.INVALID_REQUEST.getCode(),
                        "Email já cadastrado para outro usuário", HttpStatus.BAD_REQUEST);
            }
            usuarioBanco.setEmail(usuarioDto.getEmail());
        }

        if (usuarioDto.getSenha() != null) {
            usuarioBanco.setSenha(authService.criptografarSenha(usuarioDto.getSenha()));
        }

        return usuarioRepository.save(usuarioBanco);
    }

    public void atualizarSenhaViaToken(NovaSenhaDTO dto) {
        EmailVerify token = emailRepository.findByToken(dto.getToken())
                .orElseThrow(() -> new AppException(ErrorCode.TOKEN_EMAIL_NOT_FOUND.getCode(),
                        "Token Não localizado", HttpStatus.NOT_FOUND));

        if (token.isExpiradoOuUsado()) {
            throw new AppException(ErrorCode.TOKEN_EMAIL_UNAUTHORIZED.getCode(),
                    "Token Expirado", HttpStatus.UNAUTHORIZED);
        }

        Usuario usuario = buscarUsuarioPorEmail(token.getUserEmail());
        usuario.setSenha(authService.criptografarSenha(dto.getNovaSenha()));
        usuarioRepository.save(usuario);

        token.setStatus(true);
        emailRepository.save(token);
    }

    public void deletarUsuario(Long idPath) {
        buscarUsuarioPorId(idPath);
        usuarioRepository.deleteById(idPath);
    }
}