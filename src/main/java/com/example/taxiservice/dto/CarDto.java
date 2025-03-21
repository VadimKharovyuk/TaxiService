package com.example.taxiservice.dto;

import com.example.taxiservice.enums.CarCategory;
import jakarta.validation.constraints.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class CarDto {

    private Long id;

    @NotNull(message = "Бренд автомобиля должен быть указан")
    private Long brandId;

    private String brandName; // Только для отображения

    @NotBlank(message = "Модель автомобиля не может быть пустой")
    @Size(min = 2, max = 50, message = "Модель должна содержать от 2 до 50 символов")
    private String model;

    @NotNull(message = "Год выпуска должен быть указан")
    @Min(value = 1950, message = "Год выпуска должен быть не ранее 1950")
    @Max(value = 2025, message = "Год выпуска должен быть не позднее текущего года")
    private Integer year;

    @NotBlank(message = "Номерной знак не может быть пустым")
    @Size(min = 3, max = 20, message = "Номерной знак должен содержать от 3 до 20 символов")
    private String licensePlate;

    @NotNull(message = "Категория автомобиля должна быть указана")
    private CarCategory category;

    private Double latitude;

    private Double longitude;

    @Size(max = 30, message = "Цвет не может быть длиннее 30 символов")
    private String color;

    @Min(value = 1, message = "Количество мест должно быть положительным числом")
    @Max(value = 50, message = "Количество мест не может превышать 50")
    private Integer seats;

    private Long driverId;

    private Boolean isAvailable = true;
}