package com.hospital.model;

import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.UUID;

public class VerificationToken {

    private String  token;
    private String  userId;
    private String  expiresAt;
    private boolean used;
    private String  createdAt;

    public VerificationToken() {}

    public VerificationToken(String userId) {
        this.token     = UUID.randomUUID().toString();
        this.userId    = userId;
        this.expiresAt = Instant.now()
                .plus(30, ChronoUnit.MINUTES)
                .toString();
        this.used      = false;
        this.createdAt = Instant.now().toString();
    }

    public boolean isExpired() {
        return Instant.now().isAfter(Instant.parse(expiresAt));
    }

    public boolean isValid() {
        return !used && !isExpired();
    }

    public void markUsed() {
        this.used = true;
    }

    public String  getToken()                  { return token; }
    public void    setToken(String token)      { this.token = token; }
    public String  getUserId()                 { return userId; }
    public void    setUserId(String userId)    { this.userId = userId; }
    public String  getExpiresAt()              { return expiresAt; }
    public void    setExpiresAt(String e)      { this.expiresAt = e; }
    public boolean isUsed()                    { return used; }
    public void    setUsed(boolean used)       { this.used = used; }
    public String  getCreatedAt()              { return createdAt; }
    public void    setCreatedAt(String c)      { this.createdAt = c; }
}