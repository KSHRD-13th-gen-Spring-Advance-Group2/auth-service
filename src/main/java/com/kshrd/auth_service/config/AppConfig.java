package com.kshrd.auth_service.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.client.RestClient;
import org.springframework.web.client.RestTemplate;

import com.kshrd.auth_service.keycloak.KeycloakPropertiesConfig;

@Configuration
public class AppConfig {
    @Bean
    public RestClient keycloakRestClient(RestClient.Builder builder,
            KeycloakPropertiesConfig keycloakPropertiesConfig) {
        return builder.baseUrl(keycloakPropertiesConfig.getEndpoint()).build();
    }
}

