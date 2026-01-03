package com.sprinboottemplate.springboottempate.security;

import com.sprinboottemplate.springboottempate.config.properties.JwtProperties;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.nio.charset.StandardCharsets;
import java.time.Instant;
import java.util.Date;
import java.util.List;

@Component
@RequiredArgsConstructor
public class JwtTokenProvider {
    private final JwtProperties props;

    public String issueAccessToken(String subject, List<String> roles) {
        Instant now = Instant.now();
        Instant exp = now.plusSeconds(props.getAccessTokenTtlSeconds());

        // HS 계열 발급(가장 단순). RS 발급은 private key 필요(별도)
        var key = Keys.hmacShaKeyFor(props.getSecret().getBytes(StandardCharsets.UTF_8));

        return Jwts.builder()
                .setIssuer(props.getIssuer())
                .setSubject(subject)
                .setIssuedAt(Date.from(now))
                .setExpiration(Date.from(exp))
                .claim("roles", roles)
                .signWith(key, SignatureAlgorithm.HS256)
                .compact();
    }
}
