package com.fatec.producer;


import com.fatec.dto.EmailRecordDto;
import com.fatec.model.EmailVerify;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.core.MessageBuilder;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.stereotype.Component;
import org.springframework.beans.factory.annotation.Value;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.UUID;

@Component
public class EmailProducer {

    final RabbitTemplate rabbitTemplate;

    public EmailProducer(RabbitTemplate rabbitTemplate) {
        this.rabbitTemplate = rabbitTemplate;
    }

    @Value("${broker.queue.email.name}")
    private String routingKey;

    public void publishMessage(EmailVerify emailVerify) {
        System.out.println(">>> Enviando e-mail para " + emailVerify.getUserEmail());

// Criando o conteúdo do e-mail com uma mensagem mais amigável e profissional
        String subject = "Recuperação de Senha - Jornada da Inclusão";
        String body = "Olá, \n\n" +
                "Recebemos uma solicitação para a recuperação da sua senha do usuario " +emailVerify.getUserEmail() + " na plataforma Jornada da Inclusão. " +
                "Para prosseguir com a recuperação, utilize o código abaixo:\n\n" +
                "Código de Recuperação: " + emailVerify.getToken() + "\n\n" +
                "Este código é válido por 10 minutos. "+ "\n\n" +
                "Caso não tenha solicitado essa recuperação, ignore este e-mail. " +
                "Sua conta permanece segura.\n\n" +
                "Atenciosamente, \n" +
                "Equipe Jornada da Inclusão";

// Preparando o objeto DTO para envio
        var emailDto = new EmailRecordDto(
                emailVerify.getUserEmail(),
                subject,
                body
        );

// Enviando o e-mail através do RabbitMQ
        rabbitTemplate.convertAndSend("", routingKey, emailDto);

    }

    public void publishMessageContaCriada(String email, String subject, String body) {
        System.out.println(">>> Enviando e-mail de criação de conta para " + email);

        // Criando o conteúdo do e-mail com uma mensagem de boas-vindas
        String finalSubject = subject != null ? subject : "Bem-vindo à plataforma Jornada da Inclusão!";
        String finalBody = body != null ? body : "Olá,\n\nSua conta foi criada com sucesso na plataforma Jornada da Inclusão. Estamos muito felizes em tê-lo(a) conosco.\n\n" +
                "Caso tenha alguma dúvida, nossa equipe está à disposição para ajudar.\n\n" +
                "Atenciosamente,\nEquipe Jornada da Inclusão.";

        // Criando o objeto EmailVerify com um token gerado, status como verdadeiro e data de expiração (pode ser qualquer data futura)
        EmailVerify emailVerify = new EmailVerify(UUID.randomUUID().toString(), true, email, LocalDateTime.now().plusMinutes(20));

        // Preparando o objeto DTO para envio
        var emailDto = new EmailRecordDto(
                emailVerify.getUserEmail(),
                finalSubject,
                finalBody
        );

        // Enviando o e-mail através do RabbitMQ
        rabbitTemplate.convertAndSend("", routingKey, emailDto);
    }
}
