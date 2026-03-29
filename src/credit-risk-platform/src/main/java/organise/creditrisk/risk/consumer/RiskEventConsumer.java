package organise.creditrisk.risk.consumer;

import organise.creditrisk.infrastructure.messaging.EventBus;
import organise.creditrisk.infrastructure.messaging.PaymentEvent;
import organise.creditrisk.loan.model.Loan;
import organise.creditrisk.loan.service.LoanService;
import organise.creditrisk.risk.model.RiskScore;
import organise.creditrisk.risk.repository.RiskRepository;
import organise.creditrisk.risk.rules.RiskRules;

public class RiskEventConsumer {

    private final LoanService loanService = new LoanService();
    private final RiskRules rules = new RiskRules();
    private final RiskRepository repository = new RiskRepository();

    public void start() {

        EventBus.subscribe("PAYMENT_MADE", event -> {

            PaymentEvent pe = (PaymentEvent) event;

            Loan loan = loanService.getActiveLoans()
                    .stream()
                    .filter(l -> l.getId().equals(pe.getLoanId()))
                    .findFirst()
                    .orElse(null);

            if (loan != null) {

                RiskScore score = rules.evaluate(loan);

                repository.save(score);

                System.out.println("Real-time risk recalculated for loan " + loan.getId());
            }
        });
    }
}