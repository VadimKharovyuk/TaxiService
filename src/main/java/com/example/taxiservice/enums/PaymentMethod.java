package com.example.taxiservice.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum PaymentMethod {
    CASH("Cash"),
    CARD("Card"),
    WALLET("WALLET");

    private final String description;
}