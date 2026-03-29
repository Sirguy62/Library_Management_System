package organise.creditrisk.loan.model;

import organise.creditrisk.common.enums.LoanStatus;

import java.time.LocalDate;

public class Loan {

    private Long id;
    private Long customerId;
    private double amount;
    private double collateralValue;
    private LoanStatus status;
    private LocalDate dueDate;
    private LocalDate lastPaymentDate;

    public Loan(Long id,
                Long customerId,
                double amount,
                double collateralValue,
                LoanStatus status,
                LocalDate dueDate,
                LocalDate lastPaymentDate) {

        this.id = id;
        this.customerId = customerId;
        this.amount = amount;
        this.collateralValue = collateralValue;
        this.status = status;
        this.dueDate = dueDate;
        this.lastPaymentDate = lastPaymentDate;
    }

    public Long getId() { return id; }
    public Long getCustomerId() { return customerId; }
    public double getAmount() { return amount; }
    public double getCollateralValue() { return collateralValue; }
    public LoanStatus getStatus() { return status; }
    public LocalDate getDueDate() { return dueDate; }
    public LocalDate getLastPaymentDate() { return lastPaymentDate; }
}