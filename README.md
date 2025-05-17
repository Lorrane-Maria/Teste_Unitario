
# Spring Boot User Management API

## Descrição

Este projeto é uma API RESTful desenvolvida com Spring Boot para gerenciar usuários, implementada como parte da prática Testes Unitários e Mocking em Spring Boot. A API permite criar, listar, buscar, atualizar e deletar usuários, com validações de entrada e tratamento de erros. Inclui testes unitários para o controlador usando JUnit 5 e Mockito, além de um teste de integração para o contexto Spring. O banco de dados é o MariaDB, configurado com Spring Data JPA.

## Objetivo

Implementar e validar testes unitários para o controlador de usuários (`Usuariocontroller`) em uma aplicação Spring Boot, utilizando JUnit 5 e Mockito. A API é robusta, com validação de entrada, unicidade de email, e tratamento centralizado de exceções.

## Tecnologias Utilizadas

- Java: 11
- Spring Boot: 3.3.4
- Spring Web: Endpoints REST
- Spring Data JPA: Persistência
- MariaDB: Banco de dados
- Spring Boot DevTools: Desenvolvimento
- JUnit 5: Testes unitários
- Mockito: Mock de dependências
- Lombok: Redução de código
- Maven: Gerenciador de dependências

## Estrutura do Projeto

```
src/
├── main/
│   ├── java/com/example/demo/
│   │   ├── DemoApplication.java          # Classe principal
│   │   ├── Model/
│   │   │   └── Usuario.java             # Entidade JPA
│   │   ├── Repository/
│   │   │   └── UsuarioRepository.java   # Repositório JPA
│   │   ├── Service/
│   │   │   └── UsuarioService.java      # Lógica de negócio
│   │   ├── controller/
│   │   │   ├── Usuariocontroller.java   # Controlador REST
│   │   │   └── GlobalExceptionHandler.java # Tratamento de exceções
│   └── resources/
│       └── application.properties       # Configurações
├── test/
│   └── java/com/example/demo/
│       ├── DemoApplicationTests.java    # Teste de integração
│       └── UsuarioControllerTest.java   # Testes unitários
pom.xml                                  # Configuração do Maven
README.md                                # Documentação
```

## Pré-requisitos

- Java 11: Verifique com `java -version`.
- Maven: Verifique com `mvn -version`.
- MariaDB: Banco de dados instalado.
- Git: Para clonar o repositório (opcional).
- IDE: IntelliJ IDEA, Eclipse, ou VS Code (opcional).

## Configuração do Ambiente

Clone o repositório (se hospedado no GitHub):

```bash
git clone https://github.com/seu-usuario/seu-repositorio.git
cd seu-repositorio
```

Configure o MariaDB:

```bash
# Linux
sudo apt install mariadb-server
sudo systemctl start mariadb

# Mac
brew install mariadb
brew services start mariadb
```

Crie o banco `demo`:

```bash
mysql -u root -p
CREATE DATABASE demo;
EXIT;
```

Edite `src/main/resources/application.properties`:

```properties
spring.application.name=demo
spring.datasource.url=jdbc:mariadb://localhost:3306/demo
spring.datasource.username=root
spring.datasource.password=senha
spring.datasource.driver-class-name=org.mariadb.jdbc.Driver
spring.jpa.hibernate.ddl-auto=update
spring.jpa.show-sql=true
server.port=${PORT:8080}
```

Substitua `senha` pela sua senha.

Instale as dependências:

```bash
mvn clean install
```

## Executando os Testes

O projeto inclui 10 testes unitários (`UsuarioControllerTest.java`) e 1 teste de integração (`DemoApplicationTests.java`).

Via Maven:

```bash
mvn clean test
```

Saída esperada:

```
[INFO] Tests run: 11, Failures: 0, Errors: 0, Skipped: 0
[INFO] BUILD SUCCESS
```

Via IDE:

- Abra o projeto na IDE.
- Execute `UsuarioControllerTest.java` e `DemoApplicationTests.java`.

## Executando a Aplicação

```bash
mvn spring-boot:run
```

Disponível em `http://localhost:8080`.

## Teste dos Endpoints

### POST /usuarios (Criar):

```bash
curl -X POST http://localhost:8080/usuarios -H "Content-Type: application/json" -d '{"nome":"João Silva","email":"joao@example.com"}'
```

### GET /usuarios (Listar):

```bash
curl http://localhost:8080/usuarios
```

### GET /usuarios/{id} (Buscar):

```bash
curl http://localhost:8080/usuarios/1
```

### PUT /usuarios/{id} (Atualizar):

```bash
curl -X PUT http://localhost:8080/usuarios/1 -H "Content-Type: application/json" -d '{"nome":"João Atualizado","email":"joao_atualizado@example.com"}'
```

### DELETE /usuarios/{id} (Deletar):

```bash
curl -X DELETE http://localhost:8080/usuarios/1
```

### Validação (Email inválido):

```bash
curl -X POST http://localhost:8080/usuarios -H "Content-Type: application/json" -d '{"nome":"Maria","email":"invalid_email"}'
```

## Rodando Online com Render

1. Crie um repositório no GitHub:

```bash
git add .
git commit -m "Initial commit"
git push origin main
```

2. No Render:

- Crie um Web Service.
- Conecte o repositório.
- Configure:

```
Build Command: mvn clean package -DskipTests
Start Command: java -jar target/demo-0.0.1-SNAPSHOT.jar
```

- Adicione um banco MariaDB e atualize `application.properties`.
- Deploy e acesse a URL (ex.: https://seu-app.onrender.com).

### GitHub Actions (CI):

```yaml
name: CI
on:
  push:
    branches: [ main ]
jobs:
  test:
    runs-on: ubuntu-latest
    steps:
      - uses: actions/checkout@v3
      - name: Set up JDK 11
        uses: actions/setup-java@v3
        with:
          java-version: '11'
      - name: Run tests
        run: mvn clean test
```

## Solução de Problemas

- **Failed to configure a DataSource**: Verifique `application.properties` e a existência do banco.
- **Testes falham**: Rode `mvn clean install` novamente.
- **Connection refused**: Verifique se a aplicação está rodando e porta 8080 está livre.
- **Logs**: Veja os logs para comandos SQL ou erros.
