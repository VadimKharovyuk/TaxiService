package com.example.taxiservice.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum CarCategory {
    STANDARD("standard"),
    BUSINESS("business"),
    LUXURY("luxury"),;

    private final String description;
}
