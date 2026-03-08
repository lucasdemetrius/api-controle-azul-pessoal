package com.controleazulpessoal.finance_api.infrastructure.jobs;

import com.controleazulpessoal.finance_api.infrastructure.configuration.rabbitmq.RabbitMQConfig;
import com.controleazulpessoal.finance_api.infrastructure.service.EmailService;
import com.controleazulpessoal.finance_api.usecase.user.output.WelcomeEmailEvent;
import lombok.RequiredArgsConstructor;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class WelcomeEmailConsumer {

    private final EmailService emailService;

    @RabbitListener(queues = RabbitMQConfig.QUEUE_WELCOME_EMAIL)
    public void processarMensagem(WelcomeEmailEvent event) {
        emailService.sendWelcomeEmail("Bem vindo", event.email(), event.name(), "seja bem-vindo ao nosso sistema!");
        System.out.println("Recebido evento de boas-vindas para: " + event.email());
    }
}