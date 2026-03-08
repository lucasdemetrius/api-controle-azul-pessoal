package com.controleazulpessoal.finance_api.infrastructure.configuration.rabbitmq;

import org.springframework.amqp.core.Queue;
import org.springframework.amqp.rabbit.connection.ConnectionFactory;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.amqp.support.converter.MessageConverter;
import org.springframework.amqp.support.converter.JacksonJsonMessageConverter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

// Define esta classe como uma fonte de definições de Beans para o Spring
@Configuration
public class RabbitMQConfig {

    // Constante que armazena o nome da fila para evitar erros de digitação (hardcode)
    public static final String QUEUE_WELCOME_EMAIL = "user.registration.welcome";

    // Define a fila no RabbitMQ. O 'true' indica que a fila é durável (sobrevive ao restart do broker)
    @Bean
    public Queue queue() {
        return new Queue(QUEUE_WELCOME_EMAIL, true);
    }

    // Define o conversor que transforma objetos Java em JSON para o RabbitMQ
    @Bean
    public MessageConverter jsonMessageConverter() {
        return new JacksonJsonMessageConverter();
    }

    // Configura o 'RabbitTemplate', que é o "braço direito" para enviar mensagens para o RabbitMQ
    @Bean
    public RabbitTemplate rabbitTemplate(ConnectionFactory connectionFactory, MessageConverter messageConverter) {
        // Cria uma nova instância do template conectada à fábrica de conexões
        RabbitTemplate rabbitTemplate = new RabbitTemplate(connectionFactory);

        // Define o conversor JSON que configuramos acima para ser usado por este template
        rabbitTemplate.setMessageConverter(messageConverter);

        // Retorna o template pronto para uso pelo Spring
        return rabbitTemplate;
    }
}