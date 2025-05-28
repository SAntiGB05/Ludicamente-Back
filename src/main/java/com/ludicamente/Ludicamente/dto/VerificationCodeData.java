package com.ludicamente.Ludicamente.dto;

import java.time.LocalDateTime;

public class VerificationCodeData {
    private final String code;
    private final LocalDateTime expirationTime;

    public VerificationCodeData(String code, LocalDateTime expirationTime) {
        this.code = code;
        this.expirationTime = expirationTime;
    }

    public String getCode() {
        return code;
    }

    public LocalDateTime getExpirationTime() {
        return expirationTime;
    }
}
