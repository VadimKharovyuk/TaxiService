package com.example.taxiservice.enums;


import lombok.AllArgsConstructor;
import lombok.Getter;


@AllArgsConstructor
@Getter
public enum Role {
    CLIENT("client"),
    DRIVER("driver"),;

    private final String description;


}