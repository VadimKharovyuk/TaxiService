package com.example.taxiservice.repository;

import com.example.taxiservice.entity.CarBrand;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface CarBrandRepository extends JpaRepository<CarBrand, Long> {
    Optional<CarBrand> findByName(String name);
    boolean existsByName(String name);
}
