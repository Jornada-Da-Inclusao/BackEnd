package com.fatec.controller;

import com.fatec.service.EmailService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

@CrossOrigin("*")
@RestController
@RequestMapping("emailApi")

public class EmailController {

    @Autowired
    private EmailService emailService;

    @PostMapping("/token/{email}")
    public ResponseEntity<Object> enviarToken(@PathVariable(value="email") String email) {
        Map<String, String> message = new HashMap<>();
        HttpStatus statusCode = HttpStatus.OK;
        boolean result = emailService.enviarToken(email);
        if(result) {
            message.put("message", "Sucesso. Foi enviado um código de confirmação no email fornecido.");
        }
        else {
            message.put("message", "Houve uma falha no envio do email.");
            statusCode = HttpStatus.INTERNAL_SERVER_ERROR;
        }
        return ResponseEntity.status(statusCode).body(message);
    }

    @GetMapping("/token/{token}")
    public ResponseEntity<Object> verificarToken(@PathVariable(value="token") String token) {
        Map<String, String> message = new HashMap<>();
        boolean result = emailService.verificarToken(token);
        if(!result) {
            message.put("message", "Token expirado ou inválido");
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(message);
        }
        return ResponseEntity.status(HttpStatus.OK).body("Token válido");
    }

}
