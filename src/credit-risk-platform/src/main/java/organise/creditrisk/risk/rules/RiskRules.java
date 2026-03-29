package organise.creditrisk.risk.rules;

import organise.creditrisk.common.enums.RiskLevel;
import organise.creditrisk.loan.model.Loan;
import organise.creditrisk.risk.model.RiskScore;

import java.time.LocalDate;
import java.time.temporal.ChronoUnit;

public class RiskRules {

    public RiskScore evaluate(Loan loan) {

        int score = 0;
        String reason = "Healthy";

        long daysPastDue =
                ChronoUnit.DAYS.between(loan.getDueDate(), LocalDate.now());

        if (daysPastDue > 60) {
            score += 80;
            reason = "Severely overdue";
        }
        else if (daysPastDue > 30) {
            score += 50;
            reason = "Overdue";
        }

        if (loan.getCollateralValue() < loan.getAmount()) {
            score += 30;
            reason = "Under-collateralized";
        }

        RiskLevel level;

        if (score >= 80) level = RiskLevel.CRITICAL;
        else if (score >= 50) level = RiskLevel.HIGH;
        else if (score >= 20) level = RiskLevel.MEDIUM;
        else level = RiskLevel.LOW;

        return new RiskScore(loan.getId(), score, level, reason);
    }
}