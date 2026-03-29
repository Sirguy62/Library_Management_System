package organise.creditrisk.dashboard.service;

import organise.creditrisk.infrastructure.cache.CacheClient;
import organise.creditrisk.loan.model.Loan;
import organise.creditrisk.loan.service.LoanService;

import java.util.List;

public class DashboardService {

    private final CacheClient cache = new CacheClient();
    private final LoanService loanService = new LoanService();

    public List<Loan> getActiveLoans() {

        String key = "dashboard:active_loans";

        Object cached = cache.get(key);

        if (cached != null) {
            System.out.println("CACHE HIT");
            return (List<Loan>) cached;
        }

        System.out.println("CACHE MISS → DB HIT");

        List<Loan> loans = loanService.getActiveLoans();

        cache.put(key, loans);

        return loans;
    }
}