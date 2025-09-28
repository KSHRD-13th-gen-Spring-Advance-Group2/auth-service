package com.kshrd.auth_service.model.dto.request;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class UserRequest {
    private String email;
    private String firstName;
    private String lastName;
    private String username;
    private String password;
    private String imageUrl;
}
