package com.hospital.model.base;

import com.hospital.enums.UserRole;
import com.hospital.enums.UserStatus;
import com.hospital.interfaces.Auditable;
import com.hospital.interfaces.Notifiable;
import com.hospital.interfaces.Verifiable;

import java.time.Instant;
import java.util.UUID;

public abstract class Person implements Verifiable, Notifiable, Auditable {

    private String     id;
    private String     email;
    private String     username;
    private String     passwordHash;
    private UserRole   role;
    private UserStatus status;
    private int        failedAttempts;
    private String     lockedUntil;
    private String     createdAt;

    protected Person(String email, String username,
                     String passwordHash, UserRole role) {
        this.id             = UUID.randomUUID().toString();
        this.email          = email.trim().toLowerCase();
        this.username       = username;
        this.passwordHash   = passwordHash;
        this.role           = role;
        this.status         = UserStatus.UNVERIFIED;
        this.failedAttempts = 0;
        this.lockedUntil    = null;
        this.createdAt      = Instant.now().toString();
    }

    protected Person() {}

    public abstract String getRoleDescription();

    public abstract String getDisplayName();

    @Override
    public boolean isVerified() {
        return status == UserStatus.VERIFIED;
    }

    @Override
    public void markVerified() {
        this.status = UserStatus.VERIFIED;
    }

    @Override
    public String getVerificationEmail() {
        return email;
    }

    @Override
    public String getNotificationEmail() {
        return email;
    }

    @Override
    public String getNotificationName() {
        return getDisplayName();
    }

    @Override
    public boolean isNotificationsEnabled() {
        return isVerified();
    }

    @Override
    public String getAuditId() {
        return id;
    }

    @Override
    public String getAuditName() {
        return getDisplayName();
    }

    public boolean isLocked() {
        if (status != UserStatus.LOCKED) return false;
        if (lockedUntil == null) return false;
        return Instant.now().isBefore(Instant.parse(lockedUntil));
    }

    public void incrementFailedAttempts() {
        this.failedAttempts++;
    }

    public void lockAccount(int minutes) {
        this.status      = UserStatus.LOCKED;
        this.lockedUntil = Instant.now()
                .plusSeconds(minutes * 60L)
                .toString();
    }

    public void unlockAccount() {
        this.status         = UserStatus.VERIFIED;
        this.failedAttempts = 0;
        this.lockedUntil    = null;
    }

    public void resetFailedAttempts() {
        this.failedAttempts = 0;
    }

    public String     getId()                              { return id; }
    public void       setId(String id)                     { this.id = id; }
    public String     getEmail()                           { return email; }
    public void       setEmail(String email)               { this.email = email; }
    public String     getUsername()                        { return username; }
    public void       setUsername(String username)         { this.username = username; }
    public String     getPasswordHash()                    { return passwordHash; }
    public void       setPasswordHash(String passwordHash) { this.passwordHash = passwordHash; }
    public UserRole   getRole()                            { return role; }
    public void       setRole(UserRole role)               { this.role = role; }
    public UserStatus getStatus()                          { return status; }
    public void       setStatus(UserStatus status)         { this.status = status; }
    public int        getFailedAttempts()                  { return failedAttempts; }
    public void       setFailedAttempts(int failedAttempts){ this.failedAttempts = failedAttempts; }
    public String     getLockedUntil()                     { return lockedUntil; }
    public void       setLockedUntil(String lockedUntil)   { this.lockedUntil = lockedUntil; }
    public String     getCreatedAt()                       { return createdAt; }
    public void       setCreatedAt(String createdAt)       { this.createdAt = createdAt; }
}