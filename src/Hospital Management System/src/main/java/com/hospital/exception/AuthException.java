package com.hospital.exception;

public class AuthException extends AppException {

    public AuthException(String message) {
        super(401, "AUTH_ERROR", message);
    }

    public AuthException(String errorCode, String message) {
        super(401, errorCode, message);
    }

    public static AuthException invalidCredentials() {
        return new AuthException("INVALID_CREDENTIALS", "Invalid email or password");
    }

    public static AuthException emailNotVerified() {
        return new AuthException("EMAIL_NOT_VERIFIED",
                "Please verify your email before logging in");
    }

    public static AuthException unauthorized() {
        return new AuthException("UNAUTHORIZED", "Authentication required");
    }
}