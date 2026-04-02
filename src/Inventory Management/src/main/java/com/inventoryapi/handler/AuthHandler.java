package com.inventoryapi.handler;

import com.inventoryapi.middleware.AuthMiddleware;
import com.inventoryapi.model.AuthToken;
import com.inventoryapi.model.User;
import com.inventoryapi.store.UserStore;
import com.inventoryapi.util.HttpUtil;
import com.inventoryapi.util.JsonUtil;
import com.inventoryapi.util.PasswordUtil;
import com.sun.net.httpserver.HttpExchange;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;

public class AuthHandler {

    private final UserStore      userStore;
    private final AuthMiddleware authMiddleware;

    public AuthHandler(UserStore userStore, AuthMiddleware authMiddleware) {
        this.userStore      = userStore;
        this.authMiddleware = authMiddleware;
    }

    public void register(HttpExchange exchange) throws IOException {
        if (!exchange.getRequestMethod().equalsIgnoreCase("POST")) {
            HttpUtil.sendError(exchange, 405, "Method not allowed");
            return;
        }

        Map<String, String> body;
        try {
            body = JsonUtil.fromJson(exchange, HashMap.class);
        } catch (Exception e) {
            HttpUtil.sendError(exchange, 400, "Invalid JSON body");
            return;
        }

        String email    = body.get("email");
        String username = body.get("username");
        String password = body.get("password");

        if (email == null || email.isBlank()) {
            HttpUtil.sendError(exchange, 400, "Email is required");
            return;
        }
        if (username == null || username.isBlank()) {
            HttpUtil.sendError(exchange, 400, "Username is required");
            return;
        }
        if (password == null || password.length() < 6) {
            HttpUtil.sendError(exchange, 400, "Password must be at least 6 characters");
            return;
        }
        if (userStore.existsByEmail(email)) {
            HttpUtil.sendError(exchange, 409, "Email already registered");
            return;
        }

        String passwordHash = PasswordUtil.hash(password);
        User user = new User(email.trim().toLowerCase(), username, passwordHash);
        userStore.save(user);

        Map<String, String> response = new HashMap<>();
        response.put("id",        user.getId());
        response.put("email",     user.getEmail());
        response.put("username",  user.getUsername());
        response.put("createdAt", user.getCreatedAt());

        HttpUtil.sendResponse(exchange, 201, JsonUtil.toJson(response));
    }

    public void login(HttpExchange exchange) throws IOException {
        if (!exchange.getRequestMethod().equalsIgnoreCase("POST")) {
            HttpUtil.sendError(exchange, 405, "Method not allowed");
            return;
        }

        Map<String, String> body;
        try {
            body = JsonUtil.fromJson(exchange, HashMap.class);
        } catch (Exception e) {
            HttpUtil.sendError(exchange, 400, "Invalid JSON body");
            return;
        }

        String email    = body.get("email");
        String password = body.get("password");

        if (email == null || email.isBlank()) {
            HttpUtil.sendError(exchange, 400, "Email is required");
            return;
        }
        if (password == null || password.isBlank()) {
            HttpUtil.sendError(exchange, 400, "Password is required");
            return;
        }

        Optional<User> userOpt = userStore.findByEmail(email.trim().toLowerCase());
        if (userOpt.isEmpty()) {
            HttpUtil.sendError(exchange, 401, "Invalid email or password");
            return;
        }

        User user = userOpt.get();
        if (!PasswordUtil.verify(password, user.getPasswordHash())) {
            HttpUtil.sendError(exchange, 401, "Invalid email or password");
            return;
        }

        String    rawToken = UUID.randomUUID().toString();
        AuthToken token    = new AuthToken(rawToken, user.getId());
        userStore.saveToken(token);

        Map<String, String> response = new HashMap<>();
        response.put("token",     token.getToken());
        response.put("expiresAt", token.getExpiresAt());
        response.put("userId",    user.getId());

        HttpUtil.sendResponse(exchange, 200, JsonUtil.toJson(response));
    }
}