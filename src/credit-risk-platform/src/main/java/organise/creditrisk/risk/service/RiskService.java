package organise.creditrisk.risk.service;

import organise.creditrisk.alert.service.AlertService;
import organise.creditrisk.loan.model.Loan;
import organise.creditrisk.loan.service.LoanService;
import organise.creditrisk.risk.model.RiskScore;
import organise.creditrisk.risk.repository.RiskRepository;
import organise.creditrisk.risk.rules.RiskRules;

import java.util.List;

public class RiskService {

    private final LoanService loanService = new LoanService();
    private final RiskRules rules = new RiskRules();
    private final RiskRepository repository = new RiskRepository();
    private final AlertService alertService = new AlertService();

    public void runRiskAssessment() {

        List<Loan> loans = loanService.getActiveLoans();

        for (Loan loan : loans) {

            RiskScore score = rules.evaluate(loan);
            repository.save(score);

            System.out.println("Risk calculated for loan " + loan.getId());
            alertService.handleRisk(score);
        }

    }
}