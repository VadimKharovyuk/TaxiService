package com.example.taxiservice.controller;

import com.example.taxiservice.dto.CarDto;
import com.example.taxiservice.entity.Car;
import com.example.taxiservice.entity.Order;
import com.example.taxiservice.entity.User;
import com.example.taxiservice.service.CarService;
import com.example.taxiservice.service.OrderService;
import com.example.taxiservice.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/cars/{carId}/orders")
@PreAuthorize("hasRole('DRIVER')")
@RequiredArgsConstructor
public class CarOrdersController {

    private final OrderService orderService;
    private final CarService carService;
    private final UserService userService;

    @GetMapping
    public String listOrders(@PathVariable Long carId, Model model, Authentication authentication) {
        // Получаем текущего пользователя
        User currentUser = userService.getUserByUsername(authentication.getName());

        // Получаем машину
        CarDto carDto = carService.findById(carId);

        // Проверяем, принадлежит ли машина текущему водителю
        if (!carDto.getDriverId().equals(currentUser.getId())) {
            return "redirect:/cars";
        }

        // Добавляем данные в модель
        model.addAttribute("car", carDto);
        model.addAttribute("activeOrders", orderService.findActiveByCarId(carId));
        model.addAttribute("completedOrders", orderService.findCompletedByCarId(carId));

        return "cars/orders";
    }

    @GetMapping("/{orderId}")
    public String viewOrderDetails(@PathVariable Long carId, @PathVariable Long orderId, Model model, Authentication authentication) {
        // Получаем текущего пользователя
        User currentUser = userService.getUserByUsername(authentication.getName());

        // Получаем машину
        CarDto carDto = carService.findById(carId);

        // Проверяем, принадлежит ли машина текущему водителю
        if (!carDto.getDriverId().equals(currentUser.getId())) {
            return "redirect:/cars";
        }

        // Получаем заказ
        Order order = orderService.findById(orderId);

        // Проверяем, связан ли заказ с этой машиной
        if (order.getCar().getId() != carId) {
            return "redirect:/cars/" + carId + "/orders";
        }

        model.addAttribute("car", carDto);
        model.addAttribute("order", order);

        return "cars/order-details";
    }
}