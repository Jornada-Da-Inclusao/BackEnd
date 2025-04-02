# PI - Jornada da Inclusão

## Index

- [About](#about)
- [SDGs](#sdgs)
- [Team Members](#team-members)
- [Technologies](#technologies)
  - [Front-End](#front-end)
  - [Back-End](#back-end)
    - [Spring Security](#sprint-security)
    - [JWT (JSON Web Token)](#jwt-json-web-token)
    - [Swagger/OpenAPI](#swagger-open-api)
    - [Banco de Dados](#banco-de-dados)
  - [IDEs](#ides)
  - [Frameworks](#frameworks)
  - [Hosting](#hosting)
- [Installation](#installation)
  - [Requirements](#requirements)
- [Endpoints](#endpoints)
- [Hosting](#hosting)
- [Final Considerations](#final-considerations)

## Sobre

The Journey of Inclusion project is developed with the goal of identifying and meeting the specific needs of each child who has difficulties with inclusion in school environments.  
It utilizes a combination of interactive and personalized assessments, as well as teaching methods with resources and activities that primarily assist in literacy processes and number identification.  
The main objective is improving children individual performances, thereby promoting quality education and reducing educational inequality, aiming to achieve the fourth Sustainable Development Goal (SDG).

## ODS

Este projeto visa contribuir diretamente para os seguintes ODS:

<a href="https://brasil.un.org/pt-br/sdgs/4" target="_blank">
    <img src="https://brasil.un.org/profiles/undg_country/themes/custom/undg/images/SDGs/pt-br/SDG-4.svg" alt="alt text" width="200" style="margin-right: 10px;">
</a>

- **Objetivo 4**: Assegurar a educação inclusiva e equitativa e de qualidade, e promover oportunidades de aprendizagem ao longo da vida para todas e todos.

## Integrantes

- [Luciana Guedes de Araújo](https://github.com/Luciana-Guedes-de-Araujo) 
- [Manuela Tenorio da Silva](https://github.com/ManuelaTenorio)
- [Marcos Vinícius de Oliveira](https://github.com/ViniMarkos283)
- [Pedro Henrique Santos Bernardo](https://github.com/Pedro-HSB)
- [Renato W. de Lima Jacob](https://github.com/renatowljacob)

## Tecnologias

### Front-End

[![My Skills](https://skillicons.dev/icons?i=html,css,js,ts,react,vite&perline=3)](https://skillicons.dev)

### Back-End

[![My Skills](https://skillicons.dev/icons?i=java,spring,postgresql&perline=3)](https://skillicons.dev)

#### Spring Security
Implementação de segurança para autenticação e autorização de usuários.
#### JWT (JSON Web Token)
Sistema de autenticação usando tokens para garantir a segurança das requisições.
#### Swagger/OpenAPI
Para gerar a documentação interativa da API.
#### Banco de Dados
Utilização do JPA para persistência de dados e H2 como banco de dados em memória (pode ser facilmente alterado para outros bancos de dados como MySQL ou PostgreSQL).

### IDEs

[![My Skills](https://skillicons.dev/icons?i=idea,vscode,neovim&perline=3)](https://skillicons.dev) 

### Frameworks

[![My Skills](https://skillicons.dev/icons?i=spring&perline=3)](https://skillicons.dev)

### Hosting

[![My Skills](https://skillicons.dev/icons?i=firebase&perline=3)](https://skillicons.dev)

## Instalação

### Pré-requisitos

- Java 17 ou superior
- Maven

Este projeto usa Maven. Para utilizá-lo, execute:
```bash
mvn spring-boot:run
```

O projeto estará ativo no endereço http://localhost:8080.

## Endpoints

A API oferece os seguintes endpoints:

    POST /usuarios/cadastrar: Cadastrar um novo usuário.
    POST /usuarios/logar: Autenticar um usuário (gera um token JWT).
    GET /usuarios/all: Retorna todos os usuários cadastrados.
    GET /usuarios/{id}: Retorna as informações de um usuário específico.
    PUT /usuarios/atualizar: Atualiza as informações de um usuário.

Para acessar a documentação da API, acesse: http://localhost:8080/swagger-ui/index.html.

## Hospedagem

O sistema encontra-se hospedado no Render, podendo ser acessado através do seguinte link:
https://backend-9qjw.onrender.com/

## Considerações Finais

Nós, colaboradores do projeto 'Jornada da Inclusão', agradecemos a contribuição e a orientação dos docentes das disciplinas que participaram desse projeto Integrador, e aos nossos colegas de turma e de outros ciclos que prestigiaram nossa apresentação e acessaram esse repositório para compreender melhor nosso projeto.
