package com.fatec.controller;


import java.util.Optional;

import com.fatec.dto.UsuarioUpdateDTO;
import com.fatec.exception.AppException;
import com.fatec.exception.ErrorCode;
import com.fatec.security.AuthService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.fatec.model.Usuario;
import com.fatec.model.UsuarioLogin;
import com.fatec.repository.UsuarioRepository;
import com.fatec.service.UsuarioService;

import jakarta.validation.Valid;
import org.springframework.web.server.ResponseStatusException;
import org.springframework.amqp.rabbit.core.RabbitTemplate;

@RestController
@RequestMapping("/usuarios")
@CrossOrigin(origins = "*", allowedHeaders = "*")
public class UsuarioController {

    private final UsuarioService usuarioService;
    private final UsuarioRepository usuarioRepository;
    private final AuthService authService;

    @Autowired
    public UsuarioController(UsuarioService usuarioService, UsuarioRepository usuarioRepository, AuthService authService) {
        this.usuarioService = usuarioService;
        this.usuarioRepository = usuarioRepository;
        this.authService = authService;
    }

    @PostMapping
    public ResponseEntity<Usuario> postUsuario(@Valid @RequestBody Usuario usuario) {
        Usuario usuarioSalvo = usuarioService.cadastrarUsuario(usuario);
        return ResponseEntity.status(HttpStatus.CREATED).body(usuarioSalvo);
    }

    @PostMapping("/logar")
    public ResponseEntity<UsuarioLogin> autenticarUsuario(@Valid @RequestBody UsuarioLogin usuarioLogin) {
        UsuarioLogin usuarioAutenticado = authService.retornaUsuarioAutenticado(usuarioLogin);

        if (usuarioAutenticado.getId() == null)
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();


        return ResponseEntity.ok(usuarioLogin);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Void> putUsuario(@Valid @RequestBody Usuario usuario, @PathVariable Long id) {
        usuarioService.atualizarUsuario(usuario, id);
        return ResponseEntity.noContent().build(); // 204 NO CONTENT
    }

    @PatchMapping("/{id}")
    public ResponseEntity<Void> patchUsuario(@RequestBody UsuarioUpdateDTO dto, @PathVariable Long id) {
        usuarioService.atualizarParcial(dto, id);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/{id}")
    public ResponseEntity<Usuario> getById(@PathVariable Long id) {
        return usuarioRepository.findById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.status(HttpStatus.NOT_FOUND).build());
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        usuarioService.deletarUsuario(id);
        return ResponseEntity.noContent().build();
    }
}
