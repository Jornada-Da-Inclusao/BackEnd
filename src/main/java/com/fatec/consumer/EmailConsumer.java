// O pacote onde a classe de consumo de mensagens está localizada. A organização de pacotes é uma boa prática.
package com.fatec.consumer;

// Importações de classes que são utilizadas no código
import org.springframework.stereotype.Component;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.BeanUtils;
import org.springframework.messaging.handler.annotation.Payload;
import com.fatec.model.EmailVerify;
import com.fatec.dto.EmailRecordDto;
import com.fatec.service.EmailService;

// A anotação @Component indica que esta classe é um componente gerenciado pelo Spring.
// Isso significa que o Spring irá instanciar esta classe e ela pode ser injetada em outras partes da aplicação.
@Component
public class EmailConsumer {

    // Criando o logger para a classe. O log será usado para registrar eventos durante o processamento.
    Logger logger = LogManager.getLogger(this.getClass());

    // Definindo uma variável final que será injetada pelo Spring.
    // O EmailService é responsável por enviar os e-mails na aplicação.
    final EmailService emailService;

    /**
     * Construtor para injeção de dependência. O Spring irá injetar a instância do serviço `EmailService`
     * automaticamente. Isso segue o padrão de injeção de dependência do Spring.
     *
     * @param emailService Serviço de envio de e-mails que será injetado na classe.
     */
    public EmailConsumer(EmailService emailService) {
        this.emailService = emailService;
    }

    /**
     * Método responsável por ouvir mensagens na fila do RabbitMQ.
     * O método é anotado com `@RabbitListener` para que o Spring saiba que ele deve agir
     * como um ouvinte para a fila configurada no arquivo de propriedades (com a chave `broker.queue.email.name`).
     *
     * Quando uma mensagem chega, ela é mapeada para um objeto `EmailRecordDto`, que contém os dados necessários
     * para o envio de um e-mail.
     *
     * @param emailRecordDto O DTO que contém as informações do e-mail, como destinatário, assunto e texto.
     */
    @RabbitListener(queues = "${broker.queue.email.name}") // O nome da fila é lido de um arquivo de propriedades
    public void listenEmailQueue(@Payload EmailRecordDto emailRecordDto) {

        // Logando informações de depuração para entender o que está acontecendo no processo.
        logger.info("📥 [CONSUMER] Mensagem recebida na fila:");
        logger.info("➡️ emailTo: " + emailRecordDto.emailTo()); // Logando o destinatário do e-mail
        logger.info("➡️ subject: " + emailRecordDto.subject()); // Logando o assunto do e-mail
        logger.info("➡️ text: " + emailRecordDto.text()); // Logando o corpo do e-mail

        // Log adicional para indicar que a mensagem foi processada.
        logger.info(">>>>> emailconsumer -> listen email - enviado =>" + emailRecordDto.emailTo());

        // Criando um objeto do tipo `EmailVerify`, que provavelmente é uma entidade ou modelo de e-mail.
        var email = new EmailVerify();

        // Utilizando `BeanUtils.copyProperties` para copiar as propriedades do DTO `emailRecordDto` para o objeto `email`.
        // Essa técnica é útil para converter um objeto de uma camada para outro, por exemplo, de um DTO para um modelo.
        BeanUtils.copyProperties(emailRecordDto, email);

        // Agora, chamamos o serviço de envio de e-mail para realmente enviar a mensagem.
        // O `sendEmail()` provavelmente se encarrega de enviar o e-mail para o destinatário.
        emailService.sendEmail(email);
    }
}
