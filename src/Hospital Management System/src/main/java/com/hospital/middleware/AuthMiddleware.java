package com.hospital.middleware;

import com.hospital.auth.JwtUtil;
import com.hospital.enums.UserRole;
import com.hospital.exception.AuthException;
import com.hospital.store.TokenStore;
import com.hospital.util.HttpUtil;
import com.sun.net.httpserver.HttpExchange;

import java.io.IOException;

public class AuthMiddleware {

    private final TokenStore tokenStore;

    public AuthMiddleware(TokenStore tokenStore) {
        this.tokenStore = tokenStore;
    }

    public String authenticate(HttpExchange exchange) throws IOException {
        String token = HttpUtil.extractBearerToken(exchange);
        if (token == null) {
            throw AuthException.unauthorized();
        }

        var decoded = JwtUtil.verify(token);

        String jwtId = decoded.getId();
        if (tokenStore.isBlacklisted(jwtId)) {
            throw new AuthException("TOKEN_BLACKLISTED",
                    "Token has been invalidated");
        }

        return decoded.getClaim("userId").asString();
    }

    public String authenticateWithRole(HttpExchange exchange,
                                       UserRole... allowedRoles)
            throws IOException {
        String userId = authenticate(exchange);

        String token = HttpUtil.extractBearerToken(exchange);
        UserRole role = JwtUtil.extractRole(token);

        for (UserRole allowed : allowedRoles) {
            if (allowed == role) return userId;
        }

        throw com.hospital.exception.ForbiddenException
                .insufficientRole();
    }

    public UserRole extractRole(HttpExchange exchange) {
        String token = HttpUtil.extractBearerToken(exchange);
        if (token == null) return null;
        try {
            return JwtUtil.extractRole(token);
        } catch (Exception e) {
            return null;
        }
    }
}