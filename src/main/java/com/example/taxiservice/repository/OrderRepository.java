package com.example.taxiservice.repository;

import com.example.taxiservice.entity.Order;
import com.example.taxiservice.entity.User;
import com.example.taxiservice.enums.OrderStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface OrderRepository extends JpaRepository<Order, Long> {
    List<Order> findByClient(User client);
    List<Order> findByDriver(User driver);
    List<Order> findByStatus(OrderStatus status);
    List<Order> findByClientAndStatus(User client, OrderStatus status);
    List<Order> findByDriverAndStatus(User driver, OrderStatus status);
    List<Order> findByCreatedAtBetween(LocalDateTime start, LocalDateTime end);
}