package com.example.taxiservice.dto;

import com.example.taxiservice.util.LocationPoint;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * DTO для передачи координат в запросах API
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class LocationDto {

    @NotNull(message = "Широта обязательна")
    @Min(value = -90, message = "Широта должна быть не менее -90")
    @Max(value = 90, message = "Широта должна быть не более 90")
    private Double latitude;

    @NotNull(message = "Долгота обязательна")
    @Min(value = -180, message = "Долгота должна быть не менее -180")
    @Max(value = 180, message = "Долгота должна быть не более 180")
    private Double longitude;

    /**
     * Конвертирует DTO в объект LocationPoint
     */
    public LocationPoint toLocationPoint() {
        return new LocationPoint(latitude, longitude);
    }

    /**
     * Создает LocationDto из объекта LocationPoint
     */
    public static LocationDto fromLocationPoint(LocationPoint point) {
        return new LocationDto(point.getLatitude(), point.getLongitude());
    }
}
