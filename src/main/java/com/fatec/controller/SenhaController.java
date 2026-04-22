package com.fatec.controller;

import com.fatec.dto.NovaSenhaDTO;
import com.fatec.service.UsuarioService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/senha")
@CrossOrigin(origins = "*")
public class SenhaController {

    private final UsuarioService usuarioService;

    public SenhaController(UsuarioService usuarioService) {
        this.usuarioService = usuarioService;
    }

    @PatchMapping("/atualizar")
    public ResponseEntity<Object> atualizarSenha(@RequestBody NovaSenhaDTO dto) {
        // A lógica de validação do token e atualização da senha já está na service
        usuarioService.atualizarSenhaViaToken(dto);

        // Retorna mensagem padrão de sucesso
        return ResponseEntity.ok().body("Senha atualizada com sucesso.");
    }
}