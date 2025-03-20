package com.example.taxiservice.dto;

import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * DTO для запроса маршрута
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
class RouteRequestDto {

    @NotNull(message = "Координаты отправления обязательны")
    private LocationDto pickup;

    @NotNull(message = "Координаты назначения обязательны")
    private LocationDto dropoff;

}
