package com.sprinboottemplate.springboottempate.security;

import org.springframework.security.core.Authentication;

public interface TokenVerifier {
    Authentication verify(String token); // 유효하면 Authentication 반환, 아니면 예외
}
