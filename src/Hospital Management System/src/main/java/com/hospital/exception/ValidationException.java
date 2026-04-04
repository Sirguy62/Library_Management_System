package com.hospital.exception;

import java.util.List;

public class ValidationException extends AppException {

    private final List<String> errors;

    public ValidationException(String message) {
        super(400, "VALIDATION_ERROR", message);
        this.errors = List.of(message);
    }

    public ValidationException(List<String> errors) {
        super(400, "VALIDATION_ERROR", "Validation failed");
        this.errors = errors;
    }

    public List<String> getErrors() {
        return errors;
    }

    @Override
    public String toJson() {
        String errorsJson = errors.stream()
                .map(e -> "\"" + e + "\"")
                .collect(java.util.stream.Collectors.joining(", "));

        return String.format(
                "{\"error\": \"%s\", \"message\": \"%s\", " +
                        "\"statusCode\": %d, \"errors\": [%s]}",
                getErrorCode(), getMessage(), getStatusCode(), errorsJson
        );
    }
}