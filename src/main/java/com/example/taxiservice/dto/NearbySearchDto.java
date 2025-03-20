package com.example.taxiservice.dto;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * DTO для запроса поиска автомобилей поблизости
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
class NearbySearchDto {

    @NotNull(message = "Координаты обязательны")
    private LocationDto location;

    @NotNull(message = "Радиус поиска обязателен")
    @Min(value = 0, message = "Радиус должен быть положительным")
    @Max(value = 50, message = "Радиус не может превышать 50 км")
    private Double radius; // в километрах


}