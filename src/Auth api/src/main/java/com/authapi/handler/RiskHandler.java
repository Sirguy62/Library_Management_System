package com.authapi.handler;

import com.authapi.Util.HttpUtil;
import com.authapi.Util.JsonUtil;
import com.authapi.engine.RiskCalculator;
import com.authapi.middleware.AuthMiddleware;
import com.authapi.model.RiskScore;
import com.sun.net.httpserver.HttpExchange;

import java.io.IOException;
import java.io.OutputStream;
import java.util.List;
import java.util.stream.Collectors;

public class RiskHandler {

    private final RiskCalculator riskCalculator;
    private final AuthMiddleware authMiddleware;


    public RiskHandler(AuthMiddleware authMiddleware, RiskCalculator riskCalculator) {
        this.authMiddleware = authMiddleware;
        this.riskCalculator = riskCalculator;
    }

    public void getRiskScore(HttpExchange exchange) throws IOException {

        // check token
        String userId = exchange.getRequestHeaders().getFirst("user-id");
        if (userId == null) {
            return;
        }

        //calculate risl score for all loans
        List<RiskScore> scores = riskCalculator.calculateAllLoans();

        //build summary
        long lowCount = scores.stream().filter(s -> s.getRiskLevel().equals("LOW")).count();
        long mediumCount = scores.stream().filter(s -> s.getRiskLevel().equals("MEDIUM")).count();
        long highCount = scores.stream().filter(s -> s.getRiskLevel().equals("HIGH")).count();


        java.util.Map<String, Object> response = new java.util.HashMap<>();
        response.put("totalLoans",    scores.size());
        response.put("lowRisk",       lowCount);
        response.put("mediumRisk",    mediumCount);
        response.put("highRisk",      highCount);
        response.put("scores",        scores);

        HttpUtil.sendResponse(exchange, 200, JsonUtil.toJson(response));

    }

    public void getHighRiskLoans(HttpExchange exchange) throws IOException {
        // 1. Check token
        String userId = authMiddleware.authenticate(exchange);
        if (userId == null) return;

        // 2. Get all scores then filter to HIGH only
        List<RiskScore> highRisk = riskCalculator.calculateAllLoans()
                .stream()
                .filter(s -> s.getRiskLevel().equals("HIGH"))
                .collect(Collectors.toList());

        // 3. Sort by score descending — worst loans first
        highRisk.sort((a, b) -> Integer.compare(b.getScore(), a.getScore()));

        // 4. Build response
        java.util.Map<String, Object> response = new java.util.HashMap<>();
        response.put("totalHighRisk", highRisk.size());
        response.put("loans",         highRisk);

        HttpUtil.sendResponse(exchange, 200, JsonUtil.toJson(response));
    }



    public void getMyRiskScore(HttpExchange exchange) throws IOException {
        // check token
        String userId = authMiddleware.authenticate(exchange);
        if (userId == null) {
            return;
        }

        // calculate score for only this user
        List<RiskScore> score = riskCalculator.calculateAllLoans();

        // build respone
        java.util.Map<String, Object> response = new java.util.HashMap<>();
        response.put("totalLoans", score.size());
        response.put("score", score);

        HttpUtil.sendResponse(exchange,200, JsonUtil.toJson(response));
    }
}













