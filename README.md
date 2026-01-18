# 🍔 ManaFood - Microsserviço de Pedidos

[![Java](https://img.shields.io/badge/Java-17-orange.svg)](https://www.oracle.com/java/)
[![Kotlin](https://img.shields.io/badge/Kotlin-1.9.24-blue.svg)](https://kotlinlang.org/)
[![Spring Boot](https://img.shields.io/badge/Spring%20Boot-3.3.5-brightgreen.svg)](https://spring.io/projects/spring-boot)
[![Maven](https://img.shields.io/badge/Maven-3.9+-red.svg)](https://maven.apache.org/)
[![MySQL](https://img.shields.io/badge/MySQL-8.0-blue.svg)](https://www.mysql.com/)
[![License](https://img.shields.io/badge/License-MIT-yellow.svg)](LICENSE)
[![SonarCloud](https://sonarcloud.io/api/project_badges/measure?project=mana-food_mana-food-microsservice-order&metric=alert_status)](https://sonarcloud.io/summary/new_code?id=mana-food_mana-food-microsservice-order)

Microsserviço responsável pelo gerenciamento de pedidos do sistema ManaFood, desenvolvido seguindo princípios de **Clean Architecture**, **Domain-Driven Design (DDD)** e **CQRS**.

---

## 📋 Índice

- [Sobre o Projeto](#-sobre-o-projeto)
- [Arquitetura](#-arquitetura)
- [Tecnologias](#-tecnologias)
- [Estrutura do Projeto](#-estrutura-do-projeto)
- [Pré-requisitos](#-pré-requisitos)
- [Instalação e Execução](#-instalação-e-execução)
- [Testes](#-testes)
- [Endpoints da API](#-endpoints-da-api)
- [Variáveis de Ambiente](#-variáveis-de-ambiente)
- [Docker](#-docker)
- [CI/CD](#-cicd)
- [Qualidade de Código](#-qualidade-de-código)
- [Contribuindo](#-contribuindo)
- [Licença](#-licença)

---

## 🎯 Sobre o Projeto

O **Microsserviço de Pedidos** é parte do ecossistema ManaFood e tem como responsabilidades:

- ✅ Criar e gerenciar pedidos de clientes
- ✅ Validar produtos através de comunicação com o microsserviço de produtos
- ✅ Confirmar pagamentos e atualizar status de pedidos
- ✅ Controlar fluxo de preparação na cozinha
- ✅ Gerenciar estados do pedido (CREATED, RECEIVED, PREPARING, READY, FINISHED, REJECTED)
- ✅ Fornecer consultas paginadas de pedidos
- ✅ Soft delete de pedidos

### Método de Pagamento Suportado

- 🔹 **QR_CODE**: Pagamento via QR Code (Pix/Mercado Pago)

---

## 🏗️ Arquitetura

O projeto segue os princípios de **Clean Architecture** e **DDD**, organizando o código em camadas bem definidas:

```
┌─────────────────────────────────────────────┐
│          Adapter (Controller/API)           │
│  ├─ REST Controllers                        │
│  ├─ DTOs (Request/Response)                 │
│  └─ Mappers                                 │
├─────────────────────────────────────────────┤
│         Application (Use Cases)             │
│  ├─ Commands (Create, Update, Delete)       │
│  ├─ Queries (GetAll, GetById, GetReady)     │
│  └─ Services                                │
├─────────────────────────────────────────────┤
│            Domain (Business)                │
│  ├─ Entities (Order, OrderProduct)          │
│  ├─ Value Objects (OrderStatus, Payment)    │
│  └─ Repository Interfaces                   │
├─────────────────────────────────────────────┤
│         Infrastructure (External)           │
│  ├─ JPA Entities                            │
│  ├─ Repository Implementations              │
│  ├─ Feign Clients (Product Service)         │
│  └─ Database Migrations (Flyway)            │
└─────────────────────────────────────────────┘
```

### Padrões Implementados

- **CQRS**: Separação de Commands e Queries
- **Repository Pattern**: Abstração de persistência
- **Mapper Pattern**: Conversão entre camadas
- **Factory Pattern**: Criação de objetos complexos
- **Use Case Pattern**: Encapsulamento de regras de negócio

---

## 🚀 Tecnologias

### Core

- **Kotlin** 1.9.24 - Linguagem principal
- **Java** 17 - JVM
- **Spring Boot** 3.3.5 - Framework
- **Spring Cloud** 2023.0.3 - Microservices
- **Maven** - Gerenciamento de dependências

### Banco de Dados

- **MySQL** 8.0 - Banco de dados principal
- **Flyway** - Controle de versão do banco
- **Spring Data JPA** - ORM
- **Hibernate** - Implementação JPA

### Comunicação

- **Spring Cloud OpenFeign** - Cliente HTTP declarativo
- **RestTemplate** - Cliente REST

### Documentação

- **SpringDoc OpenAPI** 2.6.0 - Documentação automática da API
- **Swagger UI** - Interface interativa da API

### Testes

- **JUnit 5** - Framework de testes
- **Mockk** 1.13.13 - Mock para Kotlin
- **Cucumber** 7.18.1 - Testes BDD
- **Testcontainers** 1.19.3 - Testes de integração
- **AssertJ** - Assertions fluentes
- **WireMock** - Mock de APIs externas

### Qualidade e Monitoramento

- **SonarCloud** - Análise de código
- **JaCoCo** - Cobertura de testes
- **Spring Boot Actuator** - Métricas e health checks
- **Micrometer** - Observabilidade

---

## 📁 Estrutura do Projeto

```
mana-food-microsservice-order-app/
├── src/
│   ├── main/
│   │   ├── kotlin/
│   │   │   └── br/com/manafood/manafoodorder/
│   │   │       ├── adapter/
│   │   │       │   ├── controller/         # REST Controllers
│   │   │       │   ├── mapper/             # Conversores DTO ↔ Domain
│   │   │       │   ├── request/            # DTOs de requisição
│   │   │       │   └── response/           # DTOs de resposta
│   │   │       ├── application/
│   │   │       │   ├── factory/            # Factories
│   │   │       │   ├── service/            # Serviços de aplicação
│   │   │       │   └── usecase/
│   │   │       │       ├── commands/       # Use Cases de escrita
│   │   │       │       └── queries/        # Use Cases de leitura
│   │   │       ├── domain/
│   │   │       │   ├── common/             # Classes comuns
│   │   │       │   ├── model/              # Entidades de domínio
│   │   │       │   ├── repository/         # Interfaces de repositório
│   │   │       │   └── valueobject/        # Value Objects
│   │   │       └── infrastructure/
│   │   │           ├── client/             # Clientes externos (Feign)
│   │   │           ├── config/             # Configurações
│   │   │           └── persistence/
│   │   │               ├── adapter/        # Implementações de repositório
│   │   │               ├── entity/         # Entidades JPA
│   │   │               └── repository/     # Repositories Spring Data
│   │   └── resources/
│   │       ├── application.yml             # Configuração principal
│   │       ├── application-local.yml       # Configuração local
│   │       └── db/migration/               # Migrations Flyway
│   └── test/
│       ├── kotlin/
│       │   └── br/com/manafood/manafoodorder/
│       │       ├── adapter/                # Testes de controllers
│       │       ├── application/            # Testes de use cases
│       │       ├── bdd/                    # Testes BDD (Cucumber)
│       │       │   ├── config/             # Configuração Spring
│       │       │   ├── context/            # Contexto compartilhado
│       │       │   ├── hooks/              # Hooks Before/After
│       │       │   ├── steps/              # Step Definitions
│       │       │   └── support/            # Mocks (WireMock)
│       │       ├── domain/                 # Testes de domínio
│       │       └── infrastructure/         # Testes de infraestrutura
│       └── resources/
│           ├── application-test.yml        # Configuração de teste
│           └── features/                   # Cenários BDD (Gherkin)
├── .github/
│   └── workflows/
│       └── build.yml                       # Pipeline CI/CD
├── docker-compose.yml                      # Orquestração de containers
├── Dockerfile                              # Imagem da aplicação
├── pom.xml                                 # Dependências Maven
└── README.md                               # Este arquivo
```

---

## 📋 Pré-requisitos

Antes de começar, você precisará ter instalado:

- **Java 17** ou superior
- **Maven 3.9+**
- **Docker** e **Docker Compose** (para execução local)
- **MySQL 8.0** (se não usar Docker)
- **Git**

### Verificando versões:

```bash
java -version    # java 17.0.x ou superior
mvn -version     # Apache Maven 3.9.x ou superior
docker --version # Docker version 24.x ou superior
```

---

## 🔧 Instalação e Execução

### 1️⃣ Clonar o repositório

```bash
git clone https://github.com/mana-food/mana-food-microsservice-order-app.git
cd mana-food-microsservice-order-app
```

### 2️⃣ Configurar variáveis de ambiente

Crie um arquivo `.env` na raiz do projeto:

```env
# Database
MYSQL_ROOT_PASSWORD=root
MYSQL_DATABASE=manafood_order_db
SPRING_DATASOURCE_URL=jdbc:mysql://db-mana-food-order:3306/manafood_order_db
SPRING_DATASOURCE_USERNAME=root

# Product Service
MANAFOODPRODUCT_SERVICE_URL=http://product-service:8081
```

### 3️⃣ Executar com Docker Compose (Recomendado)

```bash
# Subir todos os serviços
docker-compose up -d

# Verificar logs
docker-compose logs -f app-mana-food-order

# Parar os serviços
docker-compose down
```

A aplicação estará disponível em: `http://localhost:8082`

### 4️⃣ Executar localmente (sem Docker)

#### Subir apenas o banco de dados:

```bash
docker-compose up -d db-mana-food-order
```

#### Executar a aplicação:

```bash
# Compilar
mvn clean package -DskipTests

# Executar
mvn spring-boot:run -Dspring-boot.run.profiles=local
```

ou

```bash
java -jar target/mana-food-microsservice-order-0.0.1-SNAPSHOT.jar --spring.profiles.active=local
```

---

## 🧪 Testes

O projeto possui 3 tipos de testes:

### Testes Unitários

```bash
# Executar apenas testes unitários
mvn test

# Com relatório de cobertura
mvn clean test jacoco:report
```

### Testes de Integração

```bash
# Executar todos os testes
mvn clean verify
```

### Testes BDD (Cucumber)

```bash
# Executar testes BDD
mvn test -Dtest=CucumberTestRunner

# ⚠️ Requer Docker rodando (Testcontainers + WireMock)
```

**Cenários BDD implementados:**
- ✅ Criar pedido com QR_CODE
- ✅ Criar pedido com múltiplos produtos
- ✅ Atualizar status do pedido
- ✅ Consultar pedido por ID
- ✅ Consultar todos os pedidos com paginação
- ✅ Consultar pedidos prontos para cozinha
- ✅ Confirmar pagamento do pedido
- ✅ Deletar pedido
- ✅ Validação de produto inválido
- ✅ Validação de pedido inexistente

### Relatórios de Teste

```bash
# Cobertura JaCoCo
open target/site/jacoco/index.html

# Relatório Cucumber
open target/cucumber-reports/cucumber.html
```

---

## 📡 Endpoints da API

### Documentação Interativa

Acesse: `http://localhost:8082/swagger-ui.html`

### Principais Endpoints

#### Pedidos

| Método | Endpoint | Descrição |
|--------|----------|-----------|
| `POST` | `/api/order` | Criar novo pedido |
| `PUT` | `/api/order` | Atualizar status do pedido |
| `GET` | `/api/order/{id}` | Buscar pedido por ID |
| `GET` | `/api/order` | Listar todos os pedidos (paginado) |
| `GET` | `/api/order/ready` | Listar pedidos prontos para cozinha |
| `POST` | `/api/order/confirm-payment` | Confirmar pagamento |
| `DELETE` | `/api/order/{id}` | Deletar pedido (soft delete) |

#### Exemplos de Requisições

**Criar Pedido:**
```bash
curl -X POST http://localhost:8082/api/order \
  -H "Content-Type: application/json" \
  -d '{
    "paymentMethod": 0,
    "products": [
      {
        "productId": "123e4567-e89b-12d3-a456-426614174000",
        "quantity": 2
      }
    ]
  }'
```

**Confirmar Pagamento:**
```bash
curl -X POST http://localhost:8082/api/order/confirm-payment \
  -H "Content-Type: application/json" \
  -d '{
    "orderId": "123e4567-e89b-12d3-a456-426614174000",
    "paymentStatus": "approved",
    "paymentId": "PAY-123456"
  }'
```

**Listar Pedidos Prontos:**
```bash
curl -X GET "http://localhost:8082/api/order/ready?page=0&pageSize=10"
```

### Status de Pedidos

| Código | Status | Descrição |
|--------|--------|-----------|
| `1` | CREATED | Pedido criado |
| `2` | RECEIVED | Pagamento confirmado |
| `3` | PREPARING | Em preparação |
| `4` | READY | Pronto para retirada |
| `5` | FINISHED | Finalizado |
| `6` | REJECTED | Rejeitado |

### Health Check

```bash
# Verificar saúde da aplicação
curl http://localhost:8082/actuator/health

# Métricas
curl http://localhost:8082/actuator/metrics
```

---

## 🔐 Variáveis de Ambiente

### Banco de Dados

| Variável | Descrição | Padrão |
|----------|-----------|--------|
| `SPRING_DATASOURCE_URL` | URL do banco MySQL | `jdbc:mysql://localhost:3308/manafood_order_db` |
| `SPRING_DATASOURCE_USERNAME` | Usuário do banco | `root` |
| `SPRING_DATASOURCE_PASSWORD` | Senha do banco | `root` |

### Serviços Externos

| Variável | Descrição | Padrão |
|----------|-----------|--------|
| `MANAFOODPRODUCT_SERVICE_URL` | URL do microsserviço de produtos | `http://localhost:8081` |

### Aplicação

| Variável | Descrição | Padrão |
|----------|-----------|--------|
| `SERVER_PORT` | Porta da aplicação | `8082` |
| `SPRING_PROFILES_ACTIVE` | Profile ativo | `local` |

---

## 🐳 Docker

### Construir imagem

```bash
docker build -t mana-food-order:latest .
```

### Executar container

```bash
docker run -p 8082:8082 \
  -e SPRING_DATASOURCE_URL=jdbc:mysql://host.docker.internal:3308/manafood_order_db \
  -e SPRING_DATASOURCE_USERNAME=root \
  -e SPRING_DATASOURCE_PASSWORD=root \
  mana-food-order:latest
```

### Docker Compose

```bash
# Subir todos os serviços
docker-compose up -d

# Ver logs
docker-compose logs -f

# Parar serviços
docker-compose down

# Limpar volumes
docker-compose down -v
```

---

## 🔄 CI/CD

O projeto utiliza **GitHub Actions** para CI/CD.

### Pipeline

1. **Build & Test** - Compila e executa testes
2. **SonarCloud** - Análise de qualidade de código
3. **JaCoCo** - Gera relatório de cobertura
4. **Docker Build** - Constrói imagem Docker

### Workflow

```yaml
name: Build and Test

on:
  push:
    branches: [ main, develop ]
  pull_request:
    branches: [ main ]

jobs:
  build:
    runs-on: ubuntu-latest
    steps:
      - uses: actions/checkout@v3
      - name: Set up JDK 17
        uses: actions/setup-java@v3
      - name: Build with Maven
        run: mvn clean verify
      - name: SonarCloud Scan
        run: mvn sonar:sonar
```

---

## 📊 Qualidade de Código

### SonarCloud

[![Quality Gate Status](https://sonarcloud.io/api/project_badges/measure?project=mana-food_mana-food-microsservice-order&metric=alert_status)](https://sonarcloud.io/summary/new_code?id=mana-food_mana-food-microsservice-order)
[![Coverage](https://sonarcloud.io/api/project_badges/measure?project=mana-food_mana-food-microsservice-order&metric=coverage)](https://sonarcloud.io/summary/new_code?id=mana-food_mana-food-microsservice-order)
[![Bugs](https://sonarcloud.io/api/project_badges/measure?project=mana-food_mana-food-microsservice-order&metric=bugs)](https://sonarcloud.io/summary/new_code?id=mana-food_mana-food-microsservice-order)

Acesse: [SonarCloud Dashboard](https://sonarcloud.io/dashboard?id=mana-food_mana-food-microsservice-order)

### Métricas de Qualidade

- ✅ Cobertura de testes > 80%
- ✅ 0 bugs críticos
- ✅ 0 vulnerabilidades
- ✅ Code smells < 5
- ✅ Duplicação de código < 3%

### Executar análise local

```bash
mvn clean verify sonar:sonar \
  -Dsonar.projectKey=mana-food_mana-food-microsservice-order \
  -Dsonar.organization=mana-food \
  -Dsonar.host.url=https://sonarcloud.io \
  -Dsonar.login=YOUR_SONAR_TOKEN
```

---

## 🤝 Contribuindo

Contribuições são bem-vindas! Por favor, siga estas diretrizes:

1. **Fork** o projeto
2. Crie uma **branch** para sua feature (`git checkout -b feature/AmazingFeature`)
3. **Commit** suas mudanças (`git commit -m 'Add some AmazingFeature'`)
4. **Push** para a branch (`git push origin feature/AmazingFeature`)
5. Abra um **Pull Request**

### Padrões de Código

- Siga as convenções do Kotlin
- Escreva testes para novas funcionalidades
- Mantenha cobertura de testes > 80%
- Use commits semânticos (feat, fix, docs, refactor, test, chore)

### Commits Semânticos

```
feat: Adiciona novo endpoint de consulta
fix: Corrige validação de pagamento
docs: Atualiza documentação da API
refactor: Melhora estrutura do use case
test: Adiciona testes BDD para pedidos
chore: Atualiza dependências
```

---

## 📄 Licença

Este projeto está sob a licença MIT. Veja o arquivo [LICENSE](LICENSE) para mais detalhes.

---

## 👥 Equipe

- **ManaFood Team** - [GitHub Organization](https://github.com/mana-food)

---

## 📞 Contato

- **Issues**: [GitHub Issues](https://github.com/mana-food/mana-food-microsservice-order-app/issues)
- **Discussions**: [GitHub Discussions](https://github.com/mana-food/mana-food-microsservice-order-app/discussions)

---

## 🔗 Links Úteis

- [Microsserviço de Produtos](https://github.com/mana-food/mana-food-microsservice-produto-app)
- [Documentação Geral do ManaFood](https://github.com/mana-food)
- [Spring Boot Documentation](https://docs.spring.io/spring-boot/docs/current/reference/html/)
- [Kotlin Documentation](https://kotlinlang.org/docs/home.html)
- [Clean Architecture](https://blog.cleancoder.com/uncle-bob/2012/08/13/the-clean-architecture.html)

---

<div align="center">
  <p>Desenvolvido com ❤️ pela equipe ManaFood</p>
  <p>⭐ Dê uma estrela se este projeto te ajudou!</p>
</div>

