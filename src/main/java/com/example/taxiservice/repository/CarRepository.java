package com.example.taxiservice.repository;

import com.example.taxiservice.entity.Car;
import com.example.taxiservice.entity.CarBrand;
import com.example.taxiservice.entity.User;
import com.example.taxiservice.enums.CarCategory;
import org.locationtech.jts.geom.Point;
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

    /**
     * Находит доступные автомобили в указанном радиусе от точки
     * Использует нативный SQL запрос с пространственными функциями PostGIS
     *
     * @param point центральная точка поиска
     * @param radius радиус поиска в метрах
     * @return список доступных автомобилей в указанном радиусе
     */
    @Query(value = "SELECT * FROM cars c WHERE c.is_available = true AND ST_DWithin(c.location, ?1, ?2)", nativeQuery = true)
    List<Car> findAvailableCarsNearby(Point point, double radius);

    /**
     * Альтернативный метод, использующий простое JPQL выражение вместо пространственной функции
     * Может использоваться как временное решение, пока не настроена полная поддержка PostGIS
     */
    @Query("SELECT c FROM Car c WHERE c.isAvailable = true")
    List<Car> findAllAvailableCars();
}