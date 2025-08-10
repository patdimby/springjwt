package com.dimbisoapatrick.springjwt.utils;

import java.util.Date;

import javax.crypto.SecretKey;

import org.springframework.stereotype.Component;

import io.jsonwebtoken.JwtParser;
import io.jsonwebtoken.Jwts;

@Component
public class JwtTokenUtil {
    // Generate a key for HS256
    private final static SecretKey SECRET_KEY = Jwts.SIG.HS256.key().build();
    private final static long EXPIRATION_TIME = 86400000;

    public static String generateToken(String email) {
        return Jwts.builder()
                .subject(email)
                .issuedAt(new Date())
                .expiration(new Date(System.currentTimeMillis() + EXPIRATION_TIME))
                .signWith(SECRET_KEY, Jwts.SIG.HS256)
                .compact();
    }

    public boolean validateToken(String token) {
        return !isTokenExpired(token);
    }

    public String extractEmail(String token) {
        JwtParser jwtParser = Jwts.parser()
                .verifyWith(SECRET_KEY)
                .build();

        return jwtParser.parseSignedClaims(token)
                .getPayload()
                .getSubject();
    }

    private boolean isTokenExpired(String token) {
        return extractExpiration(token).before(new Date());
    }

    private Date extractExpiration(String token) {
        JwtParser jwtParser = Jwts.parser()
                .verifyWith(SECRET_KEY)
                .build();

        return jwtParser.parseSignedClaims(token)
                .getPayload()
                .getExpiration();
    }
}
