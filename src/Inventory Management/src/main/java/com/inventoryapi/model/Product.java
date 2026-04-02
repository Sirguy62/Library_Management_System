package com.inventoryapi.model;

import java.time.Instant;
import java.util.UUID;

public class Product {
    private String id;
    private String name;
    private String description;
    private String categoryId;
    private String supplierId;
    private double price;
    private int    quantity;     // current stock level
    private int    minQuantity;  // threshold — below this triggers alert
    private String createdAt;

    public Product() {}

    public Product(String name, String description, String categoryId,
                   String supplierId, double price, int minQuantity) {
        this.id          = UUID.randomUUID().toString();
        this.name        = name;
        this.description = description;
        this.categoryId  = categoryId;
        this.supplierId  = supplierId;
        this.price       = price;
        this.quantity    = 0;
        this.minQuantity = minQuantity;
        this.createdAt   = Instant.now().toString();
    }

    public boolean isLowStock()   { return quantity > 0 && quantity <= minQuantity; }
    public boolean isOutOfStock() { return quantity == 0; }

    public String getId()                          { return id; }
    public void   setId(String id)                 { this.id = id; }
    public String getName()                        { return name; }
    public void   setName(String name)             { this.name = name; }
    public String getDescription()                 { return description; }
    public void   setDescription(String desc)      { this.description = desc; }
    public String getCategoryId()                  { return categoryId; }
    public void   setCategoryId(String categoryId) { this.categoryId = categoryId; }
    public String getSupplierId()                  { return supplierId; }
    public void   setSupplierId(String supplierId) { this.supplierId = supplierId; }
    public double getPrice()                       { return price; }
    public void   setPrice(double price)           { this.price = price; }
    public int    getQuantity()                    { return quantity; }
    public void   setQuantity(int quantity)        { this.quantity = quantity; }
    public int    getMinQuantity()                 { return minQuantity; }
    public void   setMinQuantity(int minQuantity)  { this.minQuantity = minQuantity; }
    public String getCreatedAt()                   { return createdAt; }
    public void   setCreatedAt(String createdAt)   { this.createdAt = createdAt; }
}