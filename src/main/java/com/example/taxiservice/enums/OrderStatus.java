package com.example.taxiservice.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public enum OrderStatus {
    PENDING("В ожидании", "pending"),
    ACCEPTED("Принят", "accepted"),
    DRIVER_ASSIGNED("Водитель назначен", "driver-assigned"),
    ON_THE_WAY("В пути", "on-the-way"),
    ARRIVED("Прибыл", "arrived"),
    IN_PROGRESS("В процессе", "in-progress"),
    COMPLETED("Завершен", "completed"),
    CANCELED("Отменен", "cancelled");

    private final String displayName;
    private final String description;
}
