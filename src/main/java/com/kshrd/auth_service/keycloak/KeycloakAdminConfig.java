package com.kshrd.auth_service.keycloak;

import org.keycloak.OAuth2Constants;
import org.keycloak.admin.client.Keycloak;
import org.keycloak.admin.client.KeycloakBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Configuration
public class KeycloakAdminConfig {
    private final KeycloakPropertiesConfig propertiesConfig;

    @Bean
    public Keycloak keycloak() {
        return KeycloakBuilder.builder()
                .serverUrl(propertiesConfig.getEndpoint())
                .realm(propertiesConfig.getRealm())
                .grantType(OAuth2Constants.CLIENT_CREDENTIALS)
                .clientId(propertiesConfig.getApplication().getClientId())
                .clientSecret(propertiesConfig.getApplication().getClientSecret())
                .build();
    }
}
