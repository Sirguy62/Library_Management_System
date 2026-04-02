package com.inventoryapi.handler;

import com.inventoryapi.middleware.AuthMiddleware;
import com.inventoryapi.model.Supplier;
import com.inventoryapi.store.SupplierStore;
import com.inventoryapi.util.HttpUtil;
import com.inventoryapi.util.JsonUtil;
import com.sun.net.httpserver.HttpExchange;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

public class SupplierHandler {

    private final SupplierStore  supplierStore;
    private final AuthMiddleware authMiddleware;

    public SupplierHandler(SupplierStore supplierStore, AuthMiddleware authMiddleware) {
        this.supplierStore  = supplierStore;
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

        String name    = body.get("name");
        String email   = body.get("email");
        String phone   = body.get("phone");
        String address = body.get("address");

        if (name == null || name.isBlank()) {
            HttpUtil.sendError(exchange, 400, "Supplier name is required");
            return;
        }

        Supplier supplier = new Supplier(name, email, phone, address);
        supplierStore.save(supplier);

        HttpUtil.sendResponse(exchange, 201, JsonUtil.toJson(supplier));
    }


    public void getAll(HttpExchange exchange) throws IOException {
        String userId = authMiddleware.authenticate(exchange);
        if (userId == null) return;

        HttpUtil.sendResponse(exchange, 200, JsonUtil.toJson(supplierStore.findAll()));
    }


    public void getOne(HttpExchange exchange) throws IOException {
        String userId = authMiddleware.authenticate(exchange);
        if (userId == null) return;

        String id = extractId(exchange.getRequestURI().getPath());
        if (id == null) {
            HttpUtil.sendError(exchange, 400, "Supplier ID is required");
            return;
        }

        Optional<Supplier> supplierOpt = supplierStore.findById(id);
        if (supplierOpt.isEmpty()) {
            HttpUtil.sendError(exchange, 404, "Supplier not found");
            return;
        }

        HttpUtil.sendResponse(exchange, 200, JsonUtil.toJson(supplierOpt.get()));
    }


    public void update(HttpExchange exchange) throws IOException {
        String userId = authMiddleware.authenticate(exchange);
        if (userId == null) return;

        String id = extractId(exchange.getRequestURI().getPath());
        if (id == null) {
            HttpUtil.sendError(exchange, 400, "Supplier ID is required");
            return;
        }

        Optional<Supplier> supplierOpt = supplierStore.findById(id);
        if (supplierOpt.isEmpty()) {
            HttpUtil.sendError(exchange, 404, "Supplier not found");
            return;
        }

        Map<String, String> body;
        try {
            body = JsonUtil.fromJson(exchange, HashMap.class);
        } catch (Exception e) {
            HttpUtil.sendError(exchange, 400, "Invalid JSON body");
            return;
        }

        Supplier supplier = supplierOpt.get();

        if (body.containsKey("name")    && !body.get("name").isBlank())    supplier.setName(body.get("name"));
        if (body.containsKey("email")   && !body.get("email").isBlank())   supplier.setEmail(body.get("email"));
        if (body.containsKey("phone")   && !body.get("phone").isBlank())   supplier.setPhone(body.get("phone"));
        if (body.containsKey("address") && !body.get("address").isBlank()) supplier.setAddress(body.get("address"));

        supplierStore.update(supplier);
        HttpUtil.sendResponse(exchange, 200, JsonUtil.toJson(supplier));
    }


    public void delete(HttpExchange exchange) throws IOException {
        String userId = authMiddleware.authenticate(exchange);
        if (userId == null) return;

        String id = extractId(exchange.getRequestURI().getPath());
        if (id == null) {
            HttpUtil.sendError(exchange, 400, "Supplier ID is required");
            return;
        }

        if (supplierStore.findById(id).isEmpty()) {
            HttpUtil.sendError(exchange, 404, "Supplier not found");
            return;
        }

        supplierStore.delete(id);

        Map<String, String> response = new HashMap<>();
        response.put("message", "Supplier deleted successfully");
        HttpUtil.sendResponse(exchange, 200, JsonUtil.toJson(response));
    }

    private String extractId(String path) {
        String[] parts = path.split("/");
        return parts.length >= 3 ? parts[2] : null;
    }
}