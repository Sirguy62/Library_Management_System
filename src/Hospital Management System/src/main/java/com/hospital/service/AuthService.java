package com.hospital.service;

import com.hospital.auth.JwtUtil;
import com.hospital.auth.PasswordUtil;
import com.hospital.auth.RateLimiter;
import com.hospital.enums.AuditEventType;
import com.hospital.enums.UserRole;
import com.hospital.exception.*;
import com.hospital.factory.PersonFactory;
import com.hospital.model.*;
import com.hospital.model.base.Person;
import com.hospital.store.*;
import com.hospital.util.Logger;
import com.hospital.util.Result;

import java.util.HashMap;
import java.util.Map;

public class AuthService {

    private final UserStore         userStore;
    private final TokenStore        tokenStore;
    private final VerificationStore verificationStore;
    private final AuditStore        auditStore;

    public AuthService(UserStore userStore,
                       TokenStore tokenStore,
                       VerificationStore verificationStore,
                       AuditStore auditStore) {
        this.userStore         = userStore;
        this.tokenStore        = tokenStore;
        this.verificationStore = verificationStore;
        this.auditStore        = auditStore;
    }


    public Person register(String email,
                           String username,
                           String password,
                           UserRole role,
                           Map<String, Object> extra,
                           String ipAddress) {
        validateRegistrationInput(email, username, password);

        if (userStore.existsByEmail(email)) {
            throw ConflictException.emailTaken();
        }

        PasswordUtil.validate(password);

        String passwordHash = PasswordUtil.hash(password);

        Person person = PersonFactory.create(
                role, email, username, passwordHash, extra);

        userStore.save(person);

        VerificationToken verToken = new VerificationToken(person.getId());
        verificationStore.save(verToken);

        EmailService.sendVerificationEmail(person, verToken.getToken());

        auditStore.log(new AuditLog(
                person.getId(),
                AuditEventType.REGISTER,
                "New " + role.name() + " registered",
                ipAddress
        ));

        return person;
    }

    public void verifyEmail(String token, String ipAddress) {
        VerificationToken verToken = verificationStore
                .findByToken(token)
                .orElseThrow(() -> new AuthException(
                        "INVALID_TOKEN", "Verification token not found"));

        if (!verToken.isValid()) {
            if (verToken.isUsed()) {
                throw ConflictException.alreadyVerified();
            }
            throw TokenExpiredException.verificationToken();
        }

        Person person = userStore.findById(verToken.getUserId())
                .orElseThrow(() -> NotFoundException.user(verToken.getUserId()));
        person.markVerified();
        userStore.updateSecurityFields(person);

        verificationStore.markUsed(token);

        auditStore.log(new AuditLog(
                person.getId(),
                AuditEventType.EMAIL_VERIFIED,
                "Email verified successfully",
                ipAddress
        ));
    }


    public Result<LoginResult> login(String email,
                                     String password,
                                     String ipAddress) {
        // 1. Check rate limit
        RateLimiter.check(email);

        // 2. Find user
        Person person = userStore.findByEmail(email).orElse(null);

        if (person == null) {
            RateLimiter.recordFailure(email);
            Map<String, Object> ctx = new HashMap<>();
            ctx.put("email", email);
            ctx.put("ip",    ipAddress);
            Logger.warn("LOGIN_FAILED",
                    "Login failed — email not found", ctx);
            throw AuthException.invalidCredentials();
        }

        // 3. Check account not locked
        if (person.isLocked()) {
            Map<String, Object> ctx = new HashMap<>();
            ctx.put("userId",      person.getId());
            ctx.put("lockedUntil", person.getLockedUntil());
            ctx.put("ip",          ipAddress);
            Logger.warn("ACCOUNT_LOCKED",
                    "Login blocked — account locked", ctx);
            throw new AccountLockedException(person.getLockedUntil());
        }

        // 4. Check email verified
        if (!person.isVerified()) {
            throw AuthException.emailNotVerified();
        }

        // 5. Verify password
        if (!PasswordUtil.verify(password, person.getPasswordHash())) {
            handleFailedAttempt(person, ipAddress);
            throw AuthException.invalidCredentials();
        }

        // 6. Success — clear attempts
        person.resetFailedAttempts();
        userStore.updateSecurityFields(person);
        RateLimiter.clearAttempts(email);

        // 7. Generate tokens
        String       accessToken  = JwtUtil.generateAccessToken(
                person.getId(), person.getRole());
        RefreshToken refreshToken = new RefreshToken(person.getId());
        tokenStore.saveRefreshToken(refreshToken);

        // 8. Log success
        Map<String, Object> loginCtx = new HashMap<>();
        loginCtx.put("email", person.getEmail());
        loginCtx.put("role",  person.getRole().name());
        loginCtx.put("ip",    ipAddress);
        Logger.business("LOGIN_SUCCESS", person.getId(),
                "User logged in successfully", loginCtx);

        return Result.success(new LoginResult(
                accessToken,
                refreshToken.getToken(),
                person
        ));
    }


    public Result<String> refresh(String refreshTokenStr,
                                  String ipAddress) {
        RefreshToken refreshToken = tokenStore
                .findRefreshToken(refreshTokenStr)
                .orElseThrow(() -> new AuthException(
                        "INVALID_REFRESH_TOKEN",
                        "Refresh token not found"));

        if (!refreshToken.isValid()) {
            if (refreshToken.isRevoked()) {
                tokenStore.revokeAllUserTokens(refreshToken.getUserId());
                throw new AuthException("TOKEN_REUSE_DETECTED",
                        "Token reuse detected — all sessions revoked");
            }
            throw TokenExpiredException.refreshToken();
        }

        Person person = userStore.findById(refreshToken.getUserId())
                .orElseThrow(() -> NotFoundException
                        .user(refreshToken.getUserId()));

        tokenStore.revokeRefreshToken(refreshTokenStr);
        RefreshToken newRefreshToken = new RefreshToken(person.getId());
        tokenStore.saveRefreshToken(newRefreshToken);

        String newAccessToken = JwtUtil.generateAccessToken(
                person.getId(), person.getRole());

        // 6. Log
        auditStore.log(new AuditLog(
                person.getId(),
                AuditEventType.TOKEN_REFRESHED,
                "Access token refreshed",
                ipAddress
        ));

        return Result.success(newAccessToken);
    }


    public void logout(String accessToken,
                       String refreshToken,
                       String userId,
                       String ipAddress) {
        try {
            String jwtId   = JwtUtil.extractJwtId(accessToken);
            String expiry  = JwtUtil.extractExpiry(accessToken);
            tokenStore.blacklistToken(
                    new BlacklistedToken(jwtId, expiry));
        } catch (Exception ignored) {

        }

        if (refreshToken != null) {
            tokenStore.revokeRefreshToken(refreshToken);
        }

        auditStore.log(new AuditLog(
                userId,
                AuditEventType.LOGOUT,
                "User logged out",
                ipAddress
        ));
    }

    public void logoutAll(String accessToken,
                          String userId,
                          String ipAddress) {
        try {
            String jwtId  = JwtUtil.extractJwtId(accessToken);
            String expiry = JwtUtil.extractExpiry(accessToken);
            tokenStore.blacklistToken(
                    new BlacklistedToken(jwtId, expiry));
        } catch (Exception ignored) {}

        tokenStore.revokeAllUserTokens(userId);

        // 3. Log
        auditStore.log(new AuditLog(
                userId,
                AuditEventType.LOGOUT_ALL,
                "User logged out from all devices",
                ipAddress
        ));
    }


    private void validateRegistrationInput(String email,
                                           String username,
                                           String password) {
        java.util.List<String> errors = new java.util.ArrayList<>();

        if (email == null || email.isBlank()) {
            errors.add("Email is required");
        } else if (!email.matches("^[^@]+@[^@]+\\.[^@]+$")) {
            errors.add("Invalid email format");
        }

        if (username == null || username.isBlank()) {
            errors.add("Username is required");
        } else if (username.length() < 2) {
            errors.add("Username must be at least 2 characters");
        }

        if (password == null || password.isBlank()) {
            errors.add("Password is required");
        }

        if (!errors.isEmpty()) {
            throw new ValidationException(errors);
        }
    }

    private void handleFailedAttempt(Person person, String ipAddress) {
        person.incrementFailedAttempts();

        int maxAttempts = 5;
        if (person.getFailedAttempts() >= maxAttempts) {
            person.lockAccount(15); // lock for 15 minutes
            userStore.updateSecurityFields(person);

            EmailService.sendAccountLockedEmail(
                    person, person.getLockedUntil());

            auditStore.log(new AuditLog(
                    person.getId(),
                    AuditEventType.ACCOUNT_LOCKED,
                    "Account locked after " + maxAttempts
                            + " failed attempts",
                    ipAddress
            ));

            throw new AccountLockedException(person.getLockedUntil());
        }

        userStore.updateSecurityFields(person);

        auditStore.log(new AuditLog(
                person.getId(),
                AuditEventType.LOGIN_FAILED,
                "Failed login attempt "
                        + person.getFailedAttempts()
                        + " of " + maxAttempts,
                ipAddress
        ));
    }


    public static class LoginResult {
        private final String accessToken;
        private final String refreshToken;
        private final Person person;

        public LoginResult(String accessToken,
                           String refreshToken,
                           Person person) {
            this.accessToken  = accessToken;
            this.refreshToken = refreshToken;
            this.person       = person;
        }

        public String getAccessToken()  { return accessToken; }
        public String getRefreshToken() { return refreshToken; }
        public Person getPerson()       { return person; }
    }
}