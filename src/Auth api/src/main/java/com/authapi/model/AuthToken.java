package com.authapi.model;

import java.time.Instant;
import java.time.temporal.ChronoUnit;

public class AuthToken {

    private String token;
    private String userId;
    private String expiresAt;


    public AuthToken () {}

    public AuthToken(String token, String userId) {
        this.token = token;
        this.userId = userId;
        this.expiresAt = Instant.now().plus(24, ChronoUnit.HOURS).toString();
    }

    public boolean isExpired() {
        return Instant.now().isAfter(Instant.parse(expiresAt));
    }

    public String getToken() {
        return token;
    }
    public void setToken(String token) {
        this.token = token;
    }

    public String getUserId() {
        return userId;
    }
    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getExpiresAt() {
        return expiresAt;
    }

    public void setExpiresAt(String expiresAt) {
        this.expiresAt = expiresAt;
    }
}
