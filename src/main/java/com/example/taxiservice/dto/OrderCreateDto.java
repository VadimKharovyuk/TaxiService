package com.example.taxiservice.dto;

import com.example.taxiservice.enums.CarCategory;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * DTO для создания нового заказа
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class OrderCreateDto {

    @NotNull(message = "Широта точки отправления обязательна")
    @Min(value = -90, message = "Широта должна быть не менее -90")
    @Max(value = 90, message = "Широта должна быть не более 90")
    private Double pickupLatitude;

    @NotNull(message = "Долгота точки отправления обязательна")
    @Min(value = -180, message = "Долгота должна быть не менее -180")
    @Max(value = 180, message = "Долгота должна быть не более 180")
    private Double pickupLongitude;

    @NotNull(message = "Широта точки назначения обязательна")
    @Min(value = -90, message = "Широта должна быть не менее -90")
    @Max(value = 90, message = "Широта должна быть не более 90")
    private Double dropoffLatitude;

    @NotNull(message = "Долгота точки назначения обязательна")
    @Min(value = -180, message = "Долгота должна быть не менее -180")
    @Max(value = 180, message = "Долгота должна быть не более 180")
    private Double dropoffLongitude;

    private CarCategory category = CarCategory.STANDARD; // По умолчанию стандартный класс

    // Дополнительные поля, которые могут понадобиться
    private String comment; // Комментарий к заказу
    private Boolean needChildSeat = false; // Нужно ли детское кресло
    private Integer passengers = 1; // Количество пассажиров
}
