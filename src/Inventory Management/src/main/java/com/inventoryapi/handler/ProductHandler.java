package com.inventoryapi.handler;

import com.inventoryapi.middleware.AuthMiddleware;
import com.inventoryapi.model.Product;
import com.inventoryapi.store.CategoryStore;
import com.inventoryapi.store.ProductStore;
import com.inventoryapi.store.SupplierStore;
import com.inventoryapi.util.HttpUtil;
import com.inventoryapi.util.JsonUtil;
import com.sun.net.httpserver.HttpExchange;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

public class ProductHandler {

    private final ProductStore   productStore;
    private final CategoryStore  categoryStore;
    private final SupplierStore  supplierStore;
    private final AuthMiddleware authMiddleware;

    public ProductHandler(ProductStore productStore, CategoryStore categoryStore,
                          SupplierStore supplierStore, AuthMiddleware authMiddleware) {
        this.productStore   = productStore;
        this.categoryStore  = categoryStore;
        this.supplierStore  = supplierStore;
        this.authMiddleware = authMiddleware;
    }


    public void create(HttpExchange exchange) throws IOException {
        String userId = authMiddleware.authenticate(exchange);
        if (userId == null) return;

        Map<String, Object> body;
        try {
            body = JsonUtil.fromJson(exchange, HashMap.class);
        } catch (Exception e) {
            HttpUtil.sendError(exchange, 400, "Invalid JSON body");
            return;
        }

        String name        = (String) body.get("name");
        String description = (String) body.get("description");
        String categoryId  = (String) body.get("categoryId");
        String supplierId  = (String) body.get("supplierId");
        int    minQuantity = 10; // default
        double price       = 0;

        try {
            price = ((Number) body.get("price")).doubleValue();
        } catch (Exception e) {
            HttpUtil.sendError(exchange, 400, "Price must be a number");
            return;
        }

        if (body.containsKey("minQuantity")) {
            minQuantity = ((Number) body.get("minQuantity")).intValue();
        }

        if (name == null || name.isBlank()) {
            HttpUtil.sendError(exchange, 400, "Product name is required");
            return;
        }
        if (price < 0) {
            HttpUtil.sendError(exchange, 400, "Price cannot be negative");
            return;
        }

        if (categoryId != null && categoryStore.findById(categoryId).isEmpty()) {
            HttpUtil.sendError(exchange, 404, "Category not found");
            return;
        }

        if (supplierId != null && supplierStore.findById(supplierId).isEmpty()) {
            HttpUtil.sendError(exchange, 404, "Supplier not found");
            return;
        }

        Product product = new Product(name, description, categoryId,
                supplierId, price, minQuantity);
        productStore.save(product);

        HttpUtil.sendResponse(exchange, 201, JsonUtil.toJson(product));
    }


    public void getAll(HttpExchange exchange) throws IOException {
        String userId = authMiddleware.authenticate(exchange);
        if (userId == null) return;

        Map<String, String> params = HttpUtil.parseQueryParams(exchange);
        String categoryId = params.get("categoryId");

        List<Product> products = categoryId != null
                ? productStore.findByCategoryId(categoryId)
                : productStore.findAll();

        HttpUtil.sendResponse(exchange, 200, JsonUtil.toJson(products));
    }


    public void getOne(HttpExchange exchange) throws IOException {
        String userId = authMiddleware.authenticate(exchange);
        if (userId == null) return;

        String id = extractId(exchange.getRequestURI().getPath());
        if (id == null) {
            HttpUtil.sendError(exchange, 400, "Product ID is required");
            return;
        }

        Optional<Product> productOpt = productStore.findById(id);
        if (productOpt.isEmpty()) {
            HttpUtil.sendError(exchange, 404, "Product not found");
            return;
        }

        HttpUtil.sendResponse(exchange, 200, JsonUtil.toJson(productOpt.get()));
    }


    public void getLowStock(HttpExchange exchange) throws IOException {
        String userId = authMiddleware.authenticate(exchange);
        if (userId == null) return;

        List<Product> lowStock = productStore.findLowStock();

        Map<String, Object> response = new HashMap<>();
        response.put("totalLowStock", lowStock.size());
        response.put("products",      lowStock);

        HttpUtil.sendResponse(exchange, 200, JsonUtil.toJson(response));
    }


    public void update(HttpExchange exchange) throws IOException {
        String userId = authMiddleware.authenticate(exchange);
        if (userId == null) return;

        String id = extractId(exchange.getRequestURI().getPath());
        if (id == null) {
            HttpUtil.sendError(exchange, 400, "Product ID is required");
            return;
        }

        Optional<Product> productOpt = productStore.findById(id);
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

        Product product = productOpt.get();

        if (body.containsKey("name") && !((String) body.get("name")).isBlank()) {
            product.setName((String) body.get("name"));
        }
        if (body.containsKey("description")) {
            product.setDescription((String) body.get("description"));
        }
        if (body.containsKey("price")) {
            product.setPrice(((Number) body.get("price")).doubleValue());
        }
        if (body.containsKey("minQuantity")) {
            product.setMinQuantity(((Number) body.get("minQuantity")).intValue());
        }
        if (body.containsKey("categoryId")) {
            String newCategoryId = (String) body.get("categoryId");
            if (categoryStore.findById(newCategoryId).isEmpty()) {
                HttpUtil.sendError(exchange, 404, "Category not found");
                return;
            }
            product.setCategoryId(newCategoryId);
        }
        if (body.containsKey("supplierId")) {
            String newSupplierId = (String) body.get("supplierId");
            if (supplierStore.findById(newSupplierId).isEmpty()) {
                HttpUtil.sendError(exchange, 404, "Supplier not found");
                return;
            }
            product.setSupplierId(newSupplierId);
        }

        productStore.update(product);
        HttpUtil.sendResponse(exchange, 200, JsonUtil.toJson(product));
    }


    public void delete(HttpExchange exchange) throws IOException {
        String userId = authMiddleware.authenticate(exchange);
        if (userId == null) return;

        String id = extractId(exchange.getRequestURI().getPath());
        if (id == null) {
            HttpUtil.sendError(exchange, 400, "Product ID is required");
            return;
        }

        if (productStore.findById(id).isEmpty()) {
            HttpUtil.sendError(exchange, 404, "Product not found");
            return;
        }

        productStore.delete(id);

        Map<String, String> response = new HashMap<>();
        response.put("message", "Product deleted successfully");
        HttpUtil.sendResponse(exchange, 200, JsonUtil.toJson(response));
    }

    public String extractId(String path) {
        String[] parts = path.split("/");
        return parts.length >= 3 ? parts[2] : null;
    }
}