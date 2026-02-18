# 🟦 Controle Azul - API Financeira

API para App controle de finanças pessoais, desenvolvida com foco em **Clean Architecture**, **SOLID** e escalabilidade.

## 🚀 Tecnologias Utilizadas
* **Java 21** & **Spring Boot 3**
* **MySQL 8** (Dockerizado)
* **MapStruct** (Mapeamento de entidades/DTOs)
* **Lombok** (Produtividade)
* **Bean Validation** (Integridade de dados)
* **Swagger/OpenAPI** (Documentação)

## 🏗️ Arquitetura
O projeto segue princípios da **Arquitetura Limpa**, separando as regras de negócio (Use Cases) das infraestruturas externas (Controllers, Repositories).
* **Domain/Persistence**: Entidades JPA.
* **Use Cases**: Lógica de negócio pura e independente.
* **Web/Controller**: Interface REST com contratos bem definidos.

## 🛠️ Como rodar o projeto

### Pré-requisitos
* Docker e Docker Compose
* JDK 21

### Passo a passo
1. Clone o repositório:
   ```bash
   git clone [https://github.com/lucasdemetrius/api-controle-azul-pessoal/finance-api.git](https://github.com/lucasdemetrius/api-controle-azul-pessoal/finance-api.git)