package com.hospital.auth;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.exceptions.JWTVerificationException;
import com.auth0.jwt.interfaces.DecodedJWT;
import com.hospital.enums.UserRole;
import com.hospital.exception.AuthException;
import com.hospital.exception.TokenExpiredException;

import java.security.KeyPair;
import java.security.KeyPairGenerator;
import java.security.interfaces.RSAPrivateKey;
import java.security.interfaces.RSAPublicKey;
import java.time.Instant;
import java.util.Date;
import java.util.UUID;

public class JwtUtil {

    private static RSAPrivateKey privateKey;
    private static RSAPublicKey  publicKey;
    private static Algorithm     algorithm;

    private static final int ACCESS_TOKEN_MINUTES = 15;
    private static final String ISSUER = "hospital-api";

    private JwtUtil() {}


    public static void initialize() {
        try {
            KeyPairGenerator generator = KeyPairGenerator.getInstance("RSA");
            generator.initialize(2048); // 2048-bit RSA key
            KeyPair keyPair = generator.generateKeyPair();

            privateKey = (RSAPrivateKey) keyPair.getPrivate();
            publicKey  = (RSAPublicKey)  keyPair.getPublic();
            algorithm  = Algorithm.RSA256(publicKey, privateKey);

            System.out.println("RSA key pair generated successfully");
        } catch (Exception e) {
            throw new RuntimeException("Failed to generate RSA keys: "
                    + e.getMessage(), e);
        }
    }


    public static String generateAccessToken(String userId, UserRole role) {
        Instant now    = Instant.now();
        Instant expiry = now.plusSeconds(ACCESS_TOKEN_MINUTES * 60L);

        return JWT.create()
                .withIssuer(ISSUER)
                .withSubject(userId)
                .withClaim("userId", userId)
                .withClaim("role",   role.name())
                .withJWTId(UUID.randomUUID().toString()) // unique token ID
                .withIssuedAt(Date.from(now))
                .withExpiresAt(Date.from(expiry))
                .sign(algorithm);
    }


    public static DecodedJWT verify(String token) {
        try {
            return JWT.require(algorithm)
                    .withIssuer(ISSUER)
                    .build()
                    .verify(token);

        } catch (com.auth0.jwt.exceptions.TokenExpiredException e) {
            throw TokenExpiredException.accessToken();

        } catch (JWTVerificationException e) {
            throw AuthException.unauthorized();
        }
    }

    public static String extractUserId(String token) {
        return verify(token).getClaim("userId").asString();
    }

    public static UserRole extractRole(String token) {
        String role = verify(token).getClaim("role").asString();
        return UserRole.fromString(role);
    }

    public static String extractJwtId(String token) {
        return verify(token).getId();
    }

    public static String extractExpiry(String token) {
        return verify(token).getExpiresAt().toInstant().toString();
    }


    public static DecodedJWT decode(String token) {
        return JWT.decode(token);
    }

    public static long getSecondsUntilExpiry(String token) {
        Instant expiry = verify(token).getExpiresAt().toInstant();
        Instant now    = Instant.now();
        long    secs   = expiry.getEpochSecond() - now.getEpochSecond();
        return Math.max(0, secs);
    }
}