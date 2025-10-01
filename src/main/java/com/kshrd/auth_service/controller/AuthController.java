package com.kshrd.auth_service.controller;

import jakarta.validation.Valid;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.kshrd.auth_service.model.dto.request.AuthRequest;
import com.kshrd.auth_service.model.dto.request.RefreshTokenRequest;
import com.kshrd.auth_service.model.dto.response.ApiResponse;
import com.kshrd.auth_service.model.dto.response.AuthResponse;
import com.kshrd.auth_service.service.AuthService;

import lombok.RequiredArgsConstructor;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;


@RestController
@RequestMapping("/api/v1/auths")
@RequiredArgsConstructor
public class AuthController extends BaseController {
    private final AuthService authService;

    @PostMapping("refresh")
    public ResponseEntity<ApiResponse<AuthResponse>> refreshToken(@RequestBody @Valid RefreshTokenRequest request) {
        return responseEntity("Refresh token successfully", authService.refreshToken(request));
    }

    @PostMapping("logout")
    public ResponseEntity<ApiResponse<Void>> logout(@RequestBody @Valid RefreshTokenRequest request) {
        authService.logout(request);
        return responseEntity("Logout successfully");
    }
    
    @PostMapping("login")
    public ResponseEntity<ApiResponse<AuthResponse>> login(@RequestBody @Valid AuthRequest request) {
        return responseEntity("Login successfully", authService.login(request));
    }
}
