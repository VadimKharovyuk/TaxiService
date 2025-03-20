package com.example.taxiservice.repository;

import com.example.taxiservice.entity.Payment;
import com.example.taxiservice.enums.PaymentMethod;
import com.example.taxiservice.enums.PaymentStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface PaymentRepository extends JpaRepository<Payment, Long> {
    List<Payment> findByStatus(PaymentStatus status);
    List<Payment> findByPaymentMethod(PaymentMethod paymentMethod);
}