package com.hospital.exception;

public class TokenExpiredException extends AppException {

    public TokenExpiredException(String message) {
        super(401, "TOKEN_EXPIRED", message);
    }

    public static TokenExpiredException accessToken() {
        return new TokenExpiredException("Access token has expired");
    }

    public static TokenExpiredException refreshToken() {
        return new TokenExpiredException("Refresh token has expired — please log in again");
    }

    public static TokenExpiredException verificationToken() {
        return new TokenExpiredException("Verification link has expired — request a new one");
    }
}