package com.example.taxiservice.repository;

import com.example.taxiservice.entity.Payment;
import com.example.taxiservice.enums.PaymentMethod;
import com.example.taxiservice.enums.PaymentStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Repository
public interface PaymentRepository extends JpaRepository<Payment, Long> {

    /**
     * Находит платеж по ID заказа
     */
    Optional<Payment> findByOrderId(Long orderId);

    /**
     * Находит платежи по статусу
     */
    List<Payment> findByStatus(PaymentStatus status);

    /**
     * Находит платежи по методу оплаты
     */
    List<Payment> findByPaymentMethod(PaymentMethod paymentMethod);

    /**
     * Находит платежи, созданные в указанный период
     */
    List<Payment> findByCreatedAtBetween(LocalDateTime start, LocalDateTime end);

    /**
     * Находит платежи по ID клиента
     */
    List<Payment> findByOrder_Client_Id(Long clientId);

    /**
     * Находит платежи по ID водителя
     */
    List<Payment> findByOrder_Driver_Id(Long driverId);
}