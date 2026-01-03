package com.sprinboottemplate.springboottempate.security;

import com.sprinboottemplate.springboottempate.config.properties.JwtProperties;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.Jws;
import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.JwtParser;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.MalformedJwtException;
import io.jsonwebtoken.SignatureException;
import io.jsonwebtoken.UnsupportedJwtException;
import io.jsonwebtoken.security.Keys;
import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.Resource;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.security.Key;
import java.security.KeyFactory;
import java.security.PublicKey;
import java.security.spec.X509EncodedKeySpec;
import java.util.Arrays;
import java.util.Base64;
import java.util.Collection;
import java.util.List;
import java.util.Locale;
import java.util.Objects;

@Service
@RequiredArgsConstructor
public class JwtTokenVerifier implements TokenVerifier {

    private final JwtProperties props;

    private JwtParser jwtParser;

    @Value("${app.security.jwt.public-key-location:}")
    private Resource publicKeyResource;

    @PostConstruct
    void init() {
        Key key = resolveKey(props);
        this.jwtParser = Jwts.parserBuilder()
                .setSigningKey(key)
                .setAllowedClockSkewSeconds(props.getClockSkewSeconds())
                .build();
    }

    @Override
    public Authentication verify(String token) {
        try {
            Jws<Claims> jws = jwtParser.parseClaimsJws(token);
            Claims claims = jws.getBody();

            // issuer 검증(선택)
            if (props.getIssuer() != null && !props.getIssuer().isBlank()) {
                String iss = claims.getIssuer();
                if (!props.getIssuer().equals(iss)) {
                    throw new JwtException("Invalid issuer");
                }
            }

            // subject(사용자 식별자)
            String subject = claims.getSubject();
            if (subject == null || subject.isBlank()) {
                throw new JwtException("Missing subject");
            }

            List<String> roles = extractRoles(claims);

            List<SimpleGrantedAuthority> authorities = roles.stream()
                    .filter(Objects::nonNull)
                    .map(String::trim)
                    .filter(s -> !s.isEmpty())
                    .map(r -> r.startsWith("ROLE_") ? r : "ROLE_" + r)
                    .distinct()
                    .map(SimpleGrantedAuthority::new)
                    .toList();

            return new UsernamePasswordAuthenticationToken(subject, null, authorities);

        } catch (ExpiredJwtException e) {
            throw new JwtException("Token expired", e);
        } catch (UnsupportedJwtException e) {
            throw new JwtException("Unsupported token", e);
        } catch (MalformedJwtException e) {
            throw new JwtException("Malformed token", e);
        } catch (SignatureException e) {
            throw new JwtException("Invalid signature", e);
        } catch (IllegalArgumentException e) {
            throw new JwtException("Invalid token", e);
        }
    }

    private List<String> extractRoles(Claims claims) {
        Object roles = claims.get("roles");
        if (roles == null) roles = claims.get("authorities");
        if (roles == null) roles = claims.get("role");

        if (roles == null) return List.of();

        if (roles instanceof String s) {
            return Arrays.stream(s.split(","))
                    .map(String::trim)
                    .filter(v -> !v.isEmpty())
                    .toList();
        }

        if (roles instanceof Collection<?> c) {
            return c.stream()
                    .map(String::valueOf)
                    .toList();
        }

        return List.of(String.valueOf(roles));
    }

    private Key resolveKey(JwtProperties props) {
        String alg = (props.getAlg() == null ? "HS256" : props.getAlg().trim().toUpperCase(Locale.ROOT));

        return switch (alg) {
            case "HS256", "HS384", "HS512" -> resolveHmacKey(props);
            case "RS256", "RS384", "RS512" -> resolveRsaPublicKey();
            default -> throw new IllegalStateException("Unsupported alg: " + alg);
        };
    }

    private Key resolveHmacKey(JwtProperties props) {
        if (props.getSecret() == null || props.getSecret().isBlank()) {
            throw new IllegalStateException("app.security.jwt.secret is required for HMAC algorithms");
        }
        return Keys.hmacShaKeyFor(props.getSecret().getBytes(StandardCharsets.UTF_8));
    }

    private PublicKey resolveRsaPublicKey() {
        if (publicKeyResource == null || !publicKeyResource.exists()) {
            throw new IllegalStateException("app.security.jwt.public-key-location is required for RSA algorithms");
        }

        try (InputStream is = publicKeyResource.getInputStream()) {
            String pem = new String(is.readAllBytes(), StandardCharsets.UTF_8);
            return parseRsaPublicKey(pem);
        } catch (IOException e) {
            throw new IllegalStateException("Failed to read RSA public key resource", e);
        }
    }

    private PublicKey parseRsaPublicKey(String pem) {
        try {
            String normalized = pem
                    .replace("-----BEGIN PUBLIC KEY-----", "")
                    .replace("-----END PUBLIC KEY-----", "")
                    .replace("\\n", "\n")
                    .replace("\r", "")
                    .replace("\n", "")
                    .replace(" ", "")
                    .trim();

            // Base64 손상 빠른 감지(원인 파악 도움)
            if (normalized.length() % 4 != 0) {
                throw new IllegalStateException("Public key Base64 is broken. length=" + normalized.length());
            }

            byte[] keyBytes = Base64.getDecoder().decode(normalized);
            X509EncodedKeySpec spec = new X509EncodedKeySpec(keyBytes);
            return KeyFactory.getInstance("RSA").generatePublic(spec);

        } catch (Exception e) {
            throw new IllegalStateException("Failed to parse RSA public key", e);
        }
    }
}
