package com.hospital.util;

import java.util.Optional;
import java.util.function.Function;

public class Result<T> {

    private final T       value;
    private final String  errorMessage;
    private final boolean success;

    private Result(T value) {
        this.value        = value;
        this.errorMessage = null;
        this.success      = true;
    }

    private Result(String errorMessage) {
        this.value        = null;
        this.errorMessage = errorMessage;
        this.success      = false;
    }

    public static <T> Result<T> success(T value) {
        return new Result<>(value);
    }

    public static <T> Result<T> failure(String errorMessage) {
        return new Result<>(errorMessage);
    }

    public static <T> Result<T> empty() {
        return new Result<>((T) null) {
            @Override public boolean isEmpty() { return true; }
        };
    }

    public boolean isSuccess()  { return success; }
    public boolean isFailure()  { return !success; }
    public boolean isEmpty()    { return value == null && success; }

    public T getValue() {
        if (!success) throw new IllegalStateException("Result is failure: " + errorMessage);
        return value;
    }

    public String getError() {
        if (success) throw new IllegalStateException("Result is success");
        return errorMessage;
    }

    public Optional<T> toOptional() {
        return Optional.ofNullable(value);
    }


    public <U> Result<U> map(Function<T, U> mapper) {
        if (success && value != null) {
            return Result.success(mapper.apply(value));
        }
        return Result.failure(errorMessage);
    }

    @Override
    public String toString() {
        return success
                ? "Result.success(" + value + ")"
                : "Result.failure(" + errorMessage + ")";
    }
}