package com.chatop.securityconfigs.openapi_config;

import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.models.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.info.Contact;
import io.swagger.v3.oas.annotations.info.Info;
import io.swagger.v3.oas.annotations.info.License;

import io.swagger.v3.oas.annotations.servers.Server;
import io.swagger.v3.oas.models.Components;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.security.SecurityScheme;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
@OpenAPIDefinition(
        info = @Info(
                title = "ChâTop Rental API",
                version = "v1.0.0",
                contact = @Contact(
                        name = "OpenClassrooms", email = "chatop@chatopcontact.com", url = "https://openclassrooms.com/fr/"
                ),
                license = @License(
                        name = "Apache 2.0", url = "https://www.apache.org/licenses/LICENSE-2.0"
                ),
                termsOfService = "ChâTop web site",
                description = "This API allows to manage Rental, Users and Messages. Download REST API Doc file : <a href='http://localhost:3001/v3/api-docs.yaml'>FILE YAML</a>"
        ),
        servers = {
                @Server(url = "http://localhost:3001", description = "Development backend server"),
                @Server(url = "https://chatopt.com", description = "Production backend server")
        }
)
public class OpenAPI3Configuration {
    @Bean
    public OpenAPI customizeOpenAPI() {

        final String securitySchemeName = "Bearer Token";
        return new OpenAPI()
                .addSecurityItem(new SecurityRequirement()
                        .addList(securitySchemeName))
                .components(new Components()
                        .addSecuritySchemes(securitySchemeName, new SecurityScheme()
                                .name(securitySchemeName)
                                .type(SecurityScheme.Type.HTTP)
                                .in(SecurityScheme.In.HEADER)
                                .scheme("bearer")
                                .description(
                                        "Provide the JWT token. JWT token can be obtained from the <strong>Login API</strong>. For testing, use the credentials <strong>Email: test@test.com/password: test!31</strong>")
                                .bearerFormat("JWT")));

    }
}
