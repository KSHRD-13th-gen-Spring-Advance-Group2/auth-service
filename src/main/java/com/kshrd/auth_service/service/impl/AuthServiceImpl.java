package com.kshrd.auth_service.service.impl;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestClient;
import org.springframework.web.client.RestClientException;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.kshrd.auth_service.exception.KeycloakUnauthorizedException;
import com.kshrd.auth_service.exception.CustomServiceUnavailableException;
import com.kshrd.auth_service.keycloak.KeycloakPropertiesConfig;
import com.kshrd.auth_service.model.dto.request.AuthRequest;
import com.kshrd.auth_service.model.dto.request.RefreshTokenRequest;
import com.kshrd.auth_service.model.dto.response.AuthResponse;
import com.kshrd.auth_service.model.dto.response.KeycloakUnauthorizedErrorResponse;
import com.kshrd.auth_service.service.AuthService;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Service
@RequiredArgsConstructor
@Slf4j
public class AuthServiceImpl implements AuthService {
    private final KeycloakPropertiesConfig keycloakPropertiesConfig;
    private final ObjectMapper objectMapper;

    @Qualifier("keycloakRestClient")
    private final RestClient keycloakRestClient;

    private String getOidcUrl() {
        return "/realms/" + keycloakPropertiesConfig.getRealm() + "/protocol/openid-connect";
    }

    private void throwKeycloakUnauthorizedException(HttpClientErrorException e) {
        try {
            KeycloakUnauthorizedErrorResponse keycloakError = objectMapper.readValue(e.getResponseBodyAsString(),
                    KeycloakUnauthorizedErrorResponse.class);
            throw new KeycloakUnauthorizedException(keycloakError.getErrorDescription(), keycloakError.getError());
        } catch (JsonProcessingException jsonException) {
            log.error("Failed to parse Keycloak error response for user: {}", e.getResponseBodyAsString());
            throw new RuntimeException(
                    "Authentication failed with unparseable error: " + e.getResponseBodyAsString(), e);
        }
    }

    private AuthResponse handleKeycloakTokenEndpointRequest(MultiValueMap<String, String> formData) {
        try {
            ResponseEntity<AuthResponse> response = keycloakRestClient
                    .post()
                    .uri(getOidcUrl() + "/token")
                    .contentType(MediaType.APPLICATION_FORM_URLENCODED)
                    .body(formData)
                    .retrieve()
                    .toEntity(AuthResponse.class);

            return response.getBody();
        } catch (HttpClientErrorException e) {
            throwKeycloakUnauthorizedException(e);
            throw new RuntimeException("Should be unreachable");
        } catch (RestClientException e) {
            throw new CustomServiceUnavailableException("Keycloak server is unavailable");
        }
    }

    @Override
    public AuthResponse login(AuthRequest request) {
        MultiValueMap<String, String> formData = new LinkedMultiValueMap<>();
        formData.add("grant_type", "password");
        formData.add("client_id", keycloakPropertiesConfig.getApplication().getClientId());
        formData.add("client_secret", keycloakPropertiesConfig.getApplication().getClientSecret());
        formData.add("username", request.getEmail());
        formData.add("password", request.getPassword());
        formData.add("scope", "openid");

        return handleKeycloakTokenEndpointRequest(formData);
    }

    @Override
    public AuthResponse refreshToken(RefreshTokenRequest request) {
        MultiValueMap<String, String> formData = new LinkedMultiValueMap<>();
        formData.add("grant_type", "refresh_token");
        formData.add("client_id", keycloakPropertiesConfig.getApplication().getClientId());
        formData.add("client_secret", keycloakPropertiesConfig.getApplication().getClientSecret());
        formData.add("refresh_token", request.getRefreshToken());

        return handleKeycloakTokenEndpointRequest(formData);
    }

    @Override
    public void logout(RefreshTokenRequest request) {
        MultiValueMap<String, String> formData = new LinkedMultiValueMap<>();
        formData.add("client_id", keycloakPropertiesConfig.getApplication().getClientId());
        formData.add("client_secret", keycloakPropertiesConfig.getApplication().getClientSecret());
        formData.add("refresh_token", request.getRefreshToken());

        try {
            ResponseEntity<Void> response = keycloakRestClient
                    .post()
                    .uri(getOidcUrl() + "/logout")
                    .contentType(MediaType.APPLICATION_FORM_URLENCODED)
                    .body(formData)
                    .retrieve().toBodilessEntity();

        } catch (HttpClientErrorException e) {
            throwKeycloakUnauthorizedException(e);
            throw new RuntimeException("Should be unreachable");
        } catch (RestClientException e) {
            throw new CustomServiceUnavailableException("Keycloak server is unavailable");
        }
    }
}
