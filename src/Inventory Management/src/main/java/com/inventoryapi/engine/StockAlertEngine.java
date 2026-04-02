package com.inventoryapi.engine;

import com.inventoryapi.model.Product;
import com.inventoryapi.store.ProductStore;
import com.inventoryapi.store.StockStore;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class StockAlertEngine {

    private final ProductStore productStore;
    private final StockStore   stockStore;

    public StockAlertEngine(ProductStore productStore, StockStore stockStore) {
        this.productStore = productStore;
        this.stockStore   = stockStore;
    }

    public List<Map<String, Object>> getLowStockAlerts() {
        return productStore.findLowStock()
                .stream()
                .map(product -> buildAlert(product, "LOW_STOCK",
                        "Stock is below minimum threshold — reorder soon"))
                .collect(Collectors.toList());
    }

    public List<Map<String, Object>> getOutOfStockAlerts() {
        return productStore.findOutOfStock()
                .stream()
                .map(product -> buildAlert(product, "OUT_OF_STOCK",
                        "Product is out of stock — reorder immediately"))
                .collect(Collectors.toList());
    }

    public Map<String, Object> getFullSummary() {
        List<Product> all        = productStore.findAll();
        List<Product> lowStock   = productStore.findLowStock();
        List<Product> outOfStock = productStore.findOutOfStock();

        double totalValue = all.stream()
                .mapToDouble(p -> p.getPrice() * p.getQuantity())
                .sum();

        Map<String, Object> summary = new HashMap<>();
        summary.put("totalProducts",    all.size());
        summary.put("totalLowStock",    lowStock.size());
        summary.put("totalOutOfStock",  outOfStock.size());
        summary.put("totalValue",       totalValue);
        summary.put("lowStockItems",    lowStock.stream()
                .map(p -> buildAlert(p, "LOW_STOCK", "Reorder soon"))
                .collect(Collectors.toList()));
        summary.put("outOfStockItems",  outOfStock.stream()
                .map(p -> buildAlert(p, "OUT_OF_STOCK", "Reorder immediately"))
                .collect(Collectors.toList()));

        return summary;
    }

    private Map<String, Object> buildAlert(Product product, String alertType, String action) {
        Map<String, Object> alert = new HashMap<>();
        alert.put("productId",   product.getId());
        alert.put("productName", product.getName());
        alert.put("alertType",   alertType);
        alert.put("quantity",    product.getQuantity());
        alert.put("minQuantity", product.getMinQuantity());
        alert.put("price",       product.getPrice());
        alert.put("totalIn",     stockStore.totalIn(product.getId()));
        alert.put("totalOut",    stockStore.totalOut(product.getId()));
        alert.put("action",      action);
        return alert;
    }
}