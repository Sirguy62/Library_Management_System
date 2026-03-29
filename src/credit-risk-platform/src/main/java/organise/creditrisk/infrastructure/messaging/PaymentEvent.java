package organise.creditrisk.infrastructure.messaging;

public class PaymentEvent implements Event {

    private Long loanId;

    public PaymentEvent(Long loanId) {
        this.loanId = loanId;
    }

    public Long getLoanId() {
        return loanId;
    }

    @Override
    public String getType() {
        return "PAYMENT_MADE";
    }
}