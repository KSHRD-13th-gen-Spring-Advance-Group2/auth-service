package com.kshrd.auth_service;

import io.swagger.v3.oas.annotations.info.Contact;
import io.swagger.v3.oas.annotations.info.License;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.enums.SecuritySchemeIn;
import io.swagger.v3.oas.annotations.enums.SecuritySchemeType;
import io.swagger.v3.oas.annotations.info.Info;
import io.swagger.v3.oas.annotations.security.SecurityScheme;
import io.swagger.v3.oas.annotations.servers.Server;

@SpringBootApplication
@SecurityScheme(name = "bearerAuth", type = SecuritySchemeType.HTTP, scheme = "bearer", in = SecuritySchemeIn.HEADER)
@OpenAPIDefinition(
        info = @Info(
                title = "Auth Service API",
                version = "1.0.0",
                description = "Endpoints for authentication and managing user information.",
                contact = @Contact(
                        name = "Contact KSGA Advance Course Group 2",
                        url = "https://github.com/KSHRD-13th-gen-Spring-Advance-Group2/auth-service"
                ),
                license = @License(
                        name = "KSGA Student 2.0",
                        url = "https://github.com/KSHRD-13th-gen-Spring-Advance-Group2/auth-service"
                )
        ),
        servers = {
                @Server(url = "/")
        }
)
public class AuthServiceApplication {

	public static void main(String[] args) {
		SpringApplication.run(AuthServiceApplication.class, args);
	}

}
