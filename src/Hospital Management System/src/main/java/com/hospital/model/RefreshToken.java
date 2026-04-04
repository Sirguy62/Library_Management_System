package com.hospital.model;

import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.UUID;

public class RefreshToken {

    private String  token;
    private String  userId;
    private String  expiresAt;
    private boolean revoked;
    private String  createdAt;

    public RefreshToken() {}

    public RefreshToken(String userId) {
        this.token     = UUID.randomUUID().toString();
        this.userId    = userId;
        this.expiresAt = Instant.now()
                .plus(30, ChronoUnit.DAYS)
                .toString();
        this.revoked   = false;
        this.createdAt = Instant.now().toString();
    }

    public boolean isExpired() {
        return Instant.now().isAfter(Instant.parse(expiresAt));
    }

    public boolean isValid() {
        return !revoked && !isExpired();
    }

    public void revoke() {
        this.revoked = true;
    }

    public String  getToken()                  { return token; }
    public void    setToken(String token)      { this.token = token; }
    public String  getUserId()                 { return userId; }
    public void    setUserId(String userId)    { this.userId = userId; }
    public String  getExpiresAt()              { return expiresAt; }
    public void    setExpiresAt(String e)      { this.expiresAt = e; }
    public boolean isRevoked()                 { return revoked; }
    public void    setRevoked(boolean revoked) { this.revoked = revoked; }
    public String  getCreatedAt()              { return createdAt; }
    public void    setCreatedAt(String c)      { this.createdAt = c; }
}