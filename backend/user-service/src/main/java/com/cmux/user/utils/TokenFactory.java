package com.cmux.user.utils;

import com.cmux.user.security.CustomUserDetails;

import io.jsonwebtoken.security.Keys;
import javax.crypto.SecretKey;
import java.util.Date;

public abstract class TokenFactory {

    protected SecretKey secretKey;
    protected int expirationMs;

    public TokenFactory(String jwtSecret, int expirationMs) {
        this.secretKey = Keys.hmacShaKeyFor(jwtSecret.getBytes());
        this.expirationMs = expirationMs;
    }

    public abstract String createToken(CustomUserDetails userDetails);

    protected Date getExpirationDate() {
        return new Date((new Date()).getTime() + expirationMs);
    }

}

