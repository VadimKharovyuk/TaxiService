package com.example.taxiservice.util;

import org.locationtech.jts.geom.Coordinate;
import org.locationtech.jts.geom.GeometryFactory;
import org.locationtech.jts.geom.Point;
import org.locationtech.jts.geom.PrecisionModel;
import org.springframework.stereotype.Component;

@Component
public class GeoUtils {

    // Фабрика для создания геометрических объектов
    // SRID 4326 соответствует WGS84 (широта и долгота)
    private final GeometryFactory geometryFactory = new GeometryFactory(new PrecisionModel(), 4326);

    /**
     * Создает объект Point из координат широты и долготы
     *
     * @param latitude широта
     * @param longitude долгота
     * @return объект Point
     */
    public Point createPoint(double latitude, double longitude) {
        // Важно: в JTS сначала идет долгота (X), потом широта (Y)
        return geometryFactory.createPoint(new Coordinate(longitude, latitude));
    }

    /**
     * Создает объект Point из объекта LocationPoint
     *
     * @param location объект LocationPoint
     * @return объект Point
     */
    public Point createPoint(LocationPoint location) {
        return createPoint(location.getLatitude(), location.getLongitude());
    }

    /**
     * Преобразует объект Point в LocationPoint
     *
     * @param point объект Point
     * @return объект LocationPoint
     */
    public LocationPoint toLocationPoint(Point point) {
        // В Point: X - долгота, Y - широта
        return new LocationPoint(point.getY(), point.getX());
    }

    /**
     * Вычисляет расстояние между двумя точками в километрах
     * Использует формулу гаверсинусов
     *
     * @param point1 первая точка
     * @param point2 вторая точка
     * @return расстояние в километрах
     */
    public double calculateDistance(Point point1, Point point2) {
        final int R = 6371; // Радиус Земли в километрах

        double lat1 = point1.getY();
        double lon1 = point1.getX();
        double lat2 = point2.getY();
        double lon2 = point2.getX();

        double latDistance = Math.toRadians(lat2 - lat1);
        double lonDistance = Math.toRadians(lon2 - lon1);

        double a = Math.sin(latDistance / 2) * Math.sin(latDistance / 2)
                + Math.cos(Math.toRadians(lat1)) * Math.cos(Math.toRadians(lat2))
                * Math.sin(lonDistance / 2) * Math.sin(lonDistance / 2);

        double c = 2 * Math.atan2(Math.sqrt(a), Math.sqrt(1 - a));

        return R * c;
    }

    /**
     * Вычисляет расстояние между двумя LocationPoint в километрах
     *
     * @param location1 первая точка
     * @param location2 вторая точка
     * @return расстояние в километрах
     */
    public double calculateDistance(LocationPoint location1, LocationPoint location2) {
        Point point1 = createPoint(location1);
        Point point2 = createPoint(location2);
        return calculateDistance(point1, point2);
    }

    /**
     * Вычисляет приблизительное время поездки в минутах
     *
     * @param distance расстояние в километрах
     * @param averageSpeed средняя скорость в км/ч
     * @return время в минутах
     */
    public int calculateDuration(double distance, double averageSpeed) {
        // Переводим время из часов в минуты
        return (int) Math.ceil((distance / averageSpeed) * 60);
    }
}