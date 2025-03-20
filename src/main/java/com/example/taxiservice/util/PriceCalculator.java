package com.example.taxiservice.util;

import com.example.taxiservice.enums.CarCategory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.DayOfWeek;
import java.time.LocalDateTime;
import java.time.LocalTime;

@Component
public class PriceCalculator {

    @Value("${taxi.price.base}")
    private BigDecimal basePrice; // Базовая стоимость поездки

    @Value("${taxi.price.per-km}")
    private BigDecimal pricePerKm; // Стоимость за километр

    @Value("${taxi.price.per-minute}")
    private BigDecimal pricePerMinute; // Стоимость за минуту ожидания

    @Value("${taxi.price.business-multiplier}")
    private BigDecimal businessMultiplier; // Множитель для бизнес-класса

    @Value("${taxi.price.luxury-multiplier}")
    private BigDecimal luxuryMultiplier; // Множитель для люкс-класса

    @Value("${taxi.price.peak-hours-multiplier}")
    private BigDecimal peakHoursMultiplier; // Множитель для часов пик

    @Value("${taxi.price.weekend-multiplier}")
    private BigDecimal weekendMultiplier; // Множитель для выходных

    @Value("${taxi.price.night-multiplier}")
    private BigDecimal nightMultiplier; // Множитель для ночного времени

    /**
     * Рассчитывает стоимость поездки
     *
     * @param distance расстояние в километрах
     * @param duration ориентировочная продолжительность в минутах
     * @param category категория автомобиля
     * @param orderTime время заказа
     * @return стоимость поездки
     */
    public BigDecimal calculatePrice(double distance, int duration, CarCategory category, LocalDateTime orderTime) {
        // Базовая стоимость + стоимость за расстояние
        BigDecimal price = basePrice.add(
                pricePerKm.multiply(BigDecimal.valueOf(distance))
        );

        // Учитываем категорию автомобиля
        if (category != null) {
            switch (category) {
                case BUSINESS:
                    price = price.multiply(businessMultiplier);
                    break;
                case LUXURY:
                    price = price.multiply(luxuryMultiplier);
                    break;
                case STANDARD:
                default:
                    // Стандартная цена, без изменений
                    break;
            }
        }

        // Учитываем время заказа (часы пик, выходные, ночное время)
        BigDecimal timeMultiplier = getTimeMultiplier(orderTime);
        price = price.multiply(timeMultiplier);

        // Округляем до 2 знаков после запятой
        return price.setScale(2, RoundingMode.HALF_UP);
    }

    /**
     * Определяет множитель стоимости в зависимости от времени заказа
     *
     * @param orderTime время заказа
     * @return множитель стоимости
     */
    private BigDecimal getTimeMultiplier(LocalDateTime orderTime) {
        BigDecimal multiplier = BigDecimal.ONE;

        // Проверяем, является ли день выходным
        DayOfWeek dayOfWeek = orderTime.getDayOfWeek();
        boolean isWeekend = dayOfWeek == DayOfWeek.SATURDAY || dayOfWeek == DayOfWeek.SUNDAY;

        if (isWeekend) {
            multiplier = multiplier.multiply(weekendMultiplier);
        }

        // Проверяем, является ли время часами пик (7-10 утра и 17-20 вечера по рабочим дням)
        LocalTime time = orderTime.toLocalTime();
        boolean isMorningPeak = time.isAfter(LocalTime.of(7, 0)) && time.isBefore(LocalTime.of(10, 0));
        boolean isEveningPeak = time.isAfter(LocalTime.of(17, 0)) && time.isBefore(LocalTime.of(20, 0));

        if (!isWeekend && (isMorningPeak || isEveningPeak)) {
            multiplier = multiplier.multiply(peakHoursMultiplier);
        }

        // Проверяем, является ли время ночным (23-6)
        boolean isNightTime = time.isAfter(LocalTime.of(23, 0)) || time.isBefore(LocalTime.of(6, 0));

        if (isNightTime) {
            multiplier = multiplier.multiply(nightMultiplier);
        }

        return multiplier;
    }

    /**
     * Рассчитывает ориентировочную стоимость поездки для отображения клиенту
     * перед подтверждением заказа
     *
     * @param distance расстояние в километрах
     * @param category категория автомобиля
     * @return ориентировочная стоимость поездки
     */
    public BigDecimal estimatePrice(double distance, CarCategory category) {
        // Для ориентировочной стоимости используем текущее время
        LocalDateTime now = LocalDateTime.now();

        // Ориентировочную продолжительность не учитываем в расчете
        int estimatedDuration = 0;

        return calculatePrice(distance, estimatedDuration, category, now);
    }
}