package com.collabflow.user.security;

import java.util.Date;
import java.util.Map;
import java.nio.charset.StandardCharsets;

import javax.crypto.SecretKey;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class JwtUtil {

    @Value("${jwt.secret}")
    private String secret;

    private SecretKey getKey() {
        return Keys.hmacShaKeyFor(secret.getBytes(StandardCharsets.UTF_8));
    }

    public String generateToken(String userId, String email) {
        long now = System.currentTimeMillis();
        long expiryMs = 1000L * 60 * 60 * 24; // 24 hours

        return Jwts.builder()
                .subject(userId)
                .claims(Map.of("email", email))
                .issuedAt(new Date(now))
                .expiration(new Date(now + expiryMs))
                .signWith(getKey())
                .compact();
    }
}
