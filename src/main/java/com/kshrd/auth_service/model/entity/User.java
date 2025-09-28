package com.kshrd.auth_service.model.entity;

import org.springframework.security.oauth2.jwt.Jwt;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class User {
    private String id;
    private String username;
    private String email;
    private String firstName;
    private String lastName;
    private String imageUrl;

    public User(Jwt jwt) {
        this.id = jwt.getSubject();
        this.username = jwt.getClaimAsString("preferred_username");
        this.email = jwt.getClaimAsString("email");
        this.firstName = jwt.getClaimAsString("given_name");
        this.lastName = jwt.getClaimAsString("family_name");
        this.imageUrl = jwt.getClaimAsString("imageUrl");
    }
}
