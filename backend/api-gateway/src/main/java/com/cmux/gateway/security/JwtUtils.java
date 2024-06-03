package com.cmux.gateway.security;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jws;
import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import jakarta.annotation.PostConstruct;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import javax.crypto.SecretKey;

@Component
public class JwtUtils {

    private SecretKey secretKey;

    @Value("${JWT_SECRET_KEY}")
    private String jwtSecret;

    @PostConstruct
    public void init() {
        this.secretKey = Keys.hmacShaKeyFor(jwtSecret.getBytes());
    }
    
    private Jws<Claims> parseClaimsJws(String token) {
        return Jwts.parser()
                .verifyWith(secretKey).build()
                .parseSignedClaims(token);
    }

    public String getUserNameFromJwtToken(String token) {
        Jws<Claims> claimsJws = parseClaimsJws(token);

        return claimsJws.getPayload().get("username", String.class);
    }

    public Long getUserIdFromJwtToken(String token) {
        Jws<Claims> claimsJws = parseClaimsJws(token);
        return claimsJws.getPayload().get("userId", Long.class);
    }

    public boolean validateJwtToken(String authToken) {
        try {
            Jwts.parser()
                .verifyWith(secretKey).build()
                .parseSignedClaims(authToken);
            
            return true;
        } catch (JwtException e) {
            System.out.println("Invalid JWT token: " + e.getMessage());
        } 
        return false;
    }
}