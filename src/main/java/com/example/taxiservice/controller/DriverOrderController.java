package com.example.taxiservice.controller;

import com.example.taxiservice.entity.Order;
import com.example.taxiservice.entity.User;
import com.example.taxiservice.service.OrderService;
import com.example.taxiservice.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
@RequestMapping("/driver/orders")
@PreAuthorize("hasRole('DRIVER')")
@RequiredArgsConstructor
public class DriverOrderController {

    private final OrderService orderService;
    private final UserService userService;

    @GetMapping
    public String listOrders(Model model, Authentication authentication) {
        User driver = userService.findByEmail(authentication.getName());
//        model.addAttribute("orders", orderService.findByDriverId(driver.getId()));
        return "driver/orders/list";
    }

    @GetMapping("/active")
    public String listActiveOrders(Model model, Authentication authentication) {
        User driver = userService.findByEmail(authentication.getName());
//        model.addAttribute("orders", orderService.findActiveByDriverId(driver.getId()));
        return "driver/orders/active";
    }

    @PostMapping("/start/{id}")
    public String startRide(
            @PathVariable Long id,
            Authentication authentication,
            RedirectAttributes redirectAttributes) {

        try {
            User driver = userService.findByEmail(authentication.getName());
//            orderService.startRide(id, driver.getId());
            redirectAttributes.addFlashAttribute("successMessage", "Поездка начата");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("errorMessage", "Ошибка при начале поездки: " + e.getMessage());
        }

        return "redirect:/driver/orders/active";
    }

    @PostMapping("/complete/{id}")
    public String completeRide(
            @PathVariable Long id,
            Authentication authentication,
            RedirectAttributes redirectAttributes) {

        try {
            User driver = userService.findByEmail(authentication.getName());
//            orderService.completeRide(id, driver.getId());
            redirectAttributes.addFlashAttribute("successMessage", "Поездка завершена");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("errorMessage", "Ошибка при завершении поездки: " + e.getMessage());
        }

        return "redirect:/driver/orders/active";
    }

    @PostMapping("/cancel/{id}")
    public String cancelOrder(
            @PathVariable Long id,
            Authentication authentication,
            RedirectAttributes redirectAttributes) {

        try {
            User driver = userService.findByEmail(authentication.getName());
            orderService.cancelOrder(id, driver.getId());
            redirectAttributes.addFlashAttribute("successMessage", "Заказ отменен");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("errorMessage", "Ошибка при отмене заказа: " + e.getMessage());
        }

        return "redirect:/driver/orders/active";
    }

    @GetMapping("/details/{id}")
    public String showOrderDetails(@PathVariable Long id, Model model, Authentication authentication) {
        User driver = userService.findByEmail(authentication.getName());
        Order order = orderService.findById(id);

        // Проверяем, назначен ли заказ текущему водителю
        if (order.getDriver() == null || !order.getDriver().getId().equals(driver.getId())) {
            return "redirect:/driver/orders";
        }

        model.addAttribute("order", order);
        return "driver/orders/details";
    }
}