package com.hospital.interfaces;

import com.hospital.enums.AuditEventType;
import com.hospital.model.AuditLog;
import java.util.List;

public interface Auditable {

    void logEvent(AuditEventType eventType, String description);

    List<AuditLog> getAuditLog();

    String getAuditId();

    String getAuditName();
}