package organise.creditrisk.risk.model;

import organise.creditrisk.common.enums.RiskLevel;

public class RiskScore {

    private Long loanId;
    private int score;
    private RiskLevel level;
    private String reason;

    public RiskScore(Long loanId, int score, RiskLevel level, String reason) {
        this.loanId = loanId;
        this.score = score;
        this.level = level;
        this.reason = reason;
    }

    public Long getLoanId() { return loanId; }
    public int getScore() { return score; }
    public RiskLevel getLevel() { return level; }
    public String getReason() { return reason; }
}