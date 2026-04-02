package com.authapi.engine;

import com.authapi.model.Loan;
import com.authapi.model.Payment;
import com.authapi.model.RiskScore;
import com.authapi.store.LoanStore;

import java.util.List;
import java.util.stream.Collectors;

public class RiskCalculator {

    private final LoanStore loanStore;

    public RiskCalculator(LoanStore loanStore) {
        this.loanStore = loanStore;
    }

    public RiskScore calculate(Loan loan) {
        List<Payment> payments = loanStore.findPaymentsByLoanId(loan.getId());
        int daysPastDue        = loanStore.calculateDaysPastDue(loan);
        int missedPayments     = loanStore.countMissedPayments(loan, payments);

        RiskScore riskScore = new RiskScore(loan, daysPastDue, missedPayments);

        int score = 0;
        score += scoreDaysPastDue(daysPastDue);
        score += scoreMissedPayments(missedPayments);
        score += scoreCollateral(loan);
        score  = Math.min(score, 100);

        riskScore.setScore(score);
        riskScore.setRiskLevel(resolveRiskLevel(score));

        return riskScore;
    }

    public List<RiskScore> calculateAll(String userId) {
        return loanStore.findByUserId(userId)
                .stream()
                .filter(loan -> !loan.isFullyPaid())
                .map(this::calculate)
                .collect(Collectors.toList());
    }

    public List<RiskScore> calculateAllLoans() {
        return loanStore.findAll()
                .stream()
                .filter(loan -> !loan.isFullyPaid())
                .map(this::calculate)
                .collect(Collectors.toList());
    }

    private int scoreDaysPastDue(int days) {
        if (days == 0)   return 0;
        if (days <= 10)  return 20;
        if (days <= 30)  return 40;
        return 60;
    }

    private int scoreMissedPayments(int missed) {
        if (missed == 0)  return 0;
        if (missed <= 2)  return 20;
        return 40;
    }

    private int scoreCollateral(Loan loan) {
        double collateral = loan.getCollateral();
        double amount     = loan.getAmount();
        if (collateral <= 0)      return 30;
        if (collateral >= amount) return 0;
        return 20;
    }

    private String resolveRiskLevel(int score) {
        if (score <= 30)  return "LOW";
        if (score <= 60)  return "MEDIUM";
        return "HIGH";
    }
}