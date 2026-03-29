package organise.creditrisk.payment.service;

import organise.creditrisk.payment.repository.IdempotencyRepository;
import organise.creditrisk.payment.repository.PaymentRepository;
import organise.creditrisk.infrastructure.messaging.EventBus;
import organise.creditrisk.infrastructure.messaging.PaymentEvent;

public class PaymentService {

    private final PaymentRepository paymentRepository = new PaymentRepository();
    private final IdempotencyRepository idempotencyRepository = new IdempotencyRepository();

    public void makePayment(Long loanId, double amount, String idempotencyKey) {

        if (idempotencyRepository.exists(idempotencyKey)) {
            System.out.println("Duplicate payment request ignored");
            return;
        }

        paymentRepository.savePayment(loanId, amount);

        idempotencyRepository.save(idempotencyKey);
        EventBus.publish(new PaymentEvent(loanId));

    }
}