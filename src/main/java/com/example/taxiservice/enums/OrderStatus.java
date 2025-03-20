package com.example.taxiservice.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public enum OrderStatus {
    PENDING("pending"),
    ACCEPTED("accepted"),
    ON_THE_WAY("on-the-way"),
    COMPLETED("COMPLETED"),
    CANCELED("cancelled");

    private final String description;

}
