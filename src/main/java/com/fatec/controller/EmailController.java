package com.fatec.controller;

import com.fatec.service.EmailService;
import com.fatec.model.EmailVerify;
import com.fatec.exception.AppException;
import com.fatec.exception.ErrorCode;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import java.util.HashMap;
import java.util.Map;

@CrossOrigin("*")
@RestController
@RequestMapping("/emailApi")
public class EmailController {

    private final EmailService emailService;

    @Autowired
    public EmailController(EmailService emailService) {
        this.emailService = emailService;
    }

    @PostMapping("/token/{email}")
    public ResponseEntity<Map<String, String>> enviarToken(@PathVariable String email) {
        Map<String, String> response = new HashMap<>();
        try {
            EmailVerify token = emailService.enviarToken(email);
            response.put("message", "Sucesso. Código de confirmação enviado para o email.");
            response.put("token", token.getToken()); // opcional para debug
            return ResponseEntity.ok(response);
        } catch (AppException e) {
            response.put("message", e.getMessage());
            return ResponseEntity.status(e.getStatus()).body(response);
        } catch (Exception e) {
            response.put("message", "Erro inesperado.");
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
        }
    }

    @GetMapping("/token/{token}")
    public ResponseEntity<Map<String, String>> verificarToken(@PathVariable String token) {
        Map<String, String> response = new HashMap<>();
        try {
            emailService.verificarToken(token);
            response.put("message", "Token válido");
            return ResponseEntity.ok(response);
        } catch (AppException e) {
            response.put("message", e.getMessage());
            return ResponseEntity.status(e.getStatus()).body(response);
        } catch (Exception e) {
            response.put("message", "Erro inesperado.");
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
        }
    }
}