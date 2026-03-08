package com.controleazulpessoal.finance_api.usecase.user;

import com.controleazulpessoal.finance_api.controller.v1.user.request.CreateUserRequest;
import com.controleazulpessoal.finance_api.exception.user.UserAlreadyExistsException;
import com.controleazulpessoal.finance_api.infrastructure.configuration.rabbitmq.RabbitMQConfig;
import com.controleazulpessoal.finance_api.persistence.entity.User;
import com.controleazulpessoal.finance_api.persistence.repository.UserRepository;
import com.controleazulpessoal.finance_api.usecase.user.mapper.UserMapper;
import com.controleazulpessoal.finance_api.usecase.user.output.UserDto;
import com.controleazulpessoal.finance_api.usecase.user.output.WelcomeEmailEvent;
import com.controleazulpessoal.finance_api.util.PasswordUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class CreateUserUseCase {

    private final UserRepository userRepository;
    private final UserMapper userMapper;
    private final RabbitTemplate rabbitTemplate;

    @Transactional
    public UserDto execute(CreateUserRequest request) {

        if (userRepository.existsByEmail(request.getEmail())) {
            throw new UserAlreadyExistsException();
        }

        String encryptedPassword = PasswordUtils.hashSenha(request.getPassword());

        User user = User.builder()
                .id(UUID.randomUUID())
                .email(request.getEmail())
                .password(encryptedPassword)
                .name(request.getName())
                .phoneNumber(request.getPhoneNumber())
                .receiveNotifications(true)
                .accountCreationDate(LocalDateTime.now())
                .lastAccessDate(LocalDateTime.now())
                .build();

        userRepository.save(user);

        WelcomeEmailEvent event = new WelcomeEmailEvent(user.getName(), user.getEmail());
        rabbitTemplate.convertAndSend(RabbitMQConfig.QUEUE_WELCOME_EMAIL, event);

        return  userMapper.entityToDto(user);
    }

}
