package com.example.taxiservice.repository;

import com.example.taxiservice.entity.Order;
import com.example.taxiservice.entity.User;
import com.example.taxiservice.enums.OrderStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.Collection;
import java.util.List;
import java.util.Optional;

@Repository
public interface OrderRepository extends JpaRepository<Order, Long> {

    // Поиск заказов по клиенту
    List<Order> findByClient(User client);

    // Поиск заказов по водителю
    List<Order> findByDriver(User driver);

    // Поиск заказов по статусу
    List<Order> findByStatus(OrderStatus status);

    // Поиск заказов по клиенту и статусу
    List<Order> findByClientAndStatus(User client, OrderStatus status);

    // Поиск заказов по водителю и статусу
    List<Order> findByDriverAndStatus(User driver, OrderStatus status);

    // Поиск заказов по клиенту и нескольким статусам
    List<Order> findByClientAndStatusIn(User client, Collection<OrderStatus> statuses);

    // Поиск заказов по водителю и нескольким статусам
    List<Order> findByDriverAndStatusIn(User driver, Collection<OrderStatus> statuses);

    // Поиск заказов за определенный период
    List<Order> findByCreatedAtBetween(LocalDateTime start, LocalDateTime end);

    // Поиск заказов по завершенному периоду
    List<Order> findByCompletedAtBetween(LocalDateTime start, LocalDateTime end);

    // Поиск заказов для конкретного автомобиля
    List<Order> findByCarId(Long carId);

    List<Order> findByClientIdAndStatusIn(Long clientId, List<OrderStatus> statuses);
    Optional<Order> findByIdAndClientId(Long id, Long clientId);
}