package com.example.taxiservice.maper;

import com.example.taxiservice.dto.CarDto;
import com.example.taxiservice.entity.Car;
import com.example.taxiservice.entity.CarBrand;
import com.example.taxiservice.entity.User;
import com.example.taxiservice.service.CarBrandService;
import com.example.taxiservice.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.stream.Collectors;

@Component
@RequiredArgsConstructor
public class CarMapper {

    private final CarBrandService carBrandService;
    private final UserService userService;

    /**
     * Преобразует сущность Car в DTO
     */
    public CarDto toDto(Car entity) {
        if (entity == null) {
            return null;
        }

        CarDto dto = new CarDto();
        dto.setId(entity.getId());

        if (entity.getBrand() != null) {
            dto.setBrandId(entity.getBrand().getId());
            dto.setBrandName(entity.getBrand().getName());
        }

        dto.setModel(entity.getModel());
        dto.setYear(entity.getYear());
        dto.setLicensePlate(entity.getLicensePlate());
        dto.setCategory(entity.getCategory());
        dto.setLatitude(entity.getLatitude());
        dto.setLongitude(entity.getLongitude());
        dto.setColor(entity.getColor());
        dto.setSeats(entity.getSeats());

        if (entity.getDriver() != null) {
            dto.setDriverId(entity.getDriver().getId());
        }

        dto.setIsAvailable(entity.isAvailable());

        return dto;
    }

    /**
     * Преобразует DTO в сущность Car
     */
    public Car toEntity(CarDto dto) {
        if (dto == null) {
            return null;
        }

        Car entity = new Car();
        entity.setId(dto.getId());

        if (dto.getBrandId() != null) {
            CarBrand brand = carBrandService.findById(dto.getBrandId());
            entity.setBrand(brand);
        }

        entity.setModel(dto.getModel());
        entity.setYear(dto.getYear());
        entity.setLicensePlate(dto.getLicensePlate());
        entity.setCategory(dto.getCategory());
        entity.setLatitude(dto.getLatitude());
        entity.setLongitude(dto.getLongitude());
        entity.setColor(dto.getColor());
        entity.setSeats(dto.getSeats());

        if (dto.getDriverId() != null) {
            User driver = userService.findById(dto.getDriverId());
            entity.setDriver(driver);
        }

        entity.setAvailable(dto.getIsAvailable());

        return entity;
    }

    /**
     * Обновляет существующую сущность данными из DTO
     */
    public void updateEntityFromDto(CarDto dto, Car entity) {
        if (dto == null || entity == null) {
            return;
        }

        if (dto.getBrandId() != null) {
            CarBrand brand = carBrandService.findById(dto.getBrandId());
            entity.setBrand(brand);
        }

        entity.setModel(dto.getModel());
        entity.setYear(dto.getYear());
        entity.setLicensePlate(dto.getLicensePlate());
        entity.setCategory(dto.getCategory());
        entity.setLatitude(dto.getLatitude());
        entity.setLongitude(dto.getLongitude());
        entity.setColor(dto.getColor());
        entity.setSeats(dto.getSeats());
        entity.setAvailable(dto.getIsAvailable());
    }

    /**
     * Преобразует список сущностей в список DTO
     */
    public List<CarDto> toDtoList(List<Car> entities) {
        if (entities == null) {
            return null;
        }

        return entities.stream()
                .map(this::toDto)
                .collect(Collectors.toList());
    }
}