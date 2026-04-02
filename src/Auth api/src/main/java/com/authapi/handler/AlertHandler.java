package com.authapi.handler;

import com.authapi.engine.RiskCalculator;
import com.authapi.middleware.AuthMiddleware;
import com.authapi.model.Loan;
import com.authapi.model.RiskScore;
import com.authapi.store.LoanStore;
import com.authapi.Util.HttpUtil;
import com.authapi.Util.JsonUtil;
import com.sun.net.httpserver.HttpExchange;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class AlertHandler {

    private final LoanStore      loanStore;
    private final RiskCalculator riskCalculator;
    private final AuthMiddleware authMiddleware;

    public AlertHandler(LoanStore loanStore, RiskCalculator riskCalculator, AuthMiddleware authMiddleware) {
        this.loanStore      = loanStore;
        this.riskCalculator = riskCalculator;
        this.authMiddleware = authMiddleware;
    }

    public void getOverdueAlerts(HttpExchange exchange) throws IOException {
        String userId = authMiddleware.authenticate(exchange);
        if (userId == null) return;

        List<Loan> allLoans = loanStore.findAll();

        List<Map<String, Object>> overdueLoans = allLoans.stream()
                .filter(loan -> !loan.isFullyPaid())
                .map(loan -> {
                    int daysPastDue = loanStore.calculateDaysPastDue(loan); // pass loan directly
                    if (daysPastDue == 0) return null;

                    Map<String, Object> entry = new HashMap<>();
                    entry.put("loanId",          loan.getId());
                    entry.put("borrowerName",     loan.getBorrowerName());
                    entry.put("amount",           loan.getAmount());
                    entry.put("remainingBalance", loan.getRemainingBalance());
                    entry.put("daysPastDue",      daysPastDue);
                    entry.put("dueDate",          loan.getDueDate());
                    entry.put("status",           loan.getStatus());
                    return entry;
                })
                .filter(entry -> entry != null)
                .sorted((a, b) -> Integer.compare(
                        (int) b.get("daysPastDue"),
                        (int) a.get("daysPastDue")
                ))
                .collect(Collectors.toList());

        int highestDaysPastDue = overdueLoans.stream()
                .mapToInt(e -> (int) e.get("daysPastDue"))
                .max()
                .orElse(0);

        Map<String, Object> response = new HashMap<>();
        response.put("totalLoans",        allLoans.size());
        response.put("totalOverdue",       overdueLoans.size());
        response.put("highestDaysPastDue", highestDaysPastDue);
        response.put("overdueLoans",       overdueLoans);

        HttpUtil.sendResponse(exchange, 200, JsonUtil.toJson(response));
    }

    public void getHighRiskAlerts(HttpExchange exchange) throws IOException {
        String userId = authMiddleware.authenticate(exchange);
        if (userId == null) return;

        List<RiskScore> highRiskScores = riskCalculator.calculateAllLoans()
                .stream()
                .filter(s -> s.getRiskLevel().equals("HIGH"))
                .sorted((a, b) -> Integer.compare(b.getScore(), a.getScore()))
                .collect(Collectors.toList());

        double totalExposure = highRiskScores.stream()
                .mapToDouble(RiskScore::getRemainingBalance)
                .sum();

        List<Map<String, Object>> alerts = highRiskScores.stream()
                .map(score -> {
                    Map<String, Object> alert = new HashMap<>();
                    alert.put("loanId",          score.getLoanId());
                    alert.put("borrowerName",     score.getBorrowerName());
                    alert.put("score",            score.getScore());
                    alert.put("riskLevel",        score.getRiskLevel());
                    alert.put("remainingBalance", score.getRemainingBalance());
                    alert.put("daysPastDue",      score.getDaysPastDue());
                    alert.put("missedPayments",   score.getMissedPayments());
                    alert.put("action",           resolveAction(score));
                    return alert;
                })
                .collect(Collectors.toList());

        Map<String, Object> response = new HashMap<>();
        response.put("totalHighRisk", highRiskScores.size());
        response.put("totalExposure", totalExposure);
        response.put("alerts",        alerts);

        HttpUtil.sendResponse(exchange, 200, JsonUtil.toJson(response));
    }

    private String resolveAction(RiskScore score) {
        if (score.getDaysPastDue() > 30)      return "IMMEDIATE ACTION — escalate to collections";
        if (score.getMissedPayments() >= 3)   return "URGENT — contact borrower immediately";
        if (score.getCollateralRatio() < 0.5) return "WARNING — request additional collateral";
        return "MONITOR — schedule follow up call";
    }
}