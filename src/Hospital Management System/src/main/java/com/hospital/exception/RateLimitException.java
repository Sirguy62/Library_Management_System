package com.hospital.exception;

public class RateLimitException extends AppException {

    private final int retryAfterSeconds;

    public RateLimitException(int retryAfterSeconds) {
        super(429, "RATE_LIMITED",
                "Too many attempts. Try again in " + retryAfterSeconds + " seconds");
        this.retryAfterSeconds = retryAfterSeconds;
    }

    public int getRetryAfterSeconds() {
        return retryAfterSeconds;
    }

    @Override
    public String toJson() {
        return String.format(
                "{\"error\": \"%s\", \"message\": \"%s\", " +
                        "\"statusCode\": %d, \"retryAfter\": %d}",
                getErrorCode(), getMessage(), getStatusCode(), retryAfterSeconds
        );
    }
}