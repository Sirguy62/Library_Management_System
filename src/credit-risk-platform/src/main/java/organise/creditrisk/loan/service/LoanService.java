package organise.creditrisk.loan.service;

import organise.creditrisk.loan.model.Loan;
import organise.creditrisk.loan.repository.LoanRepository;

import java.util.List;

public class LoanService {

    private final LoanRepository loanRepository = new LoanRepository();

    public List<Loan> getActiveLoans() {
        return loanRepository.findAllActiveLoans();
    }
}