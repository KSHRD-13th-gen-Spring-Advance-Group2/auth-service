package com.kshrd.auth_service.service;

import com.kshrd.auth_service.model.dto.request.AuthRequest;
import com.kshrd.auth_service.model.dto.request.RefreshTokenRequest;
import com.kshrd.auth_service.model.dto.response.AuthResponse;

public interface AuthService {

    AuthResponse login(AuthRequest request);

    AuthResponse refreshToken(RefreshTokenRequest request);

	void logout(RefreshTokenRequest request);
    
}
