package com.example.taxiservice.service;

import com.example.taxiservice.dto.CarBrandDto;
import com.example.taxiservice.entity.CarBrand;
import com.example.taxiservice.exception.ResourceNotFoundException;

import com.example.taxiservice.maper.CarBrandMapper;
import com.example.taxiservice.repository.CarBrandRepository;
import jakarta.validation.constraints.NotNull;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class CarBrandService {

    private final CarBrandRepository carBrandRepository;
    private final CarBrandMapper carBrandMapper;

    /**
     * Находит все бренды автомобилей и преобразует их в DTO
     */
    public List<CarBrandDto> findAll() {
        List<CarBrand> brands = carBrandRepository.findAll();
        return carBrandMapper.toDtoList(brands);
    }

    /**
     * Находит бренд по ID и преобразует его в DTO
     */
    public CarBrandDto findDtoById(Long id) {
        CarBrand brand = findEntityById(id);
        return carBrandMapper.toDto(brand);
    }

    /**
     * Находит бренд по ID как сущность (для внутреннего использования)
     */
    public CarBrand findEntityById(Long id) {
        return carBrandRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Бренд автомобиля не найден с ID: " + id));
    }

    /**
     * Находит бренд по имени и преобразует его в DTO
     */
    public CarBrandDto findByName(String name) {
        CarBrand brand = carBrandRepository.findByName(name)
                .orElseThrow(() -> new ResourceNotFoundException("Бренд автомобиля не найден с именем: " + name));
        return carBrandMapper.toDto(brand);
    }

    /**
     * Создает новый бренд автомобиля из DTO
     */
    @Transactional
    public CarBrandDto create(CarBrandDto brandDto) {
        if (carBrandRepository.existsByName(brandDto.getName())) {
            throw new IllegalArgumentException("Бренд с таким именем уже существует");
        }

        CarBrand brand = carBrandMapper.toEntity(brandDto);
        CarBrand savedBrand = carBrandRepository.save(brand);
        return carBrandMapper.toDto(savedBrand);
    }

    /**
     * Обновляет существующий бренд автомобиля из DTO
     */
    @Transactional
    public CarBrandDto update(CarBrandDto brandDto) {
        if (brandDto.getId() == null) {
            throw new IllegalArgumentException("ID бренда должен быть указан для обновления");
        }

        CarBrand existingBrand = findEntityById(brandDto.getId());

        // Проверяем, не занято ли новое имя, если оно изменилось
        if (!existingBrand.getName().equals(brandDto.getName()) &&
                carBrandRepository.existsByName(brandDto.getName())) {
            throw new IllegalArgumentException("Бренд с таким именем уже существует");
        }

        carBrandMapper.updateEntityFromDto(brandDto, existingBrand);
        CarBrand updatedBrand = carBrandRepository.save(existingBrand);
        return carBrandMapper.toDto(updatedBrand);
    }

    /**
     * Удаляет бренд автомобиля по ID
     */
    @Transactional
    public void delete(Long id) {
        CarBrand carBrand = findEntityById(id);

        // Проверяем, не привязаны ли к этому бренду машины
        if (!carBrand.getCars().isEmpty()) {
            throw new IllegalStateException("Невозможно удалить бренд, к которому привязаны автомобили");
        }

        carBrandRepository.delete(carBrand);
    }

    /**
     * Проверяет существование бренда по ID
     */
    public boolean existsById(Long id) {
        return carBrandRepository.existsById(id);
    }

    /**
     * Проверяет существование бренда по имени
     */
    public boolean existsByName(String name) {
        return carBrandRepository.existsByName(name);
    }

    public CarBrand findById(@NotNull(message = "Бренд автомобиля должен быть указан") Long brandId) {
      return   carBrandRepository.findById(brandId).orElseThrow();
    }
}