package com.sprinboottemplate.springboottempate.service;

import com.sprinboottemplate.springboottempate.dto.login.LoginRequest;
import com.sprinboottemplate.springboottempate.dto.login.LoginResponse;

public interface AuthService {
    public LoginResponse login(LoginRequest req);
}
