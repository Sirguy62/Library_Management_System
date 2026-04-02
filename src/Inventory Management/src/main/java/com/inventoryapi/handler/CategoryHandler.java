package com.inventoryapi.handler;

import com.inventoryapi.middleware.AuthMiddleware;
import com.inventoryapi.model.Category;
import com.inventoryapi.store.CategoryStore;
import com.inventoryapi.util.HttpUtil;
import com.inventoryapi.util.JsonUtil;
import com.sun.net.httpserver.HttpExchange;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

public class CategoryHandler {

    private final CategoryStore  categoryStore;
    private final AuthMiddleware authMiddleware;

    public CategoryHandler(CategoryStore categoryStore, AuthMiddleware authMiddleware) {
        this.categoryStore = categoryStore;
        this.authMiddleware = authMiddleware;
    }


    public void create(HttpExchange exchange) throws IOException {
        String userId = authMiddleware.authenticate(exchange);
        if (userId == null) return;

        Map<String, String> body;
        try {
            body = JsonUtil.fromJson(exchange, HashMap.class);
        } catch (Exception e) {
            HttpUtil.sendError(exchange, 400, "Invalid JSON body");
            return;
        }

        String name        = body.get("name");
        String description = body.get("description");

        if (name == null || name.isBlank()) {
            HttpUtil.sendError(exchange, 400, "Category name is required");
            return;
        }
        if (categoryStore.existsByName(name)) {
            HttpUtil.sendError(exchange, 409, "Category name already exists");
            return;
        }

        Category category = new Category(name, description);
        categoryStore.save(category);

        HttpUtil.sendResponse(exchange, 201, JsonUtil.toJson(category));
    }


    public void getAll(HttpExchange exchange) throws IOException {
        String userId = authMiddleware.authenticate(exchange);
        if (userId == null) return;

        HttpUtil.sendResponse(exchange, 200, JsonUtil.toJson(categoryStore.findAll()));
    }


    public void getOne(HttpExchange exchange) throws IOException {
        String userId = authMiddleware.authenticate(exchange);
        if (userId == null) return;

        String id = extractId(exchange.getRequestURI().getPath());
        if (id == null) {
            HttpUtil.sendError(exchange, 400, "Category ID is required");
            return;
        }

        Optional<Category> categoryOpt = categoryStore.findById(id);
        if (categoryOpt.isEmpty()) {
            HttpUtil.sendError(exchange, 404, "Category not found");
            return;
        }

        HttpUtil.sendResponse(exchange, 200, JsonUtil.toJson(categoryOpt.get()));
    }


    public void update(HttpExchange exchange) throws IOException {
        String userId = authMiddleware.authenticate(exchange);
        if (userId == null) return;

        String id = extractId(exchange.getRequestURI().getPath());
        if (id == null) {
            HttpUtil.sendError(exchange, 400, "Category ID is required");
            return;
        }

        Optional<Category> categoryOpt = categoryStore.findById(id);
        if (categoryOpt.isEmpty()) {
            HttpUtil.sendError(exchange, 404, "Category not found");
            return;
        }

        Map<String, String> body;
        try {
            body = JsonUtil.fromJson(exchange, HashMap.class);
        } catch (Exception e) {
            HttpUtil.sendError(exchange, 400, "Invalid JSON body");
            return;
        }

        Category category = categoryOpt.get();

        String newName        = body.get("name");
        String newDescription = body.get("description");

        if (newName != null && !newName.isBlank()) {
            category.setName(newName);
        }
        if (newDescription != null && !newDescription.isBlank()) {
            category.setDescription(newDescription);
        }

        categoryStore.update(category);
        HttpUtil.sendResponse(exchange, 200, JsonUtil.toJson(category));
    }


    public void delete(HttpExchange exchange) throws IOException {
        String userId = authMiddleware.authenticate(exchange);
        if (userId == null) return;

        String id = extractId(exchange.getRequestURI().getPath());
        if (id == null) {
            HttpUtil.sendError(exchange, 400, "Category ID is required");
            return;
        }

        if (categoryStore.findById(id).isEmpty()) {
            HttpUtil.sendError(exchange, 404, "Category not found");
            return;
        }

        categoryStore.delete(id);

        Map<String, String> response = new HashMap<>();
        response.put("message", "Category deleted successfully");
        HttpUtil.sendResponse(exchange, 200, JsonUtil.toJson(response));
    }

    private String extractId(String path) {
        String[] parts = path.split("/");
        return parts.length >= 3 ? parts[2] : null;
    }
}