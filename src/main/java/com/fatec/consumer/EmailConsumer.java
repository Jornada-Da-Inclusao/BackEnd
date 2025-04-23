package com.fatec.consumer;

import org.springframework.stereotype.Component;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.messaging.handler.annotation.Payload;
import com.fatec.dto.EmailRecordDto;
import com.fatec.service.EmailService;
import com.fatec.model.EmailVerify;

@Component
public class EmailConsumer {

    private static final Logger logger = LogManager.getLogger(EmailConsumer.class);

    private final EmailService emailService;

    public EmailConsumer(EmailService emailService) {
        this.emailService = emailService;
    }

    @RabbitListener(queues = "${broker.queue.email.name}")
    public void listenEmailQueue(@Payload EmailRecordDto emailRecordDto) {
        logger.info("📥 [CONSUMER] Mensagem recebida na fila:");

        logger.info("➡️ emailTo: " + emailRecordDto.to());
        logger.info("➡️ subject: " + emailRecordDto.subject());
        logger.info("➡️ text: " + emailRecordDto.body());

        // Converte o EmailRecordDto para EmailVerify
        EmailVerify emailVerify = EmailVerify.fromDto(emailRecordDto, 123L, "noreply@domain.com");

        // Chama o serviço de envio de e-mail
        emailService.sendEmail(emailVerify);
    }
}
