package com.kshrd.auth_service.service.impl;

import java.text.MessageFormat;
import java.util.Collections;
import java.util.List;
import java.util.Map;

import org.keycloak.admin.client.Keycloak;
import org.keycloak.admin.client.resource.UsersResource;
import org.keycloak.representations.idm.CredentialRepresentation;
import org.keycloak.representations.idm.UserRepresentation;
import org.springframework.stereotype.Service;

import com.kshrd.auth_service.exception.DuplicateUserException;
import com.kshrd.auth_service.exception.UserCreationException;
import com.kshrd.auth_service.keycloak.KeycloakAdminConfig;
import com.kshrd.auth_service.keycloak.KeycloakPropertiesConfig;
import com.kshrd.auth_service.model.dto.request.UserRequest;
import com.kshrd.auth_service.model.entity.User;
import com.kshrd.auth_service.service.UserService;

import jakarta.ws.rs.ProcessingException;
import jakarta.ws.rs.core.Response;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Service
@RequiredArgsConstructor
@Slf4j
public class UserServiceImpl implements UserService {
    private final Keycloak keycloak;
    private final KeycloakPropertiesConfig keycloakPropertiesConfig;

    private UsersResource usersResourceInstance() {
        return keycloak.realm(keycloakPropertiesConfig.getRealm()).users();
    }

    private CredentialRepresentation buildCredentialRepresentation(String password) {
        CredentialRepresentation credentialRepresentation = new CredentialRepresentation();
        credentialRepresentation.setTemporary(false);
        credentialRepresentation.setType(CredentialRepresentation.PASSWORD);
        credentialRepresentation.setValue(password);
        return credentialRepresentation;
    }

    private UserRepresentation mapToUserRepresentation(UserRequest user) {
        UserRepresentation userRepresentation = new UserRepresentation();
        userRepresentation.setUsername(user.getUsername());
        userRepresentation.setCredentials(Collections.singletonList(buildCredentialRepresentation(user.getPassword())));
        userRepresentation.setEnabled(true);
        userRepresentation.setEmail(user.getEmail());
        userRepresentation.setFirstName(user.getFirstName());
        userRepresentation.setLastName(user.getLastName());
        userRepresentation.setEmailVerified(true);

        Map<String, List<String>> customAttributes = Map.of(
                "imageUrl", Collections.singletonList(user.getImageUrl()));

        userRepresentation.setAttributes(customAttributes);

        return userRepresentation;
    }

        private User mapToUser(UserRepresentation userRepresentation) {
        return User.builder()
                .id(userRepresentation.getId())
                .username(userRepresentation.getUsername())
                .email(userRepresentation.getEmail())
                .firstName(userRepresentation.getFirstName())
                .lastName(userRepresentation.getLastName())
                .imageUrl(userRepresentation.getAttributes().get("imageUrl").get(0))
                .build();
    }

    public List<User> getByEmail(String email) {
        return usersResourceInstance().searchByEmail(email, true).stream().map(u -> mapToUser(u)).toList();
    }

    @Override
    public User registerNewUser(UserRequest request) {
        var userRepresentation = mapToUserRepresentation(request);

        try (Response response = usersResourceInstance().create(userRepresentation)) {
            int statusCode = response.getStatus();
            switch (statusCode) {
                case 201 -> log.info("User {} successfully created in Keycloak", request.getUsername());
                case 409 -> {
                    log.error("Duplicate user {}", request.getUsername());
                    throw new DuplicateUserException("User with username: " + request.getUsername() + " already exist");
                }
                default -> {
                    log.error("Error creating user: status code {}", statusCode);
                    throw new UserCreationException(
                            MessageFormat.format("Error creating user: status code {0}", statusCode));
                }
            }

        } catch (ProcessingException e) {
            log.error("Error creating a user in Keycloak", e);
            throw new UserCreationException("error creating user");
        }

        return getByEmail(request.getEmail()).get(0);
    }

}
