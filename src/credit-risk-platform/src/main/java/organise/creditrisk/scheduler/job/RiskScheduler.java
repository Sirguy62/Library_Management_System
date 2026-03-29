package organise.creditrisk.scheduler.job;

import organise.creditrisk.risk.service.RiskService;

import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

public class RiskScheduler {

    public void start() {

        ScheduledExecutorService executor =
                Executors.newSingleThreadScheduledExecutor();

        RiskService riskService = new RiskService();

        executor.scheduleAtFixedRate(
                riskService::runRiskAssessment,
                10,
                300,
                TimeUnit.SECONDS
        );

        System.out.println("Risk scheduler started");
    }
}