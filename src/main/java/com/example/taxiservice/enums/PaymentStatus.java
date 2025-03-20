package com.example.taxiservice.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum PaymentStatus {
    PENDING("PENDING"),
    SUCCESS("SUCCESS"),
    FAILED("FAILED");

    private final String description;
}
