package com.example.taxiservice.dto;

import com.example.taxiservice.enums.CarCategory;
import com.example.taxiservice.enums.OrderStatus;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * DTO для передачи информации о заказе
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class OrderDto {

    private Long id;

    private Long clientId;
    private String clientName;
    private String clientEmail;

    private Long driverId;
    private String driverName;
    private String driverEmail;

    private Long carId;
    private String carBrand;
    private String carModel;
    private String carLicensePlate;

    private Double pickupLatitude;
    private Double pickupLongitude;
    private Double dropoffLatitude;
    private Double dropoffLongitude;

    private Double distance;
    private Integer duration;
    private BigDecimal price;

    private CarCategory category;
    private OrderStatus status;

    private LocalDateTime createdAt;
    private LocalDateTime completedAt;

    private boolean hasPayment;
    private String paymentStatus;
}