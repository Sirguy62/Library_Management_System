package com.authapi.handler;

import com.authapi.Util.HttpUtil;
import com.authapi.Util.JsonUtil;
import com.authapi.Util.PasswordUtil;
import com.authapi.middleware.AuthMiddleware;
import com.authapi.model.AuthToken;
import com.authapi.model.User;
import com.authapi.store.UserStore;
import com.sun.net.httpserver.HttpExchange;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;

public class AuthHandler {

    private final UserStore userStore;
    private final AuthMiddleware authMiddleware;

    public AuthHandler(UserStore userStore, AuthMiddleware authMiddleware) {
        this.userStore = userStore;
        this.authMiddleware = authMiddleware;
    }

    public void register(HttpExchange exchange) throws IOException {
        // 1. Only allow POST
        if (!exchange.getRequestMethod().equalsIgnoreCase("POST")) {
            HttpUtil.sendError(exchange, 405, "Method not allowed");
            return;
        }

        // 2. Read the request body into a Map
        Map<String, String> body;
        try{
            body = JsonUtil.fromJson(exchange, HashMap.class);
        } catch (Exception e){
            HttpUtil.sendError(exchange, 400, "Invalid body");
            return;
        }

        // 3. Pull out the fields
        String email = body.get("email").trim().toLowerCase();
        String username = body.get("username");
        String password = body.get("password");

        // 4. Validate — make sure nothing is missing
        if (email == null || email.isBlank()) {
            HttpUtil.sendError(exchange, 400, "Invalid email");
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

        // 5. Check email is not already taken
        if (userStore.existsByEmail(email)) {
            HttpUtil.sendError(exchange, 400, "Email already exists");
            return;
        }

        // 6. Hash the password — never store plain text
        String passwordHash = PasswordUtil.hash(password);

        // 7. Create and save the user
        User user = new User(email, username, passwordHash);
        userStore.save(user);

        Map<String, String> response = new HashMap<>();
        response.put("id", user.getId());
        response.put("email", user.getEmail());
        response.put("username", user.getUsername());
        response.put("createdAt", user.getCreatedAt());

        HttpUtil.sendResponse(exchange, 201, JsonUtil.toJson(response));
    }

    public void login(HttpExchange exchange) throws IOException {
        // Only allow POST

        if (!exchange.getRequestMethod().equalsIgnoreCase("POST")) {
            HttpUtil.sendError(exchange, 400, "Method not allowed");
            return;
        }

        // Read the request body
        Map<String, String> body;
        try{
            body = JsonUtil.fromJson(exchange, HashMap.class);
        } catch (Exception e){
            HttpUtil.sendError(exchange, 400, "Invalid body");
            return;
        }

        // Pull out fields
        String email = body.get("email").trim().toLowerCase();
        String password = body.get("password");

        // Validate
        if (email == null || email.isBlank()) {
            HttpUtil.sendError(exchange, 400, "Invalid email");
            return;
        }

        if (password == null || password.isBlank()) {
            HttpUtil.sendError(exchange, 400, "Password is required");
            return;
        }

        // Find the user by email
        Optional<User> userPas = userStore.findByEmail(email);
        if (userPas.isEmpty()) {
            System.out.println(" user not found for email → " + email);
            HttpUtil.sendResponse(exchange, 401, "Invalid email or password");
            return;
        }

        // check the password against the stored hash
        User user = userPas.get();
        System.out.println(" found user → " + user.getEmail());
        System.out.println(" hash → " + user.getPasswordHash());

        if (!PasswordUtil.verify(password, user.getPasswordHash())) {
            System.out.println(" password mismatch");
            HttpUtil.sendError(exchange, 400, "Invalid email or password");
            return;
        }

        //  create token and save it
        String rawToken = UUID.randomUUID().toString();
        AuthToken token = new AuthToken(rawToken, user.getId());
        userStore.saveToken(token);

        // send the token and save it
        Map<String, String> response = new HashMap<>();
        response.put("token", token.getToken());
        response.put("expiresAt", token.getExpiresAt());
        response.put("userId", user.getId());

        HttpUtil.sendResponse(exchange, 200, JsonUtil.toJson(response));
    }
}


























