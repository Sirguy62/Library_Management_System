package com.hospital.model;

import com.hospital.enums.AuditEventType;

import java.time.Instant;
import java.util.UUID;

public class AuditLog {

    private String         id;
    private String         userId;
    private AuditEventType eventType;
    private String         description;
    private String         ipAddress;
    private String         createdAt;

    public AuditLog() {}

    public AuditLog(String userId, AuditEventType eventType,
                    String description) {
        this.id          = UUID.randomUUID().toString();
        this.userId      = userId;
        this.eventType   = eventType;
        this.description = description;
        this.createdAt   = Instant.now().toString();
    }

    public AuditLog(String userId, AuditEventType eventType,
                    String description, String ipAddress) {
        this(userId, eventType, description);
        this.ipAddress = ipAddress;
    }

    public String         getId()                          { return id; }
    public void           setId(String id)                 { this.id = id; }
    public String         getUserId()                      { return userId; }
    public void           setUserId(String userId)         { this.userId = userId; }
    public AuditEventType getEventType()                   { return eventType; }
    public void           setEventType(AuditEventType e)   { this.eventType = e; }
    public String         getDescription()                 { return description; }
    public void           setDescription(String d)         { this.description = d; }
    public String         getIpAddress()                   { return ipAddress; }
    public void           setIpAddress(String ipAddress)   { this.ipAddress = ipAddress; }
    public String         getCreatedAt()                   { return createdAt; }
    public void           setCreatedAt(String createdAt)   { this.createdAt = createdAt; }
}
