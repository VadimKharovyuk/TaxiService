package com.example.taxiservice.service;

import com.example.taxiservice.entity.Car;
import com.example.taxiservice.enums.CarCategory;
import com.example.taxiservice.repository.CarRepository;
import com.example.taxiservice.util.GeoUtils;
import com.example.taxiservice.util.LocationPoint;
import lombok.RequiredArgsConstructor;
import org.locationtech.jts.geom.Point;
import org.springframework.stereotype.Service;

import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class LocationService {

    private final CarRepository carRepository;
    private final GeoUtils geoUtils;
    private final HereRouteService hereRouteService;

    /**
     * Находит ближайшие доступные автомобили в указанном радиусе
     * Использует Java для вычисления расстояния в памяти, а не в базе данных
     *
     * @param location точка, относительно которой ищем
     * @param radius радиус поиска в километрах
     * @param category категория автомобиля (опционально)
     * @return список ближайших автомобилей, отсортированный по расстоянию
     */
    public List<Car> findNearestAvailableCars(LocationPoint location, double radius, CarCategory category) {
        // Получаем список всех доступных автомобилей
        List<Car> availableCars = carRepository.findAllAvailableCars();

        // Фильтруем автомобили по расстоянию и категории в памяти
        List<Car> carsNearby = availableCars.stream()
                .filter(car -> car.getLatitude() != null && car.getLongitude() != null)
                .filter(car -> calculateDistance(car, location) <= radius)
                .filter(car -> category == null || car.getCategory() == category)
                .sorted(Comparator.comparingDouble(car -> calculateDistance(car, location)))
                .collect(Collectors.toList());

        return carsNearby;
    }

    // Вспомогательный метод для расчета расстояния между автомобилем и точкой
    private double calculateDistance(Car car, LocationPoint location) {
        LocationPoint carLocation = new LocationPoint(car.getLatitude(), car.getLongitude());
        return carLocation.distanceTo(location);
    }

    /**
     * Находит ближайшие доступные автомобили в указанном радиусе
     *
     * @param location точка, относительно которой ищем
     * @param radius радиус поиска в километрах
     * @return список ближайших автомобилей, отсортированный по расстоянию
     */
    public List<Car> findNearestAvailableCars(LocationPoint location, double radius) {
        return findNearestAvailableCars(location, radius, null);
    }



    /**
     * Рассчитывает примерное время поездки
     *
     * @param distance расстояние в километрах
     * @param category категория автомобиля (влияет на среднюю скорость)
     * @return время в минутах
     */
    public int estimateTripDuration(double distance, CarCategory category) {
        double averageSpeed = getAverageSpeedForCategory(category);
        return geoUtils.calculateDuration(distance, averageSpeed);
    }

    /**
     * Возвращает среднюю скорость для категории автомобиля
     *
     * @param category категория автомобиля
     * @return средняя скорость в км/ч
     */
    private double getAverageSpeedForCategory(CarCategory category) {
        // Примерные значения средней скорости для разных категорий автомобилей
        if (category == null) {
            return 30.0; // По умолчанию
        }

        switch (category) {
            case STANDARD:
                return 30.0;
            case BUSINESS:
                return 35.0;
            case LUXURY:
                return 40.0;
            default:
                return 30.0;
        }
    }

    /**
     * Рассчитывает маршрут между двумя точками с использованием HERE API
     */
    public double calculateRouteDistance(LocationPoint pickup, LocationPoint dropoff) {
        HereRouteService.RouteResult result = hereRouteService.calculateRoute(
                pickup.getLatitude(), pickup.getLongitude(),
                dropoff.getLatitude(), dropoff.getLongitude()
        );

        return result.getDistance();
    }

    /**
     * Рассчитывает примерное время поездки с использованием HERE API
     */
    public int estimateTripDuration(LocationPoint pickup, LocationPoint dropoff, CarCategory category) {
        HereRouteService.RouteResult result = hereRouteService.calculateRoute(
                pickup.getLatitude(), pickup.getLongitude(),
                dropoff.getLatitude(), dropoff.getLongitude()
        );

        return result.getDuration();
    }
}