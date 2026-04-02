package com.authapi.handler;

import com.authapi.Util.HttpUtil;
import com.authapi.Util.JsonUtil;
import com.authapi.Util.PasswordUtil;
import com.authapi.middleware.AuthMiddleware;
import com.authapi.model.User;
import com.authapi.store.UserStore;
import com.sun.net.httpserver.HttpExchange;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

public class UserHandler {

    private final UserStore userStore;
    private final AuthMiddleware authMiddleware;

    public UserHandler(UserStore userStore, AuthMiddleware authMiddleware) {
        this.userStore = userStore;
        this.authMiddleware = authMiddleware;
    }

    public void getUser(HttpExchange exchange) throws IOException {
        // check token, stop if token is invalid

        String userId = authMiddleware.authenticate(exchange);
        if (userId == null) {
            return;
        }

        // find the user
        Optional<User> userVal = userStore.findById(userId);
        if(userVal.isEmpty()) {
            HttpUtil.sendResponse(exchange, 404, "User not found");
            return;
        }

        // send back user
        User user = userVal.get();
        Map<String, String> response = safeUser(user);

        HttpUtil.sendResponse(exchange, 200, JsonUtil.toJson(response));
    }

    public void updateUser(HttpExchange exchange) throws IOException {
        // check token

        String userId = authMiddleware.authenticate(exchange);
        if (userId == null) {
            return;
        }

        // find user
        Optional<User> userVal = userStore.findById(userId);
        if(userVal.isEmpty()) {
            HttpUtil.sendResponse(exchange, 404, "User not found");
            return;
        }

        // Read the request body
        Map<String, String> body;
        try {
            body = JsonUtil.fromJson(exchange, HashMap.class);
        } catch (Exception e) {
            HttpUtil.sendResponse(exchange, 400, "Invalid body");
            return;
        }

        // Apply update
        User user = userVal.get();

        String newUsername = body.get("username");
        String newPassword = body.get("password");

        if (newUsername != null && !newUsername.isBlank()) {
            user.setUsername(newUsername);
        }

        if (newPassword != null && newPassword.length() >= 6) {
            user.setPasswordHash(PasswordUtil.hash(newUsername));
        }

        // save the update user
        userStore.update(user);

        // send back updated profile
        HttpUtil.sendResponse(exchange, 200, JsonUtil.toJson(safeUser(user)));
    }


    public void deleteUser(HttpExchange exchange) throws IOException {
        // check token
        String userId = authMiddleware.authenticate(exchange);
        if (userId == null) {
            return;
        }

        // find user
        Optional<User> userVal = userStore.findById(userId);
        if(userVal.isEmpty()) {
            HttpUtil.sendResponse(exchange, 404, "User not found");
            return;
        }

        // Delete the user and their token
        User user = userVal.get();
        String authHeader = HttpUtil.getHeader(exchange, "Authorization");
        String rawToken = authHeader.substring(7);

        userStore.deleteByEmail(user.getEmail());
        userStore.deleteToken(rawToken);

        // send confirmation
        Map<String, String> respone = new HashMap<>();
        respone.put("message", "Account deleted successfully");

        HttpUtil.sendResponse(exchange, 200, JsonUtil.toJson(respone));
    }

    private Map<String, String> safeUser(User user) {
        Map<String, String> map = new HashMap<>();
        map.put("id", user.getId());
        map.put("username", user.getUsername());
        map.put("email", user.getEmail());
        map.put("createdAt", user.getCreatedAt());
        return map;
    }

}


























