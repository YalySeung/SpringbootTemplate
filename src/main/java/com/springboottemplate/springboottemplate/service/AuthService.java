package com.springboottemplate.springboottemplate.service;

import com.springboottemplate.springboottemplate.dto.login.LoginRequest;
import com.springboottemplate.springboottemplate.dto.login.LoginResponse;

public interface AuthService {
    public LoginResponse login(LoginRequest req);
}
