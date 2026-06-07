package com.ordep.aspmanagerauthservice;

import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.enums.SecuritySchemeIn;
import io.swagger.v3.oas.annotations.enums.SecuritySchemeType;
import io.swagger.v3.oas.annotations.info.Contact;
import io.swagger.v3.oas.annotations.info.Info;
import io.swagger.v3.oas.annotations.info.License;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.security.SecurityScheme;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.openfeign.EnableFeignClients;

@SpringBootApplication
@OpenAPIDefinition(
        info = @Info(
                title = "ASPManager API - Autenticação",
                version = "1.0.0",
                description = "API REST para autenticação e autorização do aspmanager",
                contact = @Contact(
                        name = "Pedro Silva - dev backend do ASPManager",
                        email = "pedrooliveira.silva@ucsal.edu.br"
                ),
                license = @License(
                        name = "Uso acadêmico interno UCSAL"
                )
        ),
        security = @SecurityRequirement(name = "bearerAuth"),
        servers = {
        @Server(url = "http://localhost:8081", description = "Ambiente Local (Desenvolvimento)"),
        @Server(url = "http://localhost:8080", description = "API Gateway (Produção)")
    }
)
@EnableFeignClients
@EnableDiscoveryClient
public class AspmanagerAuthServiceApplication {

    public static void main(String[] args) {
        SpringApplication.run(AspmanagerAuthServiceApplication.class, args);
    }

}
