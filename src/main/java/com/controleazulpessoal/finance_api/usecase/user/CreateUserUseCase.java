package com.controleazulpessoal.finance_api.usecase.user;

import com.controleazulpessoal.finance_api.controller.v1.user.request.CreateUserRequest;
import com.controleazulpessoal.finance_api.exception.user.UserAlreadyExistsException;
import com.controleazulpessoal.finance_api.infrastructure.configuration.rabbitmq.RabbitMQConfig;
import com.controleazulpessoal.finance_api.persistence.entity.Category;
import com.controleazulpessoal.finance_api.persistence.entity.User;
import com.controleazulpessoal.finance_api.persistence.repository.CategoryRepository;
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
import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class CreateUserUseCase {

    private final UserRepository userRepository;
    private final UserMapper userMapper;
    private final RabbitTemplate rabbitTemplate;
    private final CategoryRepository categoryRepository;

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

        User savedUser = userRepository.save(user);

        WelcomeEmailEvent event = new WelcomeEmailEvent(user.getName(), user.getEmail());
        rabbitTemplate.convertAndSend(RabbitMQConfig.QUEUE_WELCOME_EMAIL, event);

        createDefaultCategories(savedUser);

        return  userMapper.entityToDto(savedUser);
    }

    public UserDto executeAndNotify(CreateUserRequest request) {
        UserDto userDto = execute(request);

        try {
            WelcomeEmailEvent event = new WelcomeEmailEvent(userDto.name(), userDto.email());
            rabbitTemplate.convertAndSend(RabbitMQConfig.QUEUE_WELCOME_EMAIL, event);
        } catch (Exception e) {
            System.err.println("Falha ao enviar evento de boas-vindas: " + e.getMessage());
        }

        return userDto;
    }

    private void createDefaultCategories(User user) {
        List<Category> defaultCategories = List.of(
                Category.builder().name("Alimentação").icon("restaurant").color("#9C27B0").user(user).build(),
                Category.builder().name("Transporte").icon("directions_car").color("#2196F3").user(user).build(),
                Category.builder().name("Moradia").icon("home").color("#F44336").user(user).build(),
                Category.builder().name("Lazer").icon("celebration").color("#FF9800").user(user).build(),
                Category.builder().name("Saúde").icon("medical_services").color("#4CAF50").user(user).build(),
                Category.builder().name("Casa").icon("cottage").color("#795548").user(user).build(),
                Category.builder().name("Educação").icon("school").color("#3F51B5").user(user).build(),
                Category.builder().name("Eletrônicos").icon("devices").color("#607D8B").user(user).build(),
                Category.builder().name("Supermercado").icon("shopping_cart").color("#4CAF50").user(user).build(),
                Category.builder().name("Gasolina").icon("local_gas_station").color("#FF5722").user(user).build(),
                Category.builder().name("Manutenção").icon("build").color("#9E9E9E").user(user).build(),
                Category.builder().name("Vestuário").icon("checkroom").color("#E91E63").user(user).build(),
                Category.builder().name("Viagem").icon("flight").color("#00BCD4").user(user).build(),
                Category.builder().name("Outros").icon("more_horiz").color("#000000").user(user).build()
        );

        categoryRepository.saveAll(defaultCategories);
    }

}
