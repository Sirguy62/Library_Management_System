package com.hospital.exception;

public class ForbiddenException extends AppException {

    public ForbiddenException(String message) {
        super(403, "FORBIDDEN", message);
    }

    public static ForbiddenException insufficientRole() {
        return new ForbiddenException(
                "You do not have permission to perform this action");
    }

    public static ForbiddenException notYourResource() {
        return new ForbiddenException(
                "You can only access your own resources");
    }
}