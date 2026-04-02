package com.inventoryapi.model;

import java.time.Instant;
import java.util.UUID;

public class StockMovement {
    private String id;
    private String productId;
    private String type;      // "IN" = restock, "OUT" = sale/removal
    private int    quantity;
    private String note;
    private String movedAt;

    public StockMovement() {}

    public StockMovement(String productId, String type, int quantity, String note) {
        this.id        = UUID.randomUUID().toString();
        this.productId = productId;
        this.type      = type;
        this.quantity  = quantity;
        this.note      = note;
        this.movedAt   = Instant.now().toString();
    }

    public String getId()                        { return id; }
    public void   setId(String id)               { this.id = id; }
    public String getProductId()                 { return productId; }
    public void   setProductId(String productId) { this.productId = productId; }
    public String getType()                      { return type; }
    public void   setType(String type)           { this.type = type; }
    public int    getQuantity()                  { return quantity; }
    public void   setQuantity(int quantity)      { this.quantity = quantity; }
    public String getNote()                      { return note; }
    public void   setNote(String note)           { this.note = note; }
    public String getMovedAt()                   { return movedAt; }
    public void   setMovedAt(String movedAt)     { this.movedAt = movedAt; }
}
