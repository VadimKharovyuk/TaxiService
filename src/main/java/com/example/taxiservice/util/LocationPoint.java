package com.example.taxiservice.util;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.locationtech.jts.geom.Point;

import java.io.Serializable;

/**
 * Класс для представления географической точки с координатами широты и долготы.
 * Используется для передачи координат между клиентом и сервером.
 */

@Data
@NoArgsConstructor
@AllArgsConstructor
public class LocationPoint implements Serializable {

    private double latitude;  // Широта
    private double longitude; // Долгота

    /**
     * Вычисляет расстояние до другой точки в километрах
     * используя формулу гаверсинусов
     *
     * @param other другая точка
     * @return расстояние в километрах
     */
    public double distanceTo(LocationPoint other) {
        final int EARTH_RADIUS = 6371; // Радиус Земли в километрах

        double latDistance = Math.toRadians(other.latitude - this.latitude);
        double lonDistance = Math.toRadians(other.longitude - this.longitude);

        double a = Math.sin(latDistance / 2) * Math.sin(latDistance / 2)
                + Math.cos(Math.toRadians(this.latitude)) * Math.cos(Math.toRadians(other.latitude))
                * Math.sin(lonDistance / 2) * Math.sin(lonDistance / 2);

        double c = 2 * Math.atan2(Math.sqrt(a), Math.sqrt(1 - a));

        return EARTH_RADIUS * c;
    }

    /**
     * Создает LocationPoint из объекта org.locationtech.jts.geom.Point
     *
     * @param point объект Point библиотеки JTS
     * @return объект LocationPoint
     */
    public static LocationPoint fromJtsPoint(Point point) {
        return new LocationPoint(point.getY(), point.getX());
    }
}