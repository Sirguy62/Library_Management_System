package com.inventoryapi.handler;

import com.inventoryapi.middleware.AuthMiddleware;
import com.inventoryapi.model.Product;
import com.inventoryapi.model.StockMovement;
import com.inventoryapi.store.ProductStore;
import com.inventoryapi.store.StockStore;
import com.inventoryapi.util.HttpUtil;
import com.inventoryapi.util.JsonUtil;
import com.sun.net.httpserver.HttpExchange;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

public class StockHandler {

    private final ProductStore   productStore;
    private final StockStore     stockStore;
    private final AuthMiddleware authMiddleware;

    public StockHandler(ProductStore productStore, StockStore stockStore,
                        AuthMiddleware authMiddleware) {
        this.productStore   = productStore;
        this.stockStore     = stockStore;
        this.authMiddleware = authMiddleware;
    }

    public void restock(HttpExchange exchange) throws IOException {
        String userId = authMiddleware.authenticate(exchange);
        if (userId == null) return;

        String productId = extractProductId(exchange.getRequestURI().getPath());
        if (productId == null) {
            HttpUtil.sendError(exchange, 400, "Product ID is required");
            return;
        }

        Optional<Product> productOpt = productStore.findById(productId);
        if (productOpt.isEmpty()) {
            HttpUtil.sendError(exchange, 404, "Product not found");
            return;
        }

        Map<String, Object> body;
        try {
            body = JsonUtil.fromJson(exchange, HashMap.class);
        } catch (Exception e) {
            HttpUtil.sendError(exchange, 400, "Invalid JSON body");
            return;
        }

        int quantity;
        try {
            quantity = ((Number) body.get("quantity")).intValue();
        } catch (Exception e) {
            HttpUtil.sendError(exchange, 400, "Quantity must be a number");
            return;
        }

        String note = (String) body.getOrDefault("note", "restock");

        if (quantity <= 0) {
            HttpUtil.sendError(exchange, 400, "Quantity must be greater than zero");
            return;
        }

        // Save movement record
        StockMovement movement = new StockMovement(productId, "IN", quantity, note);
        stockStore.save(movement);

        // Update product quantity
        Product product = productOpt.get();
        product.setQuantity(product.getQuantity() + quantity);
        productStore.update(product);

        // Build response
        Map<String, Object> response = new HashMap<>();
        response.put("movement",    movement);
        response.put("newQuantity", product.getQuantity());
        response.put("productName", product.getName());

        HttpUtil.sendResponse(exchange, 201, JsonUtil.toJson(response));
    }


    public void sell(HttpExchange exchange) throws IOException {
        String userId = authMiddleware.authenticate(exchange);
        if (userId == null) return;

        String productId = extractProductId(exchange.getRequestURI().getPath());
        if (productId == null) {
            HttpUtil.sendError(exchange, 400, "Product ID is required");
            return;
        }

        Optional<Product> productOpt = productStore.findById(productId);
        if (productOpt.isEmpty()) {
            HttpUtil.sendError(exchange, 404, "Product not found");
            return;
        }

        Map<String, Object> body;
        try {
            body = JsonUtil.fromJson(exchange, HashMap.class);
        } catch (Exception e) {
            HttpUtil.sendError(exchange, 400, "Invalid JSON body");
            return;
        }

        int quantity;
        try {
            quantity = ((Number) body.get("quantity")).intValue();
        } catch (Exception e) {
            HttpUtil.sendError(exchange, 400, "Quantity must be a number");
            return;
        }

        String note = (String) body.getOrDefault("note", "sale");

        if (quantity <= 0) {
            HttpUtil.sendError(exchange, 400, "Quantity must be greater than zero");
            return;
        }

        Product product = productOpt.get();

        // Cannot sell more than available
        if (quantity > product.getQuantity()) {
            HttpUtil.sendError(exchange, 400,
                    "Insufficient stock. Available: " + product.getQuantity());
            return;
        }

        StockMovement movement = new StockMovement(productId, "OUT", quantity, note);
        stockStore.save(movement);

        product.setQuantity(product.getQuantity() - quantity);
        productStore.update(product);

        Map<String, Object> response = new HashMap<>();
        response.put("movement",    movement);
        response.put("newQuantity", product.getQuantity());
        response.put("productName", product.getName());
        response.put("lowStock",    product.isLowStock());
        response.put("outOfStock",  product.isOutOfStock());

        HttpUtil.sendResponse(exchange, 201, JsonUtil.toJson(response));
    }


    public void getMovements(HttpExchange exchange) throws IOException {
        String userId = authMiddleware.authenticate(exchange);
        if (userId == null) return;

        String productId = extractProductId(exchange.getRequestURI().getPath());
        if (productId == null) {
            HttpUtil.sendError(exchange, 400, "Product ID is required");
            return;
        }

        Optional<Product> productOpt = productStore.findById(productId);
        if (productOpt.isEmpty()) {
            HttpUtil.sendError(exchange, 404, "Product not found");
            return;
        }

        Product product = productOpt.get();
        List<StockMovement> movements = stockStore.findByProductId(productId);

        Map<String, Object> response = new HashMap<>();
        response.put("productId",   productId);
        response.put("productName", product.getName());
        response.put("quantity",    product.getQuantity());
        response.put("totalIn",     stockStore.totalIn(productId));
        response.put("totalOut",    stockStore.totalOut(productId));
        response.put("movements",   movements);

        HttpUtil.sendResponse(exchange, 200, JsonUtil.toJson(response));
    }

    private String extractProductId(String path) {
        String[] parts = path.split("/");
        return parts.length >= 3 ? parts[2] : null;
    }
}