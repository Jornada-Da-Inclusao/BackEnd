// O pacote onde a configuração de RabbitMQ está localizada. É uma boa prática organizar o código por pacotes.
package com.fatec.configuration;

// Importações de classes que serão usadas para configurar a comunicação com o RabbitMQ
import org.springframework.amqp.rabbit.config.SimpleRabbitListenerContainerFactory;
import org.springframework.amqp.rabbit.connection.ConnectionFactory;
import org.springframework.amqp.rabbit.annotation.EnableRabbit;
import org.springframework.amqp.rabbit.listener.RabbitListenerContainerFactory;
import org.springframework.amqp.support.converter.Jackson2JsonMessageConverter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

// Anotação que indica que essa classe é uma classe de configuração no Spring.
// Isso significa que o Spring irá usar essa classe para configurar beans necessários para o contexto da aplicação.
@Configuration

// Habilita o uso de listeners Rabbit no contexto da aplicação.
// O `@EnableRabbit` permite que o Spring reconheça as anotações `@RabbitListener`,
// que são usadas para configurar métodos que vão consumir mensagens de filas RabbitMQ.
@EnableRabbit
public class RabbitConfig {

    // Método que cria um Bean do tipo `Jackson2JsonMessageConverter`.
    // O Bean é um conversor de mensagens que irá converter as mensagens em JSON (e vice-versa).
    // Isso é útil quando você deseja enviar e receber mensagens no formato JSON,
    // e o RabbitMQ por padrão não lida diretamente com esse formato.
    @Bean
    public Jackson2JsonMessageConverter jackson2JsonMessageConverter() {
        // Retorna uma nova instância do conversor Jackson2JsonMessageConverter.
        // A biblioteca Jackson é utilizada para conversão entre objetos Java e JSON.
        return new Jackson2JsonMessageConverter();
    }

    // Método que cria um Bean do tipo `RabbitListenerContainerFactory`.
    // Esse Bean define como os listeners de filas devem ser configurados.
    // O método recebe dois parâmetros: o `ConnectionFactory` (responsável pela conexão com o RabbitMQ)
    // e o `Jackson2JsonMessageConverter` (responsável pela conversão de mensagens).
    @Bean
    public RabbitListenerContainerFactory<?> rabbitListenerContainerFactory(
            ConnectionFactory connectionFactory,  // Fábrica de conexão com o RabbitMQ
            Jackson2JsonMessageConverter messageConverter // Conversor de mensagens JSON
    ) {
        // Cria uma instância de `SimpleRabbitListenerContainerFactory`.
        // Essa classe é usada para criar containers de listeners que vão ouvir as filas do RabbitMQ.
        SimpleRabbitListenerContainerFactory factory = new SimpleRabbitListenerContainerFactory();

        // Configura a fábrica para usar o ConnectionFactory, o que permite ao container se conectar ao RabbitMQ.
        factory.setConnectionFactory(connectionFactory);

        // Configura o conversor de mensagens para que o container saiba como converter as mensagens
        // em JSON ao receber ou enviar.
        factory.setMessageConverter(messageConverter);

        // Retorna a fábrica configurada.
        // Essa fábrica será usada para criar containers que irão processar as mensagens.
        return factory;
    }
}
