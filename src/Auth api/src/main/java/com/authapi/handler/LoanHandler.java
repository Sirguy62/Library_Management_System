package com.authapi.handler;

import com.authapi.Util.HttpUtil;
import com.authapi.Util.JsonUtil;
import com.authapi.middleware.AuthMiddleware;
import com.authapi.model.Loan;
import com.authapi.store.LoanStore;
import com.sun.net.httpserver.HttpExchange;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

public class LoanHandler {

    private final LoanStore loanStore;
    private final AuthMiddleware authMiddleware;

    public LoanHandler(LoanStore loanStore, AuthMiddleware authMiddleware) {
        this.loanStore = loanStore;
        this.authMiddleware = authMiddleware;
    }

    public void createLoan(HttpExchange exchange) throws IOException {
        // check if user have token

        String userId = authMiddleware.authenticate(exchange);
        System.out.println("Broke here");
        if (userId == null) return;

        // read the body
        Map<String, Object> body;
        try {
            body = JsonUtil.fromJson(exchange, HashMap.class);
        } catch (Exception e) {
            HttpUtil.sendError(exchange, 400, "Invalid body");
            return;
        }

        // pull field
        String borrowerName = (String) body.get("borrowerName");
        String dueDate = (String) body.get("dueDate");
        double amount = 0;
        double collateral = 0;

        try {
            amount = ((Number) body.get("amount")).doubleValue();
            collateral = ((Number) body.get("collateral")).doubleValue();
        } catch (Exception e) {
            HttpUtil.sendError(exchange, 400, "Invalid amount, amount and collateral must be a number");
            return;
        }

        // validate
        if (borrowerName == null || borrowerName.isBlank()) {
            System.out.println("borrowerName is null or blank");
            HttpUtil.sendError(exchange, 400, "borrower name is required");
            return;
        }

        if (dueDate == null || dueDate.isBlank()) {
            HttpUtil.sendError(exchange, 400, "due date is required");
            return;
        }

        if (amount <= 0) {
            HttpUtil.sendError(exchange, 400, "amount must be above zero");
            return;
        }

        // validate due date format
        try {
            java.time.Instant.parse(dueDate);
        } catch (Exception e) {
            HttpUtil.sendError(exchange, 400, "due date must be a date");
            return;
        }

        // create and save
        Loan loan = new Loan(userId, borrowerName, amount, collateral, dueDate);
        loanStore.save(loan);

        HttpUtil.sendResponse(exchange, 200, JsonUtil.toJson(loan));
    }

    public void getLoans(HttpExchange exchange) throws IOException {
        // also check token
        String userId = authMiddleware.authenticate(exchange);
        if (userId == null) return;

        // get all loans for this user
        List<Loan> userLoans = loanStore.findByUserId(userId);

        HttpUtil.sendResponse(exchange, 200, JsonUtil.toJson(userLoans));
    }

    //get loan by id
    public void getLoan(HttpExchange exchange) throws IOException {
        //check token
        String userId = authMiddleware.authenticate(exchange);
        if (userId == null) return;

        //extract loan id from path
        String loanId = extractId(exchange.getRequestURI().getPath());
        if (loanId == null) {
            HttpUtil.sendError(exchange, 400, "Invalid loan id");
            return;
        }

        // find the loan
        Optional<Loan> loanOpt = loanStore.findById(loanId);
        if (loanOpt.isEmpty()) {
            HttpUtil.sendError(exchange, 400, "Loan not found");
            return;
        }

        // make sure loan belong to a particular user
        Loan loan = loanOpt.get();
        if (!loan.getUserId().equals(userId)) {
            HttpUtil.sendError(exchange, 400, "Access denied");
            return;
        }

        HttpUtil.sendResponse(exchange, 200, JsonUtil.toJson(loan));
    }

    // update load
    public void updateLoan(HttpExchange exchange) throws IOException {
        // check token
        String userId = authMiddleware.authenticate(exchange);
        if (userId == null) return;

        //extract Loan id
        String loanId = extractId(exchange.getRequestURI().getPath());
        if (loanId == null) {
            HttpUtil.sendError(exchange, 400, "Invalid loan id");
            return;
        }

        //find loan
        Optional<Loan> loanOpt = loanStore.findById(loanId);
        if (loanOpt.isEmpty()) {
            HttpUtil.sendError(exchange, 400, "Loan not found");
            return;
        }

        // check if user owns this
        Loan loan = loanOpt.get();
        if (!loan.getUserId().equals(userId)) {
            HttpUtil.sendError(exchange, 400, "Access denied");
            return;
        }

        //read body
        Map<String, Object> body;
        try {
            body = JsonUtil.fromJson(exchange, HashMap.class);
        }  catch (Exception e) {
            HttpUtil.sendError(exchange, 400, "Invalid body");
            return;
        }

        //apply updates
        if (body.containsKey("borrowerName")) {
            loan.setBorrowerName((String) body.get("borrowerName"));
        }

        if (body.containsKey("status")) {
            loan.setStatus((String) body.get("status"));
        }

        if (body.containsKey("collateral")) {
            loan.setCollateral(((Number) body.get("collateral")).doubleValue());
        }

        if (body.containsKey("dueDate")) {
            try{
                java.time.Instant.parse((String) body.get("dueDate"));
                loan.setDueDate((String) body.get("dueDate"));
            } catch (Exception e){
                HttpUtil.sendError(exchange, 400, "Invalid date format");
                return;
            }
        }

        //save and respond
        loanStore.save(loan);
        HttpUtil.sendResponse(exchange, 200, JsonUtil.toJson(loan));
    }

    public void deleteLoan(HttpExchange exchange) throws IOException {
        // check token
        String userId = authMiddleware.authenticate(exchange);
        if (userId == null) return;

        //extract loan id
        String loanId = extractId(exchange.getRequestURI().getPath());
        if (loanId == null) {
            HttpUtil.sendError(exchange, 400, "Invalid loan id");
            return;
        }

        //find the loan
        Optional<Loan> loanOpt = loanStore.findById(loanId);
        if (loanOpt.isEmpty()) {
            HttpUtil.sendError(exchange, 400, "Loan not found");
            return;
        }

        // check if user owns this
        Loan loan = loanOpt.get();
        if (!loan.getUserId().equals(userId)) {
            HttpUtil.sendError(exchange, 400, "Access denied");
            return;
        }

        // delete loan
        loanStore.delete(loanId);

        Map<String, String> response = new HashMap<>();
        response.put("message", "Loan deleted successfully");
        HttpUtil.sendResponse(exchange, 200, JsonUtil.toJson(response));
    }

    private String extractId(String path) {
        String[] parts = path.split("/");
        return parts.length >= 3 ? parts[2] : null;
    }
}
















