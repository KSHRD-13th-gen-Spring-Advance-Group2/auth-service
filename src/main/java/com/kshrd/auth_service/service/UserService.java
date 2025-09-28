package com.kshrd.auth_service.service;

import com.kshrd.auth_service.model.dto.request.ChangePasswordRequest;
import com.kshrd.auth_service.model.dto.request.UpdateUserRequest;
import com.kshrd.auth_service.model.dto.request.UserRequest;
import com.kshrd.auth_service.model.entity.User;

public interface UserService {

    User registerNewUser(UserRequest request);

    User getUser();

    User updateUser(UpdateUserRequest request);

    User changeUserPassword(ChangePasswordRequest request);
    
}
