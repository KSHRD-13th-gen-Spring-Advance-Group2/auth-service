package com.kshrd.auth_service.keycloak;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

import lombok.Data;
import lombok.Getter;
import lombok.Setter;

@Data
@ConfigurationProperties(prefix = "keycloak", ignoreUnknownFields = false)
@Component
public class KeycloakPropertiesConfig {
    private String realm;
    private String endpoint;
    private Application application;

    @Getter
    @Setter
    public static class Application {
        private String clientId;
        private String clientSecret;
    }
}
