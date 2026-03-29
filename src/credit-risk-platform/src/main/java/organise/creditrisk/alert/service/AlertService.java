package organise.creditrisk.alert.service;

import organise.creditrisk.alert.email.EmailSender;
import organise.creditrisk.alert.model.Alert;
import organise.creditrisk.alert.repository.AlertRepository;
import organise.creditrisk.common.enums.RiskLevel;
import organise.creditrisk.risk.model.RiskScore;

import java.time.LocalDateTime;

public class AlertService {

    private final AlertRepository repository = new AlertRepository();
    private final EmailSender emailSender = new EmailSender();

    public void handleRisk(RiskScore score) {

        if (score.getLevel() == RiskLevel.HIGH
                || score.getLevel() == RiskLevel.CRITICAL) {

            String message = "Loan " + score.getLoanId()
                    + " is at " + score.getLevel()
                    + " risk. Reason: " + score.getReason();

            Alert alert = new Alert(
                    score.getLoanId(),
                    score.getLevel(),
                    message,
                    LocalDateTime.now()
            );

            repository.save(alert);

            emailSender.send("CREDIT RISK ALERT", message);
        }
    }
}