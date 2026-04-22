package com.fatec.service;

import com.fatec.exception.AppException;
import com.fatec.exception.ErrorCode;
import com.fatec.model.EmailVerify;
import com.fatec.producer.EmailProducer;
import com.fatec.repository.EmailRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Optional;
import java.util.UUID;

@Service
public class EmailService {

    private final EmailProducer emailProducer;
    private final EmailRepository emailRepository;

    @Autowired
    public EmailService(EmailProducer emailProducer, EmailRepository emailRepository) {
        this.emailProducer = emailProducer;
        this.emailRepository = emailRepository;
    }

    public EmailVerify enviarToken(String email) {
        try {
            EmailVerify token = new EmailVerify(
                    UUID.randomUUID().toString(),
                    true,
                    email,
                    LocalDateTime.now().plusMinutes(20)
            );
            emailRepository.save(token);
            emailProducer.publishMessage(token);
            return token;
        } catch (Exception e) {
            throw new AppException(
                    ErrorCode.INTERNAL_ERROR.getCode(),
                    "Falha ao gerar ou enviar token",
                    HttpStatus.INTERNAL_SERVER_ERROR
            );
        }
    }

    public void verificarToken(String token) {
        Optional<EmailVerify> emailTokenOpt = emailRepository.findByToken(token);
        if (emailTokenOpt.isEmpty()) {
            throw new AppException(
                    ErrorCode.RESOURCE_NOT_FOUND.getCode(),
                    "Token não encontrado",
                    HttpStatus.NOT_FOUND
            );
        }

        EmailVerify emailToken = emailTokenOpt.get();
        if (!emailToken.isStatus() || emailToken.getExp().isBefore(LocalDateTime.now())) {
            throw new AppException(
                    ErrorCode.TOKEN_EMAIL_UNAUTHORIZED.getCode(),
                    "Token expirado ou inválido",
                    HttpStatus.UNAUTHORIZED
            );
        }

        // Marca token como usado
        emailToken.setStatus(false);
        emailRepository.save(emailToken);
    }

    public Optional<EmailVerify> getToken(String token) {
        return emailRepository.findByToken(token);
    }
}