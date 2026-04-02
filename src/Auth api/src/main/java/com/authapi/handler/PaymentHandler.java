package com.authapi.handler;

import com.authapi.Util.HttpUtil;
import com.authapi.Util.JsonUtil;
import com.authapi.middleware.AuthMiddleware;
import com.authapi.model.Loan;
import com.authapi.model.Payment;
import com.authapi.store.LoanStore;
import com.sun.net.httpserver.HttpExchange;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

public class PaymentHandler {

    private final LoanStore loanStore;
    private final AuthMiddleware authMiddleWare;

    public PaymentHandler(LoanStore loanStore, AuthMiddleware authMiddleWare) {
        this.loanStore = loanStore;
        this.authMiddleWare = authMiddleWare;
    }


    public void recordPayment(HttpExchange exchange) throws IOException {
       // check token
       String userId = authMiddleWare.authenticate(exchange);
       if (userId == null) {
           return;
       }

       // extract loan id from path
        String loanId = extractLoanId(exchange.getRequestURI().getPath());
       if (loanId == null) {
           HttpUtil.sendError(exchange, 400, "Invalid loan ID");
           return;
       }

       // find the loan
        Optional<Loan> loanOpt = loanStore.findById(loanId);
       if (loanOpt.isEmpty()) {
           HttpUtil.sendError(exchange, 400, "Loan not found");
           return;
       }

       // owner check
        Loan loan = loanOpt.get();
       if (!loan.getUserId().equals(userId)) {
           HttpUtil.sendError(exchange, 400, "Access denied");
           return;
       }

       // check if loan is still active
        if (loan.isFullyPaid()) {
            HttpUtil.sendError(exchange, 400, "Loan is already fully paid");
            return;
        }

        // read body
        Map<String, Object> body;
        try {
            body = JsonUtil.fromJson(exchange, HashMap.class);
        } catch (Exception e) {
            HttpUtil.sendError(exchange, 400, "Invalid JSON body");
            return;
        }

        // pull out field
        double amount = 0;
        try {
            amount = ((Number) body.get("amount")).doubleValue();
        }catch (Exception e) {
            HttpUtil.sendError(exchange, 400, "Invalid amount, it must be a number");
            return;
        }

        String note = (String) body.getOrDefault("note", "");

        // validate amount
        if (amount <= 0) {
            HttpUtil.sendError(exchange, 400, "Invalid amount, it must be greater than zero");
            return;
        }

        double remaining = loan.getRemainingBalance();
        if (amount > remaining) {
            HttpUtil.sendError(exchange, 400, "Payment exceeds remaining balance of" + remaining);
            return;
        }

        // record payment
        Payment payment = new Payment(userId, amount, note, loanId);
        loanStore.savePayment(payment);


        //  update loan paid
        loan.setAmountPaid(loan.getAmountPaid() + amount);
        if (loan.isFullyPaid()) {
            loan.setStatus("PAID");
        }

        loanStore.update(loan);


        Map<String, Object> response = new HashMap<>();
        response.put("payment", payment);
        response.put("remainingBalance", loan.getRemainingBalance());
        response.put("loanStatus", loan.getStatus());

        HttpUtil.sendResponse(exchange, 201, JsonUtil.toJson(response));

    }


    public void getPayment(HttpExchange exchange) throws IOException {
        String userId = authMiddleWare.authenticate(exchange);
        if (userId == null) {
            return;
        }

        String loanId = extractLoanId(exchange.getRequestURI().getPath());
        if (loanId == null) {
            HttpUtil.sendError(exchange, 400, "Invalid loan ID");
            return;
        }

        Optional<Loan> loanOpt = loanStore.findById(loanId);
        if (loanOpt.isEmpty()) {
            HttpUtil.sendError(exchange, 400, "Loan not found");
            return;
        }

        Loan loan = loanOpt.get();
        if (!loan.getUserId().equals(userId)) {
            HttpUtil.sendError(exchange, 400, "Access denied");
            return;
        }

        List<Payment> loanPayments = loanStore.findPaymentsByLoanId(loanId);

        Map<String, Object> response = new HashMap<>();
        response.put("payments", loanPayments);
        response.put("remainingBalance", loan.getRemainingBalance());
        response.put("loanStatus", loan.getStatus());
        response.put("loanId", loanId);
        response.put("amountPaid", loan.getAmountPaid());
        response.put("totalAmount", loan.getAmount());
        response.put("borrowerName", loan.getBorrowerName());


        HttpUtil.sendResponse(exchange, 200, JsonUtil.toJson(response));
    }


    private String extractLoanId(String path) {
        String[] parts = path.split("/");
        return parts.length >= 3 ? parts[2] : null;
    }
}
