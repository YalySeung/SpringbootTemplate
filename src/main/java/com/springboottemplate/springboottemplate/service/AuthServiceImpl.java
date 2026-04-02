package com.springboottemplate.springboottemplate.service;

import com.springboottemplate.springboottemplate.common.ApiException;
import com.springboottemplate.springboottemplate.common.ApiResultCode;
import com.springboottemplate.springboottemplate.dto.login.LoginRequest;
import com.springboottemplate.springboottemplate.dto.login.LoginResponse;
import com.springboottemplate.springboottemplate.security.JwtTokenProvider;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@Transactional
@RequiredArgsConstructor
public class AuthServiceImpl implements AuthService {
    private final JwtTokenProvider jwtTokenProvider;

    @Override
    public LoginResponse login(LoginRequest req) {
        // TODO: DB 인증으로 교체
        if (!"admin".equals(req.getUsername()) || !"1234".equals(req.getPassword())) {
            throw new ApiException(ApiResultCode.UNAUTHORIZED, "아이디 또는 비밀번호가 올바르지 않습니다.");
        }

        String token = jwtTokenProvider.issueAccessToken(req.getUsername(), List.of("ADMIN"));
        return new LoginResponse("Bearer", token, 1800);
    }
}
