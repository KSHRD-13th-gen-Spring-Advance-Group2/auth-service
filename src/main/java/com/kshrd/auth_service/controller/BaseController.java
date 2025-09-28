package com.kshrd.auth_service.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import com.kshrd.auth_service.model.dto.response.ApiResponse;

public abstract class BaseController {
    public <T> ResponseEntity<ApiResponse<T>> responseEntity(String message, HttpStatus httpStatus,
            T payload) {
        ApiResponse<T> response = ApiResponse.<T>builder()
                .success(true)
                .payload(payload)
                .message(message)
                .build();
        return ResponseEntity.status(httpStatus).body(response);
    }

    public <T> ResponseEntity<ApiResponse<T>> responseEntity(String message, T payload) {
        return responseEntity(message, HttpStatus.OK, payload);
    }

    public <T> ResponseEntity<ApiResponse<T>> responseEntity(String message) {
        return responseEntity(message, HttpStatus.OK, null);
    }
}