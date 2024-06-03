package com.cmux.user.utils;

import org.springframework.stereotype.Component;

import io.jsonwebtoken.ClaimsBuilder;
import io.jsonwebtoken.Jwts;
import com.cmux.user.security.CustomUserDetails;
import org.springframework.beans.factory.annotation.Value;
import java.util.Date;

@Component
public class RefreshTokenFactory extends TokenFactory {

    public RefreshTokenFactory(@Value("${JWT_SECRET_KEY}") String jwtSecret, @Value("${JWT_REFRESH_EXPIRATION_MS}") int jwtRefreshExpirationMs ) {
        super(jwtSecret, jwtRefreshExpirationMs);
    }

    @Override
    public String createToken(CustomUserDetails userDetails) {
        ClaimsBuilder claims = Jwts.claims();
        claims.add("username", userDetails.getUsername());
        claims.add("userId", userDetails.getUserId());
        claims.add("tokenType", "refresh");
        return Jwts.builder().claims(claims.build())
                .subject(userDetails.getUsername())
                .issuedAt(new Date())
                .expiration(getExpirationDate())
                .signWith(secretKey)
                .compact();
    }
}

