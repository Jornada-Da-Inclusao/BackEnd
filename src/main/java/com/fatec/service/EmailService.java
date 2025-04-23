package com.fatec.service;

import com.fatec.model.EmailVerify;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;


@Service
public class EmailService {

    private final JavaMailSender mailSender;

    // Construtor para injeção de dependência do JavaMailSender
    public EmailService(JavaMailSender mailSender) {
        this.mailSender = mailSender;
    }

    // Pega o valor da variável de ambiente do application.properties
    @Value(value = "${spring.rabbitmq.username}")
    private String emailFrom;

    // Método para enviar e-mail
    public void sendEmail(EmailVerify email) {
        System.out.println("Enviando...");

        // Criando a mensagem de e-mail
        var message = new SimpleMailMessage();
        message.setFrom(emailFrom);
        message.setTo(email.to());
        message.setSubject(email.subject());
        message.setText(email.body());

        // Enviando a mensagem
        mailSender.send(message);
    }
}
