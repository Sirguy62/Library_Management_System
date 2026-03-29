package organise.creditrisk.payment.model;

import java.time.LocalDateTime;

public class Payment {

    private Long id;
    private Long loanId;
    private double amount;
    private LocalDateTime paymentTime;

    public Payment(Long id, Long loanId, double amount, LocalDateTime paymentTime) {
        this.id = id;
        this.loanId = loanId;
        this.amount = amount;
        this.paymentTime = paymentTime;
    }

    public Long getId() { return id; }
    public Long getLoanId() { return loanId; }
    public double getAmount() { return amount; }
    public LocalDateTime getPaymentTime() { return paymentTime; }
}