package com.sprinboottemplate.springboottempate.service;

import com.sprinboottemplate.springboottempate.dto.login.LoginRequest;
import com.sprinboottemplate.springboottempate.dto.login.LoginResponse;
import com.sprinboottemplate.springboottempate.security.JwtTokenProvider;
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
            throw new IllegalArgumentException("Invalid credentials");
        }

        String token = jwtTokenProvider.issueAccessToken(req.getUsername(), List.of("ADMIN"));
        return new LoginResponse("Bearer", token, 1800);
    }
}
