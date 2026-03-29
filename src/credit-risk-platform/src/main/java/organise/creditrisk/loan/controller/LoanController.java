package organise.creditrisk.loan.controller;

import organise.creditrisk.loan.service.LoanService;

public class LoanController {

    private final LoanService loanService = new LoanService();

    public String getLoans() {
        return loanService.getActiveLoans().toString();
    }
}