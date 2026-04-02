package com.inventoryapi.middleware;

import com.inventoryapi.model.AuthToken;
import com.inventoryapi.store.UserStore;
import com.inventoryapi.util.HttpUtil;
import com.sun.net.httpserver.HttpExchange;

import java.io.IOException;
import java.util.Optional;

public class AuthMiddleware {

    private final UserStore userStore;

    public AuthMiddleware(UserStore userStore) {
        this.userStore = userStore;
    }

    public String authenticate(HttpExchange exchange) throws IOException {
        String authHeader = HttpUtil.getHeader(exchange, "Authorization");

        if (authHeader == null || !authHeader.startsWith("Bearer ")) {
            HttpUtil.sendError(exchange, 401, "Missing or invalid Authorization header");
            return null;
        }

        String rawToken = authHeader.substring(7);

        Optional<AuthToken> tokenOpt = userStore.findToken(rawToken);
        if (tokenOpt.isEmpty()) {
            HttpUtil.sendError(exchange, 401, "Invalid token");
            return null;
        }

        AuthToken token = tokenOpt.get();
        if (token.isExpired()) {
            userStore.deleteToken(rawToken);
            HttpUtil.sendError(exchange, 401, "Token has expired");
            return null;
        }

        return token.getUserId();
    }
}