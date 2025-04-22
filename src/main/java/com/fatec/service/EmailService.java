// O pacote onde o serviço de envio de e-mail está localizado.
package com.fatec.service;

// Importações necessárias para o envio de e-mails, manipulação de filas e outras funcionalidades.
import java.time.LocalDateTime;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.MailException;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.fatec.model.EmailVerify;
import com.fatec.repository.EmailRepository;
import com.fatec.model.StatusEmail;

// A anotação `@Service` marca essa classe como um serviço Spring. O Spring irá gerenciar a criação e injeção dessa classe.
@Service
public class EmailService {

    // Inicializando o logger para registrar informações de erro ou sucesso no envio de e-mails.
    private static final Logger logger = LogManager.getLogger(EmailService.class);

    // Injeção de dependências do repositório, do JavaMailSender (para enviar e-mails) e do RabbitTemplate (para enviar mensagens à fila).
    private final EmailRepository emailRepository;
    private final JavaMailSender emailSender;
    private final RabbitTemplate rabbitTemplate;

    // A anotação `@Value` injeta o valor da configuração diretamente nas variáveis.
    // O nome da fila de e-mail (do broker) é lido a partir do arquivo de configuração (application.properties ou application.yml).
    @Value("${broker.queue.email.name}")
    private String queueEmail;

    // O e-mail do remetente também é lido da configuração.
    @Value("${spring.mail.username}")
    private String emailFrom;

    // Construtor que permite a injeção das dependências necessárias para o funcionamento do serviço.
    public EmailService(
            EmailRepository emailRepository,
            JavaMailSender emailSender,
            RabbitTemplate rabbitTemplate
    ) {
        this.emailRepository = emailRepository;
        this.emailSender = emailSender;
        this.rabbitTemplate = rabbitTemplate;
    }

    /**
     * Método responsável por enviar o e-mail.
     * Este método é anotado com `@Transactional`, o que significa que, se ocorrer algum erro durante o envio do e-mail,
     * a transação será revertida e o estado do banco de dados não será alterado.
     *
     * @param email O objeto `EmailVerify` que contém todas as informações do e-mail a ser enviado.
     * @return O objeto `EmailVerify` atualizado com o status do envio.
     */
    @Transactional
    public EmailVerify sendEmail(EmailVerify email) {
        try {
            // Atribuindo a data e hora atual ao campo `sendDateEmail` do objeto `email`.
            email.setSendDateEmail(LocalDateTime.now());
            // Definindo o remetente do e-mail.
            email.setEmailFrom(emailFrom);

            // Criando uma instância de `SimpleMailMessage`, que é um tipo simples de mensagem de e-mail.
            SimpleMailMessage message = new SimpleMailMessage();
            // Definindo o destinatário, o assunto e o corpo do e-mail.
            message.setTo(email.getEmailTo());
            message.setSubject(email.getSubject());
            message.setText(email.getText());

            // Enviando o e-mail utilizando o `JavaMailSender`.
            emailSender.send(message);

            // Se o e-mail for enviado com sucesso, atualizamos o status para "SENT".
            email.setStatusEmail(StatusEmail.SENT);

            // Enviando o objeto `email` para a fila RabbitMQ para notificações ou processamento adicional.
            // O RabbitMQ está sendo usado aqui para comunicar outro serviço sobre o status do e-mail.
            rabbitTemplate.convertAndSend(queueEmail, email);

        } catch (MailException e) {
            // Se ocorrer um erro durante o envio do e-mail, ele é tratado aqui.
            // O erro é registrado nos logs e o status do e-mail é atualizado para "ERROR".
            logger.info(">>>>> emailservice sendmail erro -> " + e.getMessage());
            email.setStatusEmail(StatusEmail.ERROR);
        } finally {
            // Após tentar enviar o e-mail, o objeto `email` (com o novo status) é salvo no banco de dados.
            return emailRepository.save(email);
        }
    }
}
