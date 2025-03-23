package com.example.taxiservice.service;

import com.example.taxiservice.entity.Order;
import com.example.taxiservice.entity.Payment;
import com.example.taxiservice.entity.User;
import com.example.taxiservice.enums.PaymentMethod;
import com.example.taxiservice.enums.PaymentStatus;
import com.example.taxiservice.exception.InsufficientFundsException;
import com.example.taxiservice.repository.PaymentRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
public class PaymentService {

    private final PaymentRepository paymentRepository;
    private final UserService userService;

    /**
     * Обрабатывает платеж для завершенного заказа
     * По умолчанию снимает деньги с баланса клиента и начисляет водителю
     *
     * @param order завершенный заказ
     * @return созданный платеж
     */
    @Transactional
    public Payment processPayment(Order order) {
        // Предполагаем, что деньги берутся с баланса клиента (WALLET)
        return processPayment(order, PaymentMethod.WALLET);
    }

    /**
     * Обрабатывает платеж для завершенного заказа с указанным методом оплаты
     *
     * @param order заказ
     * @param paymentMethod метод оплаты
     * @return созданный платеж
     */
    @Transactional
    public Payment processPayment(Order order, PaymentMethod paymentMethod) {
        // Проверяем, что заказ завершен
        if (order.getStatus() != com.example.taxiservice.enums.OrderStatus.COMPLETED) {
            throw new IllegalStateException("Платеж может быть обработан только для завершенного заказа");
        }

        // Проверяем, что у заказа есть клиент и водитель
        if (order.getClient() == null || order.getDriver() == null) {
            throw new IllegalStateException("Заказ должен иметь клиента и водителя для обработки платежа");
        }

        // Создаем платеж
        Payment payment = new Payment();
        payment.setOrder(order);
        payment.setAmount(order.getPrice());
        payment.setPaymentMethod(paymentMethod);
        payment.setCreatedAt(LocalDateTime.now());

        try {
            // Обрабатываем платеж в зависимости от метода оплаты
            switch (paymentMethod) {
                case WALLET:
                    processWalletPayment(order);
                    break;
                case CARD:
                    processCardPayment(order);
                    break;
                case CASH:
                    processCashPayment(order);
                    break;
                default:
                    throw new IllegalStateException("Неподдерживаемый метод оплаты: " + paymentMethod);
            }

            // Платеж успешен
            payment.setStatus(PaymentStatus.SUCCESS);
        } catch (Exception e) {
            // Платеж не удался
            payment.setStatus(PaymentStatus.FAILED);
            throw e; // Повторно выбрасываем исключение для обработки на уровне вызывающего кода
        }

        // Сохраняем платеж
        return paymentRepository.save(payment);
    }

    /**
     * Обрабатывает платеж с баланса клиента
     * Снимает деньги с баланса клиента и начисляет комиссию сервису и остаток водителю
     */
    private void processWalletPayment(Order order) {
        User client = order.getClient();
        User driver = order.getDriver();
        BigDecimal amount = order.getPrice();

        // Проверяем достаточно ли средств у клиента
        if (client.getBalance().compareTo(amount) < 0) {
            throw new InsufficientFundsException("Недостаточно средств на балансе клиента");
        }

        // Снимаем деньги с баланса клиента
        userService.withdrawBalance(client.getId(), amount);

        // Рассчитываем комиссию сервиса (например, 20% от суммы заказа)
        BigDecimal serviceCommission = amount.multiply(new BigDecimal("0.2"));

        // Остаток идет водителю
        BigDecimal driverAmount = amount.subtract(serviceCommission);

        // Начисляем деньги водителю
        userService.addBalance(driver.getId(), driverAmount);
    }

    /**
     * Обрабатывает платеж банковской картой
     * В реальном приложении здесь был бы вызов платежного шлюза
     */
    private void processCardPayment(Order order) {
        User driver = order.getDriver();
        BigDecimal amount = order.getPrice();

        // Имитация обработки платежа картой через платежный шлюз
        // В реальности здесь был бы вызов API платежного сервиса

        // Рассчитываем комиссию сервиса (например, 20% от суммы заказа)
        BigDecimal serviceCommission = amount.multiply(new BigDecimal("0.2"));

        // Остаток идет водителю
        BigDecimal driverAmount = amount.subtract(serviceCommission);

        // Начисляем деньги водителю
        userService.addBalance(driver.getId(), driverAmount);
    }

    /**
     * Обрабатывает платеж наличными
     * При оплате наличными просто фиксируем факт оплаты,
     * предполагая, что водитель получил деньги от клиента
     */
    private void processCashPayment(Order order) {
        // При оплате наличными просто фиксируем факт оплаты
        // Предполагается, что водитель сдает комиссию сервису отдельно
    }

    /**
     * Находит платеж по ID заказа
     *
     * @param orderId ID заказа
     * @return платеж
     */
    public Payment findByOrderId(Long orderId) {
        return paymentRepository.findByOrderId(orderId)
                .orElseThrow(() -> new com.example.taxiservice.exception.ResourceNotFoundException(
                        "Платеж не найден для заказа с ID: " + orderId));
    }
}