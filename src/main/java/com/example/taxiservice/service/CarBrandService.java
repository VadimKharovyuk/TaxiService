package com.example.taxiservice.service;



import com.example.taxiservice.entity.CarBrand;
import com.example.taxiservice.exception.ResourceNotFoundException;
import com.example.taxiservice.repository.CarBrandRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class CarBrandService {

    private final CarBrandRepository carBrandRepository;

    public List<CarBrand> findAll() {
        return carBrandRepository.findAll();
    }

    public CarBrand findById(Long id) {
        return carBrandRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Car brand not found with id: " + id));
    }

    public CarBrand findByName(String name) {
        return carBrandRepository.findByName(name)
                .orElseThrow(() -> new ResourceNotFoundException("Car brand not found with name: " + name));
    }

    @Transactional
    public CarBrand create(CarBrand carBrand) {
        if (carBrandRepository.existsByName(carBrand.getName())) {
            throw new IllegalArgumentException("Car brand with this name already exists");
        }

        return carBrandRepository.save(carBrand);
    }

    @Transactional
    public CarBrand update(CarBrand carBrand) {
        CarBrand existingBrand = findById(carBrand.getId());

        // Проверяем, не занято ли новое имя, если оно изменилось
        if (!existingBrand.getName().equals(carBrand.getName()) &&
                carBrandRepository.existsByName(carBrand.getName())) {
            throw new IllegalArgumentException("Car brand with this name already exists");
        }

        existingBrand.setName(carBrand.getName());
        existingBrand.setLogoUrl(carBrand.getLogoUrl());
        existingBrand.setDescription(carBrand.getDescription());

        return carBrandRepository.save(existingBrand);
    }

    @Transactional
    public void delete(Long id) {
        CarBrand carBrand = findById(id);

        // Проверяем, не привязаны ли к этому бренду машины
        if (!carBrand.getCars().isEmpty()) {
            throw new IllegalStateException("Cannot delete brand that has cars associated with it");
        }

        carBrandRepository.delete(carBrand);
    }
}