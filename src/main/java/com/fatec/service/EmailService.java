package com.fatec.service;

import com.fatec.dto.EmailRecordDto;
import com.fatec.producer.EmailProducer;
import com.fatec.model.EmailVerify;
import com.fatec.repository.EmailRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Optional;
import java.util.UUID;


@Service
public class EmailService {

    @Autowired
    private EmailProducer emailProducer;
    @Autowired
    private EmailRepository emailRepository;

    public boolean enviarToken(String email) {
        try {
            EmailVerify passwordToken = new EmailVerify(UUID.randomUUID().toString(), true, email, LocalDateTime.now().plusMinutes(20));
            emailRepository.save(passwordToken);
            emailProducer.publishMessage(passwordToken);
            return true;
        } catch(Exception e) {
            return false;
        }
    }

    public boolean verificarToken(String token) {
        var t = emailRepository.findByToken(token);
        if(t.isPresent()) {
            boolean result = t.get().isStatus() && t.get().getExp().isAfter(LocalDateTime.now());
            if(t.get().isStatus()) {
                t.get().setStatus(false);
                emailRepository.save(t.get());
            }
            return result;
        }
        else {
            return false;
        }
    }

    public Optional<EmailVerify> getToken(String token) {
        Optional<EmailVerify> passwordToken = emailRepository.findByToken(token);
        return passwordToken;
    }
}
