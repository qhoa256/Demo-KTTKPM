package com.quanghoa.costumeservice.service;

import com.quanghoa.costumeservice.model.LoginRequest;
import com.quanghoa.costumeservice.model.UserResponse;

public interface UserService {
    UserResponse login(LoginRequest loginRequest);
} 