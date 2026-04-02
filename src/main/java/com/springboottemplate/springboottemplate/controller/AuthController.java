package com.springboottemplate.springboottemplate.controller;

import com.springboottemplate.springboottemplate.common.dto.BaseResponse;
import com.springboottemplate.springboottemplate.dto.login.LoginRequest;
import com.springboottemplate.springboottemplate.dto.login.LoginResponse;
import com.springboottemplate.springboottemplate.service.AuthService;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Tag(name = "User API", description = "샘플 CRUD API 입니다")
@RestController
@RequiredArgsConstructor
@RequestMapping("/auth")
public class AuthController {
    private final AuthService authService;

    @PostMapping("/login")
    public BaseResponse<LoginResponse> login(@Valid @RequestBody LoginRequest request) {
        return BaseResponse.success(authService.login(request));
    }
}
