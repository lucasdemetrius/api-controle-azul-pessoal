package com.controleazulpessoal.finance_api.usecase.category;

import com.controleazulpessoal.finance_api.controller.v1.category.request.CategoryRequest;
import com.controleazulpessoal.finance_api.exception.category.CategoryAlreadyExistsException;
import com.controleazulpessoal.finance_api.persistence.entity.Category;
import com.controleazulpessoal.finance_api.persistence.entity.User;
import com.controleazulpessoal.finance_api.persistence.repository.CategoryRepository;
import com.controleazulpessoal.finance_api.usecase.category.mapper.CategoryMapper;
import com.controleazulpessoal.finance_api.usecase.category.output.CategoryDto;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;

import java.util.Collections;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class CreateCategoryUseCaseTest {

    // @Mock cria um "dublê" do repositório — não acessa o banco real
    @Mock
    private CategoryRepository categoryRepository;

    // @Mock cria um "dublê" do mapper
    @Mock
    private CategoryMapper categoryMapper;

    // @InjectMocks cria o UseCase real e injeta os mocks acima nele
    @InjectMocks
    private CreateCategoryUseCase createCategoryUseCase;

    private User authenticatedUser;
    private CategoryRequest request;

    // @BeforeEach roda antes de cada teste — prepara os dados comuns
    @BeforeEach
    void setUp() {
        // Cria um usuário fake para simular o usuário autenticado
        authenticatedUser = User.builder()
                .id(UUID.randomUUID())
                .name("User Test")
                .email("test@test.com")
                .password("hash")
                .build();

        // Simula o usuário autenticado no SecurityContext
        var authentication = new UsernamePasswordAuthenticationToken(
                authenticatedUser, null, Collections.emptyList()
        );
        SecurityContextHolder.getContext().setAuthentication(authentication);

        // Cria um request fake comum para os testes
        request = CategoryRequest.builder()
                .name("Alimentação")
                .description("Gastos com alimentação")
                .color("#9C27B0")
                .icon("restaurant")
                .build();
    }

    @Test
    @DisplayName("Deve criar categoria com sucesso")
    void shouldCreateCategorySuccessfully() {
        // ARRANGE — prepara o comportamento dos mocks
        Category savedCategory = Category.builder()
                .id(UUID.randomUUID())
                .name(request.getName())
                .description(request.getDescription())
                .color(request.getColor())
                .icon(request.getIcon())
                .user(authenticatedUser)
                .build();

        CategoryDto expectedDto = new CategoryDto(
                savedCategory.getId(),
                savedCategory.getName(),
                savedCategory.getDescription(),
                savedCategory.getColor(),
                savedCategory.getIcon()
        );

        // Ensina os mocks a se comportarem
        when(categoryRepository.existsByNameAndUser(request.getName(), authenticatedUser))
                .thenReturn(false); // categoria não existe ainda

        when(categoryRepository.save(any(Category.class)))
                .thenReturn(savedCategory); // retorna a categoria salva

        when(categoryMapper.entityToDto(savedCategory))
                .thenReturn(expectedDto); // retorna o DTO esperado

        // ACT — executa o método que estamos testando
        CategoryDto result = createCategoryUseCase.execute(request);

        // ASSERT — verifica se o resultado foi o esperado
        assertThat(result).isNotNull();
        assertThat(result.name()).isEqualTo("Alimentação");
        assertThat(result.color()).isEqualTo("#9C27B0");
        assertThat(result.icon()).isEqualTo("restaurant");
    }

    @Test
    @DisplayName("Deve lançar exceção quando categoria já existe")
    void shouldThrowExceptionWhenCategoryAlreadyExists() {
        // ARRANGE — categoria já existe para esse usuário
        when(categoryRepository.existsByNameAndUser(request.getName(), authenticatedUser))
                .thenReturn(true); // simula que já existe

        // ACT + ASSERT — verifica se a exceção correta é lançada
        assertThatThrownBy(() -> createCategoryUseCase.execute(request))
                .isInstanceOf(CategoryAlreadyExistsException.class)
                .hasMessage("Category with this name already exists for this user.");
    }
}