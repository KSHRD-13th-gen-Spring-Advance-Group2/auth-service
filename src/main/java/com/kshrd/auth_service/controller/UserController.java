package com.kshrd.auth_service.controller;

import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.kshrd.auth_service.model.dto.request.ChangePasswordRequest;
import com.kshrd.auth_service.model.dto.request.UpdateUserRequest;
import com.kshrd.auth_service.model.dto.request.UserRequest;
import com.kshrd.auth_service.model.dto.response.ApiResponse;
import com.kshrd.auth_service.model.entity.User;
import com.kshrd.auth_service.service.UserService;

import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/v1/users")
@RequiredArgsConstructor
public class UserController extends BaseController {
    private final UserService userService;

    @SecurityRequirement(name = "bearerAuth")
    @GetMapping
    public ResponseEntity<ApiResponse<User>> getUser() {
        return responseEntity("Get user successfully", userService.getUser());
    }

    @SecurityRequirement(name = "bearerAuth")
    @PutMapping
    public ResponseEntity<ApiResponse<User>> updateUser(@RequestBody @Valid UpdateUserRequest request) {
        return responseEntity("Update user info successfully", userService.updateUser(request));
    }

    @PostMapping
    public ResponseEntity<ApiResponse<User>> registerNewUser(@RequestBody @Valid UserRequest request) {
        return responseEntity("Register new user successfully", HttpStatus.CREATED,
                userService.registerNewUser(request));
    }

    @SecurityRequirement(name = "bearerAuth")
    @PutMapping("/password")
    public ResponseEntity<ApiResponse<User>> changeUserPassword(@RequestBody @Valid ChangePasswordRequest request) {
        return responseEntity("Change password successfully", userService.changeUserPassword(request));
    }
}