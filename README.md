Projeto Jornada da Inclusão

Jornada da Inclusão é um projeto dedicado a desenvolver jogos educativos interativos que ajudam crianças com dificuldades escolares a melhorar suas habilidades acadêmicas. O objetivo é proporcionar um ambiente inclusivo, no qual todas as crianças, incluindo aquelas com necessidades especiais como deficiências auditivas, visuais, cognitivas, entre outras, possam aprender a ler, escrever e desenvolver suas competências em diversas disciplinas de maneira divertida e acessível.
Tecnologias Utilizadas

    Spring Boot: Framework para o desenvolvimento da API backend.
    Spring Security: Implementação de segurança para autenticação e autorização de usuários.
    JWT (JSON Web Token): Sistema de autenticação usando tokens para garantir a segurança das requisições.
    Swagger/OpenAPI: Para gerar a documentação interativa da API.
    Banco de Dados: Utilização do JPA para persistência de dados e H2 como banco de dados em memória (pode ser facilmente alterado para outros bancos de dados como MySQL ou PostgreSQL).

Funcionalidades

A API do projeto oferece diversas funcionalidades essenciais para o sistema, incluindo:

    Cadastro de Usuários: Permite que novos usuários (crianças ou educadores) se cadastrem na plataforma.
    Autenticação de Usuários: Uso de JWT para login e segurança na comunicação entre o cliente e o servidor.
    Acesso aos Jogos: Funcionalidades para interação com os jogos educativos para crianças.
    Gestão de Perfis: As crianças podem atualizar seus perfis, incluir suas fotos e informações pessoais.
    Acessibilidade: O sistema foi desenvolvido para ser acessível para crianças com deficiências auditivas, visuais, cognitivas e outras necessidades especiais.

Objetivo do Projeto

O principal objetivo do Jornada da Inclusão é fornecer uma plataforma onde crianças com dificuldades escolares possam aprender de maneira interativa e inclusiva. Isso inclui:

    Aprendizado de leitura e escrita: Jogos que estimulam a aprendizagem de leitura e escrita de forma divertida.
    Apoio a crianças com necessidades especiais: Jogos projetados para crianças com deficiências auditivas, visuais, cognitivas, entre outras.
    Melhoria no desempenho escolar: Oferecer recursos que ajudem na melhoria das notas escolares de forma lúdica.

Instalação
Pré-requisitos

    Java 17 ou superior
    Maven
    IDE (IntelliJ IDEA, Eclipse ou outra de sua preferência)

Passos para rodar o projeto localmente:

    Clone o repositório:

git clone https://github.com/jornadadainclusao/BackEnd.git

Navegue até o diretório do projeto:

cd BackEnd

Execute o projeto usando o Maven:

    mvn spring-boot:run

    O backend estará rodando em http://localhost:8080.

Endpoints

A API oferece os seguintes endpoints principais:

    POST /usuarios/cadastrar: Cadastrar um novo usuário.
    POST /usuarios/logar: Autenticar um usuário (gera um token JWT).
    GET /usuarios/all: Retorna todos os usuários cadastrados.
    GET /usuarios/{id}: Retorna as informações de um usuário específico.
    PUT /usuarios/atualizar: Atualiza as informações de um usuário.

Para acessar a documentação interativa da API, acesse http://localhost:8080/swagger-ui/index.html.
Funcionalidades Futuras

    Desenvolvimento de Jogos: Expansão dos jogos educativos com mais opções e funcionalidades.
    Aprimoramento da Acessibilidade: Melhorias contínuas para tornar os jogos ainda mais acessíveis para crianças com necessidades especiais.
    Suporte Multilíngue: Inclusão de múltiplos idiomas para alcançar um público ainda maior.
    Sistema de Feedback: Sistema para os educadores acompanharem o progresso das crianças.


Equipe

    Nome do Projeto: Jornada da Inclusão
    Desenvolvedores: 

      Luciana Guedes de Araújo
      Manuela Tenorio da Silva
      Marcos Vinícius de Oliveira
      Pedro Henrique Santos Bernardo
      Renato W. de Lima Jacob
      

Licença

Este projeto é licenciado sob a MIT License - veja o arquivo LICENSE para mais detalhes.
