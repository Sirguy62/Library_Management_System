package com.inventoryapi.model;

import java.time.Instant;
import java.util.UUID;

public class Supplier {
    private String id;
    private String name;
    private String email;
    private String phone;
    private String address;
    private String createdAt;

    public Supplier() {}

    public Supplier(String name, String email, String phone, String address) {
        this.id        = UUID.randomUUID().toString();
        this.name      = name;
        this.email     = email;
        this.phone     = phone;
        this.address   = address;
        this.createdAt = Instant.now().toString();
    }

    public String getId()                        { return id; }
    public void   setId(String id)               { this.id = id; }
    public String getName()                      { return name; }
    public void   setName(String name)           { this.name = name; }
    public String getEmail()                     { return email; }
    public void   setEmail(String email)         { this.email = email; }
    public String getPhone()                     { return phone; }
    public void   setPhone(String phone)         { this.phone = phone; }
    public String getAddress()                   { return address; }
    public void   setAddress(String address)     { this.address = address; }
    public String getCreatedAt()                 { return createdAt; }
    public void   setCreatedAt(String createdAt) { this.createdAt = createdAt; }
}