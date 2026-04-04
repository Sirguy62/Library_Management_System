package com.hospital.exception;

public class DatabaseException extends AppException {

    public DatabaseException(String message, Throwable cause) {
        super(500, "DATABASE_ERROR", message);
        initCause(cause);
    }

    public static DatabaseException wrap(String operation, Throwable cause) {
        return new DatabaseException(
                "Database error during: " + operation, cause);
    }
}