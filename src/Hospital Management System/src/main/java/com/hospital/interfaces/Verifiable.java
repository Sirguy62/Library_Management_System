package com.hospital.interfaces;

public interface Verifiable {

    boolean isVerified();

    void markVerified();

    String getVerificationEmail();
}