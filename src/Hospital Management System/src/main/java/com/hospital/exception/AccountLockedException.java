package com.hospital.exception;

public class AccountLockedException extends AppException {

    private final String lockedUntil;

    public AccountLockedException(String lockedUntil) {
        super(423, "ACCOUNT_LOCKED",
                "Account is locked until " + lockedUntil);
        this.lockedUntil = lockedUntil;
    }

    public String getLockedUntil() {
        return lockedUntil;
    }

    @Override
    public String toJson() {
        return String.format(
                "{\"error\": \"%s\", \"message\": \"%s\", " +
                        "\"statusCode\": %d, \"lockedUntil\": \"%s\"}",
                getErrorCode(), getMessage(), getStatusCode(), lockedUntil
        );
    }
}