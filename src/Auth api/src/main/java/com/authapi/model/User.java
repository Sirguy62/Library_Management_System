package com.authapi.model;

import java.time.Instant;
import java.util.UUID;

public class User {

    private String id;
    private String email;
    private String username;
    private String passwordHash;
    private String createdAt;

    public User () {}

    public User (String email, String username, String passwordHash) {
        this.id = UUID.randomUUID().toString();
        this.email = email;
        this.username = username;
        this.passwordHash = passwordHash;
        this.createdAt = Instant.now().toString();
    }


    public String getId() {
        return id;
    }
    public void setId(String id) {
        this.id = id;
    }

    public String getEmail() {
        return email;
    }
    public void setEmail(String email) {
        this.email = email;
    }

    public String getUsername() {
        return username;
    }
    public void setUsername(String username) {
        this.username = username;
    }

    public String getPasswordHash() {
        return passwordHash;
    }
    public void setPasswordHash(String passwordHash) {
        this.passwordHash = passwordHash;
    }

    public String getCreatedAt() {
        return createdAt;
    }
    public void setCreatedAt(String createdAt) {
        this.createdAt = createdAt;
    }

}
