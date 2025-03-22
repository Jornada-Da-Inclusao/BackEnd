package com.fatec.configuration;

import org.springdoc.core.customizers.OpenApiCustomizer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import io.swagger.v3.oas.models.ExternalDocumentation;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.info.License;
import io.swagger.v3.oas.models.responses.ApiResponse;
import io.swagger.v3.oas.models.responses.ApiResponses;

@Configuration  // Anotação que define esta classe como uma classe de configuração do Spring
public class SwaggerConfig {

    // Definindo o OpenAPI personalizado para a documentação da API
    @Bean  // Indica que este método é um bean que será gerido pelo Spring
    OpenAPI springBlogPessoalOpenAPI() {
        // Configura as informações básicas da API para exibição no Swagger UI
        return new OpenAPI()
                .info(new Info()
                        .title("Jornada da Inclusao")  // Título da API
                        .description("Projeto Jornada da Inclusao")  // Descrição da API
                        .version("v0.0.1")  // Versão da API
                        .license(new License()  // Detalhes sobre a licença
                                .name("Projeto Jornada da Inclusao")  // Nome da licença
                                .url("https://github.com/jornadadainclusao"))  // URL da licença
                        .contact(new Contact()  // Detalhes de contato
                                .name("Projeto Jornada da Inclusao")  // Nome de contato
                                .url("https://github.com/jornadadainclusao")  // URL de contato
                                .email("jornadadainclusao@gmail.com")))  // E-mail de contato
                .externalDocs(new ExternalDocumentation()  // Documentação externa relacionada ao projeto
                        .description("Github")  // Descrição da documentação externa
                        .url("https://github.com/jornadadainclusao/BackEnd"));  // URL da documentação externa
    }

    // Definindo um customizador de OpenAPI para configurar as respostas globais da API
    @Bean  // Indica que este método é um bean que será gerido pelo Spring
    OpenApiCustomizer customerGlobalHeaderOpenApiCustomiser() {
        return openApi -> {
            // Itera sobre todas as operações nos caminhos da API e configura as respostas
            openApi.getPaths().values().forEach(pathItem -> pathItem.readOperations().forEach(operation -> {

                ApiResponses apiResponses = operation.getResponses();

                // Adiciona as respostas HTTP padrão para cada operação da API
                apiResponses.addApiResponse("200", createApiResponse("Sucesso!"));
                apiResponses.addApiResponse("201", createApiResponse("Objeto Persistido!"));
                apiResponses.addApiResponse("204", createApiResponse("Objeto Excluído!"));
                apiResponses.addApiResponse("400", createApiResponse("Erro na Requisição!"));
                apiResponses.addApiResponse("401", createApiResponse("Acesso Não Autorizado!"));
                apiResponses.addApiResponse("403", createApiResponse("Acesso Proibido!"));
                apiResponses.addApiResponse("404", createApiResponse("Objeto Não Encontrado!"));
                apiResponses.addApiResponse("500", createApiResponse("Erro na Aplicação!"));
            }));
        };
    }

    // Método auxiliar para criar uma resposta API com uma descrição fornecida
    private ApiResponse createApiResponse(String message) {
        return new ApiResponse().description(message);  // Cria uma resposta com a descrição fornecida
    }
}
