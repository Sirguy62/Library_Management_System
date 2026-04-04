package com.hospital.model;

import java.time.Instant;

public class BlacklistedToken {

    private String tokenId;
    private String expiresAt;
    private String blacklistedAt;

    public BlacklistedToken() {}

    public BlacklistedToken(String tokenId, String expiresAt) {
        this.tokenId       = tokenId;
        this.expiresAt     = expiresAt;
        this.blacklistedAt = Instant.now().toString();
    }

    public boolean hasExpired() {
        return Instant.now().isAfter(Instant.parse(expiresAt));
    }

    public String getTokenId()                   { return tokenId; }
    public void   setTokenId(String tokenId)     { this.tokenId = tokenId; }
    public String getExpiresAt()                 { return expiresAt; }
    public void   setExpiresAt(String expiresAt) { this.expiresAt = expiresAt; }
    public String getBlacklistedAt()             { return blacklistedAt; }
    public void   setBlacklistedAt(String b)     { this.blacklistedAt = b; }
}