package com.hospital.auth;

import com.hospital.cache.CacheService;
import com.hospital.exception.AccountLockedException;
import com.hospital.exception.RateLimitException;

public class RateLimiter {

    private static final int MAX_ATTEMPTS    = 5;
    private static final int LOCKOUT_SECONDS = 900; // 15 minutes

    private RateLimiter() {}


    public static void check(String email) {
        String key     = CacheService.keyRateLimit(email);
        String current = CacheService.get(key);

        if (current == null) return;

        int attempts = Integer.parseInt(current);

        if (attempts >= MAX_ATTEMPTS) {
            throw new RateLimitException(LOCKOUT_SECONDS);
        }
    }


    public static void recordFailure(String email) {
        String key      = CacheService.keyRateLimit(email);
        String current  = CacheService.get(key);

        if (current == null) {
            CacheService.set(key, "1", LOCKOUT_SECONDS);
        } else {
            int attempts = Integer.parseInt(current) + 1;
            CacheService.set(key, String.valueOf(attempts), LOCKOUT_SECONDS);

            if (attempts >= MAX_ATTEMPTS) {
                throw new AccountLockedException(
                        java.time.Instant.now()
                                .plusSeconds(LOCKOUT_SECONDS)
                                .toString()
                );
            }
        }
    }

    public static void clearAttempts(String email) {
        CacheService.delete(CacheService.keyRateLimit(email));
    }


    public static int getAttempts(String email) {
        String current = CacheService.get(CacheService.keyRateLimit(email));
        return current == null ? 0 : Integer.parseInt(current);
    }
}
