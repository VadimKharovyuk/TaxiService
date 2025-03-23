//package com.example.taxiservice.controller;
//
//
//
//import com.example.taxiservice.dto.OrderCreateDto;
//import com.example.taxiservice.dto.OrderDto;
//import com.example.taxiservice.entity.Order;
//import com.example.taxiservice.entity.User;
//import com.example.taxiservice.enums.CarCategory;
//import com.example.taxiservice.service.CarService;
//import com.example.taxiservice.service.OrderService;
//import com.example.taxiservice.service.UserService;
//import jakarta.validation.Valid;
//import lombok.RequiredArgsConstructor;
//import org.springframework.security.access.prepost.PreAuthorize;
//import org.springframework.security.core.Authentication;
//import org.springframework.stereotype.Controller;
//import org.springframework.ui.Model;
//import org.springframework.validation.BindingResult;
//import org.springframework.web.bind.annotation.*;
//import org.springframework.web.servlet.mvc.support.RedirectAttributes;
//
//@Controller
//@RequestMapping("/orders")
//@PreAuthorize("hasRole('CLIENT')")
//@RequiredArgsConstructor
//public class OrderController {
//
//    private final OrderService orderService;
//    private final UserService userService;
//
//
//    @GetMapping
//    public String listOrders(Model model, Authentication authentication) {
//        User client = userService.findByEmail(authentication.getName());
//        model.addAttribute("orders", orderService.findByClientId(client.getId()));
//        return "orders/list";
//    }
//
//    @GetMapping("/active")
//    public String listActiveOrders(Model model, Authentication authentication) {
//        User client = userService.findByEmail(authentication.getName());
//        model.addAttribute("orders", orderService.findActiveByClientId(client.getId()));
//        return "orders/active";
//    }
//
//    @GetMapping("/new")
//    public String showOrderForm(Model model) {
//        model.addAttribute("order", new OrderCreateDto());
//        model.addAttribute("categories", CarCategory.values());
//        return "orders/form";
//    }
//
//    @PostMapping("/new")
//    public String createOrder(
//            @Valid @ModelAttribute("order") OrderCreateDto orderDto,
//            BindingResult result,
//            Authentication authentication,
//            RedirectAttributes redirectAttributes) {
//
//        if (result.hasErrors()) {
//            return "orders/form";
//        }
//
//        try {
//            User client = userService.findByEmail(authentication.getName());
//
//            OrderDto order = orderService.createOrder(
//                    client.getId(),
//                    orderDto.getPickupLatitude(),
//                    orderDto.getPickupLongitude(),
//                    orderDto.getDropoffLatitude(),
//                    orderDto.getDropoffLongitude(),
//                    orderDto.getCategory()
//            );
//
//            // Пытаемся автоматически найти и назначить водителя
//            try {
//                orderService.assignDriver(order.getId());
//                redirectAttributes.addFlashAttribute("successMessage", "Заказ создан и водитель назначен!");
//            } catch (Exception e) {
//                redirectAttributes.addFlashAttribute("successMessage",
//                        "Заказ создан! Водитель будет назначен в ближайшее время.");
//            }
//
//            return "redirect:/orders/active";
//        } catch (Exception e) {
//            redirectAttributes.addFlashAttribute("errorMessage", "Ошибка при создании заказа: " + e.getMessage());
//            return "orders/form";
//        }
//    }
//
//    @PostMapping("/cancel/{id}")
//    public String cancelOrder(
//            @PathVariable Long id,
//            Authentication authentication,
//            RedirectAttributes redirectAttributes) {
//
//        try {
//            User client = userService.findByEmail(authentication.getName());
//            orderService.cancelOrder(id, client.getId());
//            redirectAttributes.addFlashAttribute("successMessage", "Заказ успешно отменен");
//        } catch (Exception e) {
//            redirectAttributes.addFlashAttribute("errorMessage", "Ошибка при отмене заказа: " + e.getMessage());
//        }
//
//        return "redirect:/orders/active";
//    }
//
//    @GetMapping("/details/{id}")
//    public String showOrderDetails(@PathVariable Long id, Model model, Authentication authentication) {
//        User client = userService.findByEmail(authentication.getName());
//        Order order = orderService.findById(id);
//
//        // Проверяем, принадлежит ли заказ текущему клиенту
//        if (!order.getClient().getId().equals(client.getId())) {
//            return "redirect:/orders";
//        }
//
//        model.addAttribute("order", order);
//        return "orders/details";
//    }
//}