package com.example.taxiservice.service;//package com.example.taxiservice.service;
//
//import com.example.taxiservice.dto.OrderDto;
//import com.example.taxiservice.entity.Car;
//import com.example.taxiservice.entity.Order;
//import com.example.taxiservice.entity.User;
//import com.example.taxiservice.enums.CarCategory;
//import com.example.taxiservice.enums.OrderStatus;
//import com.example.taxiservice.exception.ResourceNotFoundException;
//import com.example.taxiservice.exception.UnauthorizedException;
//import com.example.taxiservice.mapper.OrderMapper;
//import com.example.taxiservice.repository.OrderRepository;
//import com.example.taxiservice.util.LocationPoint;
//import com.example.taxiservice.util.PriceCalculator;
//import lombok.RequiredArgsConstructor;
//import org.springframework.stereotype.Service;
//import org.springframework.transaction.annotation.Transactional;
//
//import java.math.BigDecimal;
//import java.time.LocalDateTime;
//import java.util.List;
//
//@Service
//@RequiredArgsConstructor
//public class OrderService {
//
//    private final OrderRepository orderRepository;
//    private final UserService userService;
//    private final LocationService locationService;
//    private final PriceCalculator priceCalculator;
//    private final PaymentService paymentService;
//    private final OrderMapper orderMapper;
//
//    /**
//     * Создает новый заказ и возвращает DTO
//     */
//    @Transactional
//    public OrderDto createOrder(Long clientId,
//                                Double pickupLatitude, Double pickupLongitude,
//                                Double dropoffLatitude, Double dropoffLongitude,
//                                CarCategory category) {
//
//        User client = userService.findById(clientId);
//
//        // Создаем объекты местоположения
//        LocationPoint pickup = new LocationPoint(pickupLatitude, pickupLongitude);
//        LocationPoint dropoff = new LocationPoint(dropoffLatitude, dropoffLongitude);
//
//        // Рассчитываем расстояние
//        double distance = locationService.calculateRouteDistance(pickup, dropoff);
//
//        // Рассчитываем длительность
//        int duration = locationService.estimateTripDuration(distance, category);
//
//        // Рассчитываем стоимость
//        BigDecimal price = priceCalculator.calculatePrice(distance, duration, category, LocalDateTime.now());
//
//        // Создаем заказ
//        Order order = new Order();
//        order.setClient(client);
//        order.setPickupLatitude(pickupLatitude);
//        order.setPickupLongitude(pickupLongitude);
//        order.setDropoffLatitude(dropoffLatitude);
//        order.setDropoffLongitude(dropoffLongitude);
//        order.setDistance(distance);
//        order.setDuration(duration);
//        order.setPrice(price);
//        order.setStatus(OrderStatus.PENDING);
//        order.setCreatedAt(LocalDateTime.now());
//        order.setCategory(category);
//
//        Order savedOrder = orderRepository.save(order);
//        return orderMapper.toDto(savedOrder);
//    }
//
//    /**
//     * Находит ближайшего водителя и назначает заказ, возвращает DTO
//     */
//    @Transactional
//    public OrderDto assignDriver(Long orderId) {
//        Order order = findById(orderId);
//
//        if (order.getStatus() != OrderStatus.PENDING) {
//            throw new IllegalStateException("Заказ не может быть назначен, т.к. имеет статус: " + order.getStatus());
//        }
//
//        // Создаем объект местоположения для поиска ближайших автомобилей
//        LocationPoint pickup = new LocationPoint(order.getPickupLatitude(), order.getPickupLongitude());
//
//        // Находим ближайшие автомобили (в радиусе 10 км)
//        List<Car> nearestCars = locationService.findNearestAvailableCars(pickup, 10.0, order.getCategory());
//
//        if (nearestCars.isEmpty()) {
//            // Если не нашли автомобилей в заданной категории, ищем любые автомобили
//            nearestCars = locationService.findNearestAvailableCars(pickup, 10.0, null);
//        }
//
//        if (nearestCars.isEmpty()) {
//            throw new IllegalStateException("Не найдено доступных автомобилей поблизости");
//        }
//
//        // Берем первый (ближайший) автомобиль
//        Car nearestCar = nearestCars.get(0);
//        User driver = nearestCar.getDriver();
//
//        // Назначаем водителя и автомобиль
//        order.setDriver(driver);
//        order.setCar(nearestCar);
//        order.setStatus(OrderStatus.ACCEPTED);
//
//        // Делаем автомобиль недоступным для других заказов
//        nearestCar.setAvailable(false);
//
//        Order savedOrder = orderRepository.save(order);
//        return orderMapper.toDto(savedOrder);
//    }
//
//    /**
//     * Водитель начинает поездку, возвращает DTO
//     */
//    @Transactional
//    public OrderDto startRide(Long orderId, Long driverId) {
//        Order order = findById(orderId);
//
//        if (order.getStatus() != OrderStatus.ACCEPTED) {
//            throw new IllegalStateException("Заказ не может быть начат, т.к. имеет статус: " + order.getStatus());
//        }
//
//        if (!order.getDriver().getId().equals(driverId)) {
//            throw new UnauthorizedException("Водитель не назначен на этот заказ");
//        }
//
//        order.setStatus(OrderStatus.ON_THE_WAY);
//        Order savedOrder = orderRepository.save(order);
//        return orderMapper.toDto(savedOrder);
//    }
//
//    /**
//     * Водитель завершает поездку, возвращает DTO
//     */
//    @Transactional
//    public OrderDto completeRide(Long orderId, Long driverId) {
//        Order order = findById(orderId);
//
//        if (order.getStatus() != OrderStatus.ON_THE_WAY) {
//            throw new IllegalStateException("Заказ не может быть завершен, т.к. имеет статус: " + order.getStatus());
//        }
//
//        if (!order.getDriver().getId().equals(driverId)) {
//            throw new UnauthorizedException("Водитель не назначен на этот заказ");
//        }
//
//        // Меняем статус заказа
//        order.setStatus(OrderStatus.COMPLETED);
//        order.setCompletedAt(LocalDateTime.now());
//
//        // Делаем автомобиль снова доступным
//        Car car = order.getCar();
//        car.setAvailable(true);
//
//        // Создаем платеж
//        paymentService.processPayment(order);
//
//        Order savedOrder = orderRepository.save(order);
//        return orderMapper.toDto(savedOrder);
//    }
//
//    /**
//     * Клиент или водитель отменяет заказ, возвращает DTO
//     */
//    @Transactional
//    public OrderDto cancelOrder(Long orderId, Long userId) {
//        Order order = findById(orderId);
//
//        if (order.getStatus() == OrderStatus.COMPLETED || order.getStatus() == OrderStatus.CANCELED) {
//            throw new IllegalStateException("Заказ не может быть отменен, т.к. имеет статус: " + order.getStatus());
//        }
//
//        // Проверяем, что пользователь имеет право отменить заказ
//        if (!order.getClient().getId().equals(userId) &&
//                (order.getDriver() == null || !order.getDriver().getId().equals(userId))) {
//            throw new UnauthorizedException("Пользователь не имеет права отменить этот заказ");
//        }
//
//        // Меняем статус заказа
//        order.setStatus(OrderStatus.CANCELED);
//
//        // Если был назначен автомобиль, делаем его снова доступным
//        if (order.getCar() != null) {
//            Car car = order.getCar();
//            car.setAvailable(true);
//        }
//
//        Order savedOrder = orderRepository.save(order);
//        return orderMapper.toDto(savedOrder);
//    }
//
//    /**
//     * Находит заказ по ID
//     */
//    public Order findById(Long id) {
//        return orderRepository.findById(id)
//                .orElseThrow(() -> new ResourceNotFoundException("Заказ не найден: " + id));
//    }
//
//    /**
//     * Находит заказ по ID и возвращает DTO
//     */
//    public OrderDto findDtoById(Long id) {
//        Order order = findById(id);
//        return orderMapper.toDto(order);
//    }
//
//    /**
//     * Находит заказы клиента и возвращает список DTO
//     */
//    public List<OrderDto> findByClientId(Long clientId) {
//        User client = userService.findById(clientId);
//        List<Order> orders = orderRepository.findByClient(client);
//        return orderMapper.toDtoList(orders);
//    }
//
//    /**
//     * Находит заказы водителя и возвращает список DTO
//     */
//    public List<OrderDto> findByDriverId(Long driverId) {
//        User driver = userService.findById(driverId);
//        List<Order> orders = orderRepository.findByDriver(driver);
//        return orderMapper.toDtoList(orders);
//    }
//
//    /**
//     * Находит активные заказы клиента и возвращает список DTO
//     */
//    public List<OrderDto> findActiveByClientId(Long clientId) {
//        User client = userService.findById(clientId);
//        List<Order> orders = orderRepository.findByClientAndStatusIn(
//                client,
//                List.of(OrderStatus.PENDING, OrderStatus.ACCEPTED, OrderStatus.ON_THE_WAY)
//        );
//        return orderMapper.toDtoList(orders);
//    }
//
//    /**
//     * Находит активные заказы водителя и возвращает список DTO
//     */
//    public List<OrderDto> findActiveByDriverId(Long driverId) {
//        User driver = userService.findById(driverId);
//        List<Order> orders = orderRepository.findByDriverAndStatusIn(
//                driver,
//                List.of(OrderStatus.ACCEPTED, OrderStatus.ON_THE_WAY)
//        );
//        return orderMapper.toDtoList(orders);
//    }
//}


import com.example.taxiservice.dto.OrderRequestDto;
import com.example.taxiservice.dto.OrderResponseDto;
import com.example.taxiservice.entity.Car;
import com.example.taxiservice.entity.Order;
import com.example.taxiservice.entity.User;
import com.example.taxiservice.enums.OrderStatus;
import com.example.taxiservice.maper.OrderMapper;
import com.example.taxiservice.repository.CarRepository;
import com.example.taxiservice.repository.OrderRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;

@Service
@RequiredArgsConstructor
public class OrderService {

    private final OrderRepository orderRepository;
    private final CarRepository carRepository;
    private final UserService userService;
    private final OrderMapper orderMapper;
    private final CarService carService;

    @Transactional
    public OrderResponseDto createOrder(OrderRequestDto orderRequestDto) {
        // Получаем текущего пользователя
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        User currentUser = userService.getUserByUsername(authentication.getName());

        // Находим подходящую машину
        Car availableCar = carRepository.findByIsAvailableTrueAndCategory(orderRequestDto.getCategory())
                .stream()
                .findFirst()
                .orElseThrow(() -> new RuntimeException("Нет доступных машин в выбранной категории"));

        // Устанавливаем машину как недоступную
        availableCar.setAvailable(false);

        // Создаем заказ
        Order order = new Order();
        order.setCar(availableCar);
        order.setClient(currentUser);
        order.setPickupLatitude(orderRequestDto.getPickupLatitude());
        order.setPickupLongitude(orderRequestDto.getPickupLongitude());
        order.setDropoffLatitude(orderRequestDto.getDropoffLatitude());
        order.setDropoffLongitude(orderRequestDto.getDropoffLongitude());
        order.setCategory(orderRequestDto.getCategory());

        // Устанавливаем расстояние, время и цену из запроса

        order.setDistance(orderRequestDto.getDistance() != null ?
                orderRequestDto.getDistance() : 0.0);
        order.setDuration(orderRequestDto.getDuration() != null ?
                orderRequestDto.getDuration() : 0);
        order.setPrice(orderRequestDto.getPrice());


        // Устанавливаем статус и время создания
        order.setStatus(OrderStatus.ACCEPTED);
        order.setCreatedAt(LocalDateTime.now());

        // Сохраняем заказ
        Order savedOrder = orderRepository.save(order);

        // Используем маппер для преобразования сущности в DTO
        return orderMapper.toResponseDto(savedOrder);
    }

    /**
     * Находит все активные заказы клиента
     */
    public List<Order> findActiveByClientId(Long clientId) {
        return orderRepository.findByClientIdAndStatusIn(
                clientId,
                Arrays.asList(OrderStatus.ACCEPTED, OrderStatus.DRIVER_ASSIGNED, OrderStatus.IN_PROGRESS)
        );
    }
    /**
     * Находит все завершенные заказы клиента
     */
    public List<Order> findCompletedByClientId(Long clientId) {
        return orderRepository.findByClientIdAndStatusIn(
                clientId,
                Arrays.asList(OrderStatus.COMPLETED, OrderStatus.CANCELED)
        );
    }
    /**
     * Отменяет заказ
     */
    @Transactional
    public void cancelOrder(Long orderId, Long clientId) {
        Order order = orderRepository.findByIdAndClientId(orderId, clientId)
                .orElseThrow(() -> new RuntimeException("Заказ не найден или не принадлежит текущему пользователю"));

        // Проверяем, можно ли отменить заказ
        if (order.getStatus() == OrderStatus.COMPLETED) {
            throw new RuntimeException("Нельзя отменить завершенный заказ");
        }

        // Отмечаем машину как доступную
        Car car = order.getCar();
        if (car != null) {
            car.setAvailable(true);
        }

        // Меняем статус заказа
        order.setStatus(OrderStatus.CANCELED);
        orderRepository.save(order);
    }

    public Order findById(Long id) {
        return orderRepository.findById(id).orElse(null);
    }

    /**
     * Находит все заказы для конкретного автомобиля
     */
    public List<Order> findByCarId(Long carId) {
        Car car = carService.findEntityById(carId);
        return orderRepository.findByCarId(carId);
    }

    /**
     * Находит активные заказы для конкретного автомобиля
     */
    public List<Order> findActiveByCarId(Long carId) {
        return orderRepository.findByCarIdAndStatusIn(
                carId,
                Arrays.asList(OrderStatus.ACCEPTED, OrderStatus.DRIVER_ASSIGNED, OrderStatus.IN_PROGRESS)
        );
    }

    /**
     * Находит завершенные заказы для конкретного автомобиля
     */
    public List<Order> findCompletedByCarId(Long carId) {
        return orderRepository.findByCarIdAndStatusIn(
                carId,
                Arrays.asList(OrderStatus.COMPLETED, OrderStatus.CANCELED)
        );
    }


}