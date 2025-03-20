package com.example.taxiservice.entity;

import com.example.taxiservice.enums.CarCategory;
import com.example.taxiservice.util.GeoUtils;
import com.example.taxiservice.util.LocationPoint;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.locationtech.jts.geom.Point;

import java.util.List;

@Entity
@Table(name = "cars")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Car {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "brand_id", nullable = false)
    private CarBrand brand;

    @Column(nullable = false)
    private String model;

    @Column(nullable = false)
    private int year;

    @Column(nullable = false, unique = true)
    private String licensePlate;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private CarCategory category;

    @Column(nullable = false)
    private boolean isAvailable = true;

    private Double latitude;
    private Double longitude;

    private String color;

    private Integer seats;

    @ManyToOne
    @JoinColumn(name = "driver_id", nullable = false)
    private User driver;

    @OneToMany(mappedBy = "car", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<Order> orders;


}