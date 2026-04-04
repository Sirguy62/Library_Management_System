package com.hospital.auth;

import org.mindrot.jbcrypt.BCrypt;

public class PasswordUtil {

    private PasswordUtil() {}

    public static String hash(String plainPassword) {
        return BCrypt.hashpw(plainPassword, BCrypt.gensalt(12));
    }

    public static boolean verify(String plainPassword, String hashedPassword) {
        return BCrypt.checkpw(plainPassword, hashedPassword);
    }

    public static void validate(String password) {
        if (password == null || password.length() < 8) {
            throw new com.hospital.exception.ValidationException(
                    "Password must be at least 8 characters");
        }
        if (!password.matches(".*[A-Z].*")) {
            throw new com.hospital.exception.ValidationException(
                    "Password must contain at least one uppercase letter");
        }
        if (!password.matches(".*[0-9].*")) {
            throw new com.hospital.exception.ValidationException(
                    "Password must contain at least one number");
        }
    }
}