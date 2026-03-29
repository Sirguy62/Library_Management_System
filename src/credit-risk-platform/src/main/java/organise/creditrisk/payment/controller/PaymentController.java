package organise.creditrisk.payment.controller;

import organise.creditrisk.payment.service.PaymentService;

public class PaymentController {

    private final PaymentService paymentService = new PaymentService();

    public String pay(Long loanId, double amount, String key) {

        paymentService.makePayment(loanId, amount, key);

        return "Payment processed";
    }
}