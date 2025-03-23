package com.example.taxiservice.dto;


import lombok.Data;



import com.example.taxiservice.enums.CarCategory;
import com.example.taxiservice.enums.OrderStatus;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
public class OrderResponseDto {
    private Long id;

    // Информация о поездке
    private Double pickupLatitude;
    private Double pickupLongitude;
    private Double dropoffLatitude;
    private Double dropoffLongitude;
    private BigDecimal estimatedPrice;
    private Integer estimatedDuration; // в минутах
    private Double distance; // в километрах

    // Информация о машине
    private String carModel;
    private String carLicensePlate;
    private String carColor;
    private CarCategory carCategory;

    // Информация о водителе
    private String driverName;
    private String driverPhone;
    private Double driverRating;

    // Статус заказа
    private OrderStatus status;
    private LocalDateTime createdAt;
    private LocalDateTime estimatedArrivalTime;
}
