package organise.creditrisk;

import organise.creditrisk.gateway.server.ApiServer;
import organise.creditrisk.risk.consumer.RiskEventConsumer;
import organise.creditrisk.scheduler.job.RiskScheduler;

// This file is the brain bootloader.
public class Application {

    public static void main(String[] args) throws Exception {

        // starts HTTP server
        new ApiServer().start();
        // starts background scheduler thread
        new RiskScheduler().start();
        // starts Kafka consumer loop
        new RiskEventConsumer().start();

    }
}