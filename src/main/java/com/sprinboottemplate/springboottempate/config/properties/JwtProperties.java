package com.sprinboottemplate.springboottempate.config.properties;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;

@Data
@ConfigurationProperties(prefix = "app.security.jwt")
public class JwtProperties {

    /**
     * HS256 / RS256
     */
    private String alg = "HS256";

    /**
     * HS256용 secret (최소 256-bit 권장)
     */
    private String secret;

    /**
     * RS256용 공개키 PEM
     */
    private String publicKeyPem;

    private String issuer;
    private String audience;

    /**
     * 만료/nbf 검증 허용 오차
     */
    private long clockSkewSeconds = 30;

    /** Access Token 만료 시간 (초) */
    private long accessTokenTtlSeconds = 1800;
}