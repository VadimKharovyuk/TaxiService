//package com.example.taxiservice.service;
//
//import com.example.taxiservice.entity.Car;
//import com.example.taxiservice.enums.CarCategory;
//import com.example.taxiservice.repository.CarRepository;
//import com.example.taxiservice.util.GeoUtils;
//import com.example.taxiservice.util.LocationPoint;
//import lombok.RequiredArgsConstructor;
//import org.locationtech.jts.geom.Point;
//import org.springframework.stereotype.Service;
//
//import java.util.Comparator;
//import java.util.List;
//import java.util.stream.Collectors;
//
//@Service
//@RequiredArgsConstructor
//public class LocationService {
//
//    private final CarRepository carRepository;
//    private final GeoUtils geoUtils;
//
//    /**
//     * Находит ближайшие доступные автомобили в указанном радиусе
//     * Использует Java для вычисления расстояния в памяти, а не в базе данных
//     *
//     * @param location точка, относительно которой ищем
//     * @param radius радиус поиска в километрах
//     * @param category категория автомобиля (опционально)
//     * @return список ближайших автомобилей, отсортированный по расстоянию
//     */
//    public List<Car> findNearestAvailableCars(LocationPoint location, double radius, CarCategory category) {
//        // Получаем список всех доступных автомобилей
//        List<Car> availableCars = carRepository.findAllAvailableCars();
//
//        Point searchPoint = geoUtils.createPoint(location);
//
//        // Фильтруем автомобили по расстоянию и категории в памяти
//        List<Car> carsNearby = availableCars.stream()
//                .filter(car -> car.getLocation() != null)
//                .filter(car -> geoUtils.calculateDistance(car.getLocation(), searchPoint) <= radius)
//                .filter(car -> category == null || car.getCategory() == category)
//                .sorted(Comparator.comparingDouble(car -> geoUtils.calculateDistance(car.getLocation(), searchPoint)))
//                .collect(Collectors.toList());
//
//        return carsNearby;
//    }
//
//    /**
//     * Находит ближайшие доступные автомобили в указанном радиусе
//     *
//     * @param location точка, относительно которой ищем
//     * @param radius радиус поиска в километрах
//     * @return список ближайших автомобилей, отсортированный по расстоянию
//     */
//    public List<Car> findNearestAvailableCars(LocationPoint location, double radius) {
//        return findNearestAvailableCars(location, radius, null);
//    }
//
//    /**
//     * Рассчитывает маршрут между двумя точками
//     * В простейшем случае используем прямое расстояние
//     *
//     * @param pickup точка отправления
//     * @param dropoff точка назначения
//     * @return расстояние в километрах
//     */
//    public double calculateRouteDistance(LocationPoint pickup, LocationPoint dropoff) {
//        return geoUtils.calculateDistance(pickup, dropoff);
//    }
//
//    /**
//     * Рассчитывает примерное время поездки
//     *
//     * @param distance расстояние в километрах
//     * @param category категория автомобиля (влияет на среднюю скорость)
//     * @return время в минутах
//     */
//    public int estimateTripDuration(double distance, CarCategory category) {
//        double averageSpeed = getAverageSpeedForCategory(category);
//        return geoUtils.calculateDuration(distance, averageSpeed);
//    }
//
//    /**
//     * Возвращает среднюю скорость для категории автомобиля
//     *
//     * @param category категория автомобиля
//     * @return средняя скорость в км/ч
//     */
//    private double getAverageSpeedForCategory(CarCategory category) {
//        // Примерные значения средней скорости для разных категорий автомобилей
//        if (category == null) {
//            return 30.0; // По умолчанию
//        }
//
//        switch (category) {
//            case STANDARD:
//                return 30.0;
//            case BUSINESS:
//                return 35.0;
//            case LUXURY:
//                return 40.0;
//            default:
//                return 30.0;
//        }
//    }
//}