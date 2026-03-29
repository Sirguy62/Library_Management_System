package organise.creditrisk.alert.model;

import organise.creditrisk.common.enums.RiskLevel;

import java.time.LocalDateTime;

public class Alert {

    private Long loanId;
    private RiskLevel level;
    private String message;
    private LocalDateTime createdAt;

    public Alert(Long loanId, RiskLevel level, String message, LocalDateTime createdAt) {
        this.loanId = loanId;
        this.level = level;
        this.message = message;
        this.createdAt = createdAt;
    }

    public Long getLoanId() { return loanId; }
    public RiskLevel getLevel() { return level; }
    public String getMessage() { return message; }
    public LocalDateTime getCreatedAt() { return createdAt; }
}