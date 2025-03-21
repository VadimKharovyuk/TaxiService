package com.example.taxiservice.repository;

import com.example.taxiservice.entity.Car;
import com.example.taxiservice.entity.CarBrand;
import com.example.taxiservice.entity.User;
import com.example.taxiservice.enums.CarCategory;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CarRepository extends JpaRepository<Car, Long> {
    List<Car> findByDriver(User driver);
    List<Car> findByIsAvailableAndCategory(boolean isAvailable, CarCategory category);
    List<Car> findByBrand(CarBrand brand);
    List<Car> findByBrandAndIsAvailable(CarBrand brand, boolean isAvailable);
    boolean existsByLicensePlate(String licensePlate);

    /**
     * Находит доступные автомобили в указанном радиусе от точки
     * Использует простой SQL для расчета расстояния между двумя точками
     */
    @Query("SELECT c FROM Car c WHERE c.isAvailable = true " +
            "AND (6371 * acos(cos(radians(:latitude)) * cos(radians(c.latitude)) * " +
            "cos(radians(c.longitude) - radians(:longitude)) + sin(radians(:latitude)) * " +
            "sin(radians(c.latitude)))) <= :radius")
    List<Car> findAvailableCarsNearby(@Param("latitude") Double latitude,
                                      @Param("longitude") Double longitude,
                                      @Param("radius") Double radius);

    /**
     * Находит доступные автомобили указанной категории в радиусе от точки
     */
    @Query("SELECT c FROM Car c WHERE c.isAvailable = true AND c.category = :category " +
            "AND (6371 * acos(cos(radians(:latitude)) * cos(radians(c.latitude)) * " +
            "cos(radians(c.longitude) - radians(:longitude)) + sin(radians(:latitude)) * " +
            "sin(radians(c.latitude)))) <= :radius")
    List<Car> findAvailableCarsNearbyByCategory(@Param("latitude") Double latitude,
                                                @Param("longitude") Double longitude,
                                                @Param("radius") Double radius,
                                                @Param("category") CarCategory category);
}