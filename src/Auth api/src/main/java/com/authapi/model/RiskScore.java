package com.authapi.model;

public class RiskScore {

    private String loanId;
    private String borrowerName;
    private double loanAmount;
    private double remainingBalance;
    private int daysPastDue;
    private int missedPayment;
    private double collateralRatio;
    private int score;
    private String riskLevel;


    public RiskScore() {}

    public RiskScore(Loan loan, int missedPayment, int daysPastDue) {
        this.missedPayment = missedPayment;
        this.daysPastDue = daysPastDue;
        this.loanId = loan.getId();
        this.borrowerName = loan.getBorrowerName();
        this.loanAmount = loan.getAmount();
        this.remainingBalance = loan.getRemainingBalance();
        this.collateralRatio = loan.getAmount() > 0
                ? loan.getCollateral() / loan.getAmount()
                : 0;
    }


    public String getLoanId()                  { return loanId; }
    public void   setLoanId(String loanId)     { this.loanId = loanId; }

    public String getBorrowerName()                        { return borrowerName; }
    public void   setBorrowerName(String borrowerName)     { this.borrowerName = borrowerName; }

    public double getLoanAmount()                    { return loanAmount; }
    public void   setLoanAmount(double loanAmount)   { this.loanAmount = loanAmount; }

    public double getRemainingBalance()                        { return remainingBalance; }
    public void   setRemainingBalance(double remainingBalance) { this.remainingBalance = remainingBalance; }

    public int  getDaysPastDue()                   { return daysPastDue; }
    public void setDaysPastDue(int daysPastDue)    { this.daysPastDue = daysPastDue; }

    public int  getMissedPayments()                      { return missedPayment; }
    public void setMissedPayments(int missedPayments)    { this.missedPayment = missedPayments; }

    public double getCollateralRatio()                       { return collateralRatio; }
    public void   setCollateralRatio(double collateralRatio) { this.collateralRatio = collateralRatio; }

    public int  getScore()               { return score; }
    public void setScore(int score)      { this.score = score; }

    public String getRiskLevel()                   { return riskLevel; }
    public void   setRiskLevel(String riskLevel)   { this.riskLevel = riskLevel; }
}



















