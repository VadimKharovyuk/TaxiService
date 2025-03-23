package com.example.taxiservice.dto;

import com.example.taxiservice.enums.CarCategory;
import lombok.Data;

import java.math.BigDecimal;

@Data
public class OrderRequestDto {
    private Double pickupLatitude;
    private Double pickupLongitude;
    private Double dropoffLatitude;
    private Double dropoffLongitude;
    private CarCategory category;
    private BigDecimal price;
    private Double distance;
    private Integer duration;
}