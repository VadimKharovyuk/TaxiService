package com.example.taxiservice.entity;
import com.example.taxiservice.enums.PaymentMethod;
import com.example.taxiservice.enums.PaymentStatus;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Entity
@Table(name = "payments")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Payment {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @OneToOne
    @JoinColumn(name = "order_id", nullable = false)
    private Order order;

    @Column(nullable = false)
    private BigDecimal amount;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private PaymentMethod paymentMethod;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private PaymentStatus status = PaymentStatus.PENDING;

    @Column(nullable = false)
    private LocalDateTime createdAt = LocalDateTime.now();

    // Дополнительные поля для отслеживания платежей
    private String transactionId; // ID транзакции в платежной системе
    private String receiptUrl; // URL для доступа к чеку

    // Методы для обработки платежа
    public void processPayment() {
        this.status = PaymentStatus.SUCCESS;
    }

    public void failPayment() {
        this.status = PaymentStatus.FAILED;
    }
}
