package com.example.taxiservice.maper;

import com.example.taxiservice.dto.CarBrandDto;
import com.example.taxiservice.entity.CarBrand;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.stream.Collectors;

@Component
public class CarBrandMapper {

    /**
     * Преобразует сущность CarBrand в DTO
     */
    public CarBrandDto toDto(CarBrand entity) {
        if (entity == null) {
            return null;
        }

        CarBrandDto dto = new CarBrandDto();
        dto.setId(entity.getId());
        dto.setName(entity.getName());

        return dto;
    }

    /**
     * Преобразует DTO в сущность CarBrand
     */
    public CarBrand toEntity(CarBrandDto dto) {
        if (dto == null) {
            return null;
        }

        CarBrand entity = new CarBrand();
        entity.setId(dto.getId());
        entity.setName(dto.getName());
        return entity;
    }

    /**
     * Обновляет существующую сущность данными из DTO
     */
    public void updateEntityFromDto(CarBrandDto dto, CarBrand entity) {
        if (dto == null || entity == null) {
            return;
        }

        entity.setName(dto.getName());

    }

    /**
     * Преобразует список сущностей в список DTO
     */
    public List<CarBrandDto> toDtoList(List<CarBrand> entities) {
        if (entities == null) {
            return null;
        }

        return entities.stream()
                .map(this::toDto)
                .collect(Collectors.toList());
    }
}