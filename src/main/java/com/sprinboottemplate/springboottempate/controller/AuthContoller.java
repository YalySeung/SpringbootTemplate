package com.sprinboottemplate.springboottempate.controller;

import com.sprinboottemplate.springboottempate.common.dto.BaseResponse;
import com.sprinboottemplate.springboottempate.dto.login.LoginRequest;
import com.sprinboottemplate.springboottempate.dto.login.LoginResponse;
import com.sprinboottemplate.springboottempate.service.AuthService;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Tag(name = "User API", description = "샘플 CRUD API 입니다")
@RestController
@RequiredArgsConstructor
@RequestMapping("/auth")
public class AuthContoller {
    private final AuthService authService;

    @PostMapping("/login")
    public BaseResponse<LoginResponse> login(@RequestBody LoginRequest request) {
        return BaseResponse.success(authService.login(request));
    }
}
