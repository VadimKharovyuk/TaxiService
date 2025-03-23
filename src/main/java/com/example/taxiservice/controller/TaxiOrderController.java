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
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
@RequestMapping("/taxi-orders")
@PreAuthorize("hasRole('CLIENT')")
@RequiredArgsConstructor
public class TaxiOrderController {

    private final OrderService orderService;
    private final UserService userService;

    /**
     * Отображает список активных заказов такси
     */
    @GetMapping("/active")
    public String showActiveOrders(Model model, Authentication authentication) {
        User client = userService.getUserByUsername(authentication.getName());
        model.addAttribute("orders", orderService.findActiveByClientId(client.getId()));
        return "taxi-orders/active";
    }

    /**
     * Отображает страницу с историей заказов
     */
    @GetMapping("/history")
    public String showOrderHistory(Model model, Authentication authentication) {
        User client = userService.getUserByUsername(authentication.getName());
        model.addAttribute("orders", orderService.findCompletedByClientId(client.getId()));
        return "taxi-orders/history";
    }

    /**
     * Отображает детали конкретного заказа
     */
    @GetMapping("/details/{id}")
    public String showOrderDetails(@PathVariable Long id, Model model, Authentication authentication) {
        User client = userService.getUserByUsername(authentication.getName());
        Order order = orderService.findById(id);

        // Проверяем, принадлежит ли заказ текущему клиенту
        if (!order.getClient().getId().equals(client.getId())) {
            return "redirect:/taxi-orders/active";
        }

        model.addAttribute("order", order);
        return "taxi-orders/details";
    }

    /**
     * Отменяет заказ
     */
    @GetMapping("/cancel/{id}")
    public String cancelOrder(@PathVariable Long id, Authentication authentication, RedirectAttributes redirectAttributes) {
        try {
            User client = userService.getUserByUsername(authentication.getName());
            orderService.cancelOrder(id, client.getId());
            redirectAttributes.addFlashAttribute("successMessage", "Заказ успешно отменен");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("errorMessage", "Ошибка при отмене заказа: " + e.getMessage());
        }
        return "redirect:/taxi-orders/active";
    }

    /**
     * Перенаправляет на страницу карты для создания нового заказа
     */
    @GetMapping("/new")
    public String newOrderForm() {
        return "redirect:/map/order";
    }

    /**
     * Страница подтверждения успешного заказа
     */
    @GetMapping("/success/{id}")
    public String orderSuccessPage(@PathVariable Long id, Model model, Authentication authentication) {
        User client = userService.getUserByUsername(authentication.getName());
        Order order = orderService.findById(id);

        // Проверяем, принадлежит ли заказ текущему клиенту
        if (!order.getClient().getId().equals(client.getId())) {
            return "redirect:/taxi-orders/active";
        }

        model.addAttribute("order", order);
        return "taxi-orders/success";
    }
}