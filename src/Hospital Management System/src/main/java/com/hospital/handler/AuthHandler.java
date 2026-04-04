package com.hospital.handler;

import com.hospital.enums.UserRole;
import com.hospital.middleware.AuthMiddleware;
import com.hospital.service.AuthService;
import com.hospital.service.AuthService.LoginResult;
import com.hospital.util.HttpUtil;
import com.hospital.util.JsonUtil;
import com.hospital.util.Result;
import com.sun.net.httpserver.HttpExchange;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

public class AuthHandler {

    private final AuthService    authService;
    private final AuthMiddleware authMiddleware;

    public AuthHandler(AuthService authService,
                       AuthMiddleware authMiddleware) {
        this.authService    = authService;
        this.authMiddleware = authMiddleware;
    }


    public void register(HttpExchange exchange) throws IOException {
        Map<String, Object> body = JsonUtil.fromJson(exchange, HashMap.class);

        String email    = (String) body.get("email");
        String username = (String) body.get("username");
        String password = (String) body.get("password");
        String roleStr  = (String) body.getOrDefault("role", "PATIENT");

        UserRole role;
        try {
            role = UserRole.fromString(roleStr);
        } catch (IllegalArgumentException e) {
            HttpUtil.sendError(exchange, 400, "Invalid role: " + roleStr);
            return;
        }

        var person = authService.register(
                email, username, password, role, body,
                HttpUtil.getClientIp(exchange));

        Map<String, Object> response = new HashMap<>();
        response.put("id",        person.getId());
        response.put("email",     person.getEmail());
        response.put("username",  person.getUsername());
        response.put("role",      person.getRole().name());
        response.put("status",    person.getStatus().name());
        response.put("message",
                "Registration successful. Check your email to verify.");

        HttpUtil.sendResponse(exchange, 201, JsonUtil.toJson(response));
    }


    public void verifyEmail(HttpExchange exchange) throws IOException {
        Map<String, String> params =
                HttpUtil.parseQueryParams(exchange);
        String token = params.get("token");

        if (token == null || token.isBlank()) {
            HttpUtil.sendError(exchange, 400,
                    "Verification token is required");
            return;
        }

        authService.verifyEmail(token,
                HttpUtil.getClientIp(exchange));

        Map<String, String> response = new HashMap<>();
        response.put("message",
                "Email verified successfully. You can now log in.");

        HttpUtil.sendResponse(exchange, 200, JsonUtil.toJson(response));
    }


    public void login(HttpExchange exchange) throws IOException {
        Map<String, Object> body =
                JsonUtil.fromJson(exchange, HashMap.class);

        String email    = (String) body.get("email");
        String password = (String) body.get("password");

        if (email == null || password == null) {
            HttpUtil.sendError(exchange, 400,
                    "Email and password are required");
            return;
        }

        Result<LoginResult> result = authService.login(
                email, password,
                HttpUtil.getClientIp(exchange));

        LoginResult loginResult = result.getValue();

        exchange.getResponseHeaders().add(
                "Set-Cookie",
                "refresh_token=" + loginResult.getRefreshToken()
                        + "; HttpOnly; Path=/auth/refresh"
                        + "; Max-Age=2592000"  // 30 days
        );

        Map<String, Object> response = new HashMap<>();
        response.put("accessToken",  loginResult.getAccessToken());
        response.put("tokenType",    "Bearer");
        response.put("expiresIn",    900); // 15 minutes in seconds
        response.put("userId",       loginResult.getPerson().getId());
        response.put("role",         loginResult.getPerson()
                .getRole().name());
        response.put("displayName",  loginResult.getPerson()
                .getDisplayName());

        HttpUtil.sendResponse(exchange, 200, JsonUtil.toJson(response));
    }


    public void refresh(HttpExchange exchange) throws IOException {
        // Read refresh token from cookie
        String refreshToken = extractRefreshTokenFromCookie(exchange);

        if (refreshToken == null) {
            HttpUtil.sendError(exchange, 401,
                    "Refresh token not found");
            return;
        }

        Result<String> result = authService.refresh(
                refreshToken,
                HttpUtil.getClientIp(exchange));

        Map<String, Object> response = new HashMap<>();
        response.put("accessToken", result.getValue());
        response.put("tokenType",   "Bearer");
        response.put("expiresIn",   900);

        HttpUtil.sendResponse(exchange, 200, JsonUtil.toJson(response));
    }


    public void logout(HttpExchange exchange) throws IOException {
        String userId = authMiddleware.authenticate(exchange);
        String token  = HttpUtil.extractBearerToken(exchange);
        String refreshToken = extractRefreshTokenFromCookie(exchange);

        authService.logout(token, refreshToken, userId,
                HttpUtil.getClientIp(exchange));

        exchange.getResponseHeaders().add(
                "Set-Cookie",
                "refresh_token=; HttpOnly; Path=/auth/refresh"
                        + "; Max-Age=0"
        );

        Map<String, String> response = new HashMap<>();
        response.put("message", "Logged out successfully");
        HttpUtil.sendResponse(exchange, 200, JsonUtil.toJson(response));
    }


    public void logoutAll(HttpExchange exchange) throws IOException {
        String userId = authMiddleware.authenticate(exchange);
        String token  = HttpUtil.extractBearerToken(exchange);

        authService.logoutAll(token, userId,
                HttpUtil.getClientIp(exchange));

        exchange.getResponseHeaders().add(
                "Set-Cookie",
                "refresh_token=; HttpOnly; Path=/auth/refresh"
                        + "; Max-Age=0"
        );

        Map<String, String> response = new HashMap<>();
        response.put("message",
                "Logged out from all devices successfully");
        HttpUtil.sendResponse(exchange, 200, JsonUtil.toJson(response));
    }


    private String extractRefreshTokenFromCookie(
            HttpExchange exchange) {
        String cookieHeader = HttpUtil.getHeader(exchange, "Cookie");
        if (cookieHeader == null) return null;

        for (String cookie : cookieHeader.split(";")) {
            String trimmed = cookie.trim();
            if (trimmed.startsWith("refresh_token=")) {
                return trimmed.substring("refresh_token=".length());
            }
        }
        return null;
    }
}