package com.kshrd.auth_service.model.dto.response;

import lombok.Data;
import lombok.NoArgsConstructor;

import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.AllArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class KeycloakUnauthorizedErrorResponse {
    private String error;

    @JsonProperty("error_description")
    private String errorDescription;
}