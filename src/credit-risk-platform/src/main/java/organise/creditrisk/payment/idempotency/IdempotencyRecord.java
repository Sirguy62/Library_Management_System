package organise.creditrisk.payment.idempotency;

public class IdempotencyRecord {

    private String key;
    private boolean processed;

    public IdempotencyRecord(String key, boolean processed) {
        this.key = key;
        this.processed = processed;
    }

    public String getKey() { return key; }
    public boolean isProcessed() { return processed; }
}