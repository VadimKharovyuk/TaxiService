package com.example.taxiservice.dto;

import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
class OrderCreateDto {

    @NotNull(message = "Координаты отправления обязательны")
    private LocationDto pickup;

    @NotNull(message = "Координаты назначения обязательны")
    private LocationDto dropoff;
//
//    private Long carId; // ID автомобиля (опционально, если клиент выбирает конкретный автомобиль)
//
//    private String category; // Категория автомобиля (опционально)
}
