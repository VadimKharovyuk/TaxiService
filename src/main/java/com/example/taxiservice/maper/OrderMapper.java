package com.example.taxiservice.maper;//package com.example.taxiservice.mapper;
//
//import com.example.taxiservice.dto.OrderDto;
//import com.example.taxiservice.entity.Order;
//import com.example.taxiservice.entity.User;
//import com.example.taxiservice.enums.OrderStatus;
//import com.example.taxiservice.service.CarService;
//import com.example.taxiservice.service.UserService;
//import lombok.RequiredArgsConstructor;
//import org.springframework.stereotype.Component;
//
//import java.time.LocalDateTime;
//import java.util.List;
//import java.util.stream.Collectors;
//
//@Component
//@RequiredArgsConstructor
//public class OrderMapper {
//
//    private final UserService userService;
//    private final CarService carService;
//
//    /**
//     * Преобразует сущность Order в DTO
//     */
//    public OrderDto toDto(Order order) {
//        if (order == null) {
//            return null;
//        }
//
//        OrderDto dto = new OrderDto();
//        dto.setId(order.getId());
//
//        // Информация о клиенте
//        if (order.getClient() != null) {
//            dto.setClientId(order.getClient().getId());
//            dto.setClientName(order.getClient().getName());
//            dto.setClientEmail(order.getClient().getEmail());
//        }
//
//        // Информация о водителе
//        if (order.getDriver() != null) {
//            dto.setDriverId(order.getDriver().getId());
//            dto.setDriverName(order.getDriver().getName());
//            dto.setDriverEmail(order.getDriver().getEmail());
//        }
//
//        // Информация об автомобиле
//        if (order.getCar() != null) {
//            dto.setCarId(order.getCar().getId());
//            dto.setCarBrand(order.getCar().getBrand().getName());
//            dto.setCarModel(order.getCar().getModel());
//            dto.setCarLicensePlate(order.getCar().getLicensePlate());
//        }
//
//        // Координаты
//        dto.setPickupLatitude(order.getPickupLatitude());
//        dto.setPickupLongitude(order.getPickupLongitude());
//        dto.setDropoffLatitude(order.getDropoffLatitude());
//        dto.setDropoffLongitude(order.getDropoffLongitude());
//
//        // Детали поездки
//        dto.setDistance(order.getDistance());
//        dto.setDuration(order.getDuration());
//        dto.setPrice(order.getPrice());
//        dto.setCategory(order.getCategory());
//        dto.setStatus(order.getStatus());
//        dto.setCreatedAt(order.getCreatedAt());
//        dto.setCompletedAt(order.getCompletedAt());
//
//        // Информация о платеже
//        dto.setHasPayment(order.getPayment() != null);
//        if (order.getPayment() != null) {
//            dto.setPaymentStatus(order.getPayment().getStatus().getDescription());
//        }
//
//        return dto;
//    }
//
//    /**
//     * Преобразует OrderDto в сущность Order
//     * Примечание: не все поля заполняются (например, client, driver и car должны быть получены из сервисов)
//     */
//    public Order toEntity(OrderDto dto) {
//        if (dto == null) {
//            return null;
//        }
//
//        Order order = new Order();
//        order.setId(dto.getId());
//
//        // Устанавливаем клиента, если ID указан
//        if (dto.getClientId() != null) {
//            User client = userService.findById(dto.getClientId());
//            order.setClient(client);
//        }
//
//        // Устанавливаем водителя, если ID указан
//        if (dto.getDriverId() != null) {
//            User driver = userService.findById(dto.getDriverId());
//            order.setDriver(driver);
//        }
//
//        // Устанавливаем автомобиль, если ID указан
//        if (dto.getCarId() != null) {
//            order.setCar(carService.findEntityById(dto.getCarId()));
//        }
//
//        // Координаты
//        order.setPickupLatitude(dto.getPickupLatitude());
//        order.setPickupLongitude(dto.getPickupLongitude());
//        order.setDropoffLatitude(dto.getDropoffLatitude());
//        order.setDropoffLongitude(dto.getDropoffLongitude());
//
//        // Детали поездки
//        order.setDistance(dto.getDistance() != null ? dto.getDistance() : 0.0);
//        order.setDuration(dto.getDuration() != null ? dto.getDuration() : 0);
//        order.setPrice(dto.getPrice());
//        order.setCategory(dto.getCategory());
//
//        // Статус
//        if (dto.getStatus() != null) {
//            order.setStatus(dto.getStatus());
//        }
//
//        // Даты
//        if (dto.getCreatedAt() != null) {
//            order.setCreatedAt(dto.getCreatedAt());
//        }
//        order.setCompletedAt(dto.getCompletedAt());
//
//        return order;
//    }
//
//    /**
//     * Обновляет существующую сущность Order из DTO
//     */
//    public void updateEntityFromDto(OrderDto dto, Order order) {
//        if (dto == null || order == null) {
//            return;
//        }
//
//        // Обновляем только те поля, которые имеет смысл обновлять
//        // Координаты
//        if (dto.getPickupLatitude() != null) order.setPickupLatitude(dto.getPickupLatitude());
//        if (dto.getPickupLongitude() != null) order.setPickupLongitude(dto.getPickupLongitude());
//        if (dto.getDropoffLatitude() != null) order.setDropoffLatitude(dto.getDropoffLatitude());
//        if (dto.getDropoffLongitude() != null) order.setDropoffLongitude(dto.getDropoffLongitude());
//
//        // Категория
//        if (dto.getCategory() != null) order.setCategory(dto.getCategory());
//
//        // Статус - если статус меняется, то может потребоваться дополнительная логика
//        if (dto.getStatus() != null && dto.getStatus() != order.getStatus()) {
//            order.setStatus(dto.getStatus());
//
//            // Если статус изменен на COMPLETED, устанавливаем время завершения
//            if (dto.getStatus() == OrderStatus.COMPLETED && order.getCompletedAt() == null) {
//                order.setCompletedAt(LocalDateTime.now());
//            }
//        }
//    }
//
//    /**
//     * Преобразует список сущностей Order в список OrderDto
//     */
//    public List<OrderDto> toDtoList(List<Order> orders) {
//        if (orders == null) {
//            return null;
//        }
//
//        return orders.stream()
//                .map(this::toDto)
//                .collect(Collectors.toList());
//    }
//}



import com.example.taxiservice.dto.OrderResponseDto;
import com.example.taxiservice.entity.Car;
import com.example.taxiservice.entity.Order;
import com.example.taxiservice.entity.User;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;

@Component
public class OrderMapper {

    /**
     * Преобразует сущность Order в DTO для ответа клиенту
     */
    public OrderResponseDto toResponseDto(Order order) {
        if (order == null) {
            return null;
        }

        OrderResponseDto dto = new OrderResponseDto();
        dto.setId(order.getId());

        // Данные о поездке
        dto.setPickupLatitude(order.getPickupLatitude());
        dto.setPickupLongitude(order.getPickupLongitude());
        dto.setDropoffLatitude(order.getDropoffLatitude());
        dto.setDropoffLongitude(order.getDropoffLongitude());
        dto.setStatus(order.getStatus());
        dto.setCreatedAt(order.getCreatedAt() != null ? order.getCreatedAt() : LocalDateTime.now());

        // Данные о машине и водителе
        Car car = order.getCar();
        if (car != null) {
            dto.setCarModel(car.getBrand().getName() + " " + car.getModel());
            dto.setCarLicensePlate(car.getLicensePlate());
            dto.setCarColor(car.getColor());
            dto.setCarCategory(car.getCategory());

            User driver = car.getDriver();
            if (driver != null) {
                dto.setDriverName(driver.getName() + " " + driver.getEmail());
                dto.setDriverPhone(driver.getPhoto());
                // Если у вас есть рейтинг водителя, можно добавить и его
            }
        }

        // Вы можете добавить расчет оценочных данных
        // (цена, продолжительность, расстояние) если у вас есть такие методы
        // dto.setEstimatedPrice(calculatePrice(order));
        // dto.setEstimatedDuration(calculateDuration(order));
        // dto.setDistance(calculateDistance(order));

        return dto;
    }
}