package com.fatec.controller;

import com.fatec.dto.NovaSenhaDTO;
import com.fatec.service.UsuarioService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/senha")
@CrossOrigin("*")
public class SenhaController {

    @Autowired
    private UsuarioService usuarioService;

    @PatchMapping("/atualizar")
    public ResponseEntity<Object> atualizarSenha(@RequestBody NovaSenhaDTO dto) {
        boolean atualizou = usuarioService.atualizarSenhaViaToken(dto);

        if (!atualizou) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body("Token inválido, expirado ou usuário não encontrado.");
        }

        return ResponseEntity.ok("Senha atualizada com sucesso.");
    }
}
