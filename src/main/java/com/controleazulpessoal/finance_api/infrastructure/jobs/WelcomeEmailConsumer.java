package com.controleazulpessoal.finance_api.infrastructure.jobs;

import com.controleazulpessoal.finance_api.infrastructure.configuration.rabbitmq.RabbitMQConfig;
import com.controleazulpessoal.finance_api.infrastructure.service.EmailService;
import com.controleazulpessoal.finance_api.usecase.user.output.WelcomeEmailEvent;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@RequiredArgsConstructor
public class WelcomeEmailConsumer {

    private final EmailService emailService;

    @RabbitListener(queues = RabbitMQConfig.QUEUE_WELCOME_EMAIL)
    public void processarMensagem(WelcomeEmailEvent event) {
        emailService.sendWelcomeEmail("Bem vindo", event.email(), event.name(), "seja bem-vindo ao nosso sistema!");
        log.info("Evento de boas-vindas processado para: {}", event.email());
    }
}