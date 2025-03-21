package com.example.taxiservice.controller;

import com.example.taxiservice.entity.User;
import com.example.taxiservice.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.io.IOException;
import java.math.BigDecimal;

@Controller
@RequestMapping("/profile")
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;

    @GetMapping
    public String showProfile(Authentication authentication, Model model) {
        String email = authentication.getName();
        User user = userService.findByEmail(email);

        model.addAttribute("user", user);
        return "profile/profile";
    }

    @PostMapping("/update")
    public String updateProfile(
            @RequestParam String name,
            @RequestParam Integer age,
            Authentication authentication,
            RedirectAttributes redirectAttributes) {

        String email = authentication.getName();
        User user = userService.findByEmail(email);

        user.setName(name);
        user.setAge(age);

        userService.update(user);

        redirectAttributes.addFlashAttribute("successMessage", "Профиль успешно обновлен");
        return "redirect:/profile";
    }

    @PostMapping("/upload-photo")
    public String uploadProfilePhoto(
            @RequestParam("photo") MultipartFile photo,
            Authentication authentication,
            RedirectAttributes redirectAttributes) {

        if (photo.isEmpty()) {
            redirectAttributes.addFlashAttribute("errorMessage", "Пожалуйста, выберите файл");
            return "redirect:/profile";
        }

        // Проверяем тип файла
        if (!photo.getContentType().startsWith("image/")) {
            redirectAttributes.addFlashAttribute("errorMessage", "Пожалуйста, загрузите изображение");
            return "redirect:/profile";
        }

        try {
            String email = authentication.getName();
            User user = userService.findByEmail(email);

            userService.updateProfilePhoto(user.getId(), photo);

            redirectAttributes.addFlashAttribute("successMessage", "Фото профиля успешно обновлено");
        } catch (IOException e) {
            redirectAttributes.addFlashAttribute("errorMessage", "Ошибка при загрузке фото: " + e.getMessage());
        }

        return "redirect:/profile";
    }

    @PostMapping("/remove-photo")
    public String removeProfilePhoto(
            Authentication authentication,
            RedirectAttributes redirectAttributes) {

        try {
            String email = authentication.getName();
            User user = userService.findByEmail(email);

            userService.removeProfilePhoto(user.getId());

            redirectAttributes.addFlashAttribute("successMessage", "Фото профиля успешно удалено");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("errorMessage", "Ошибка при удалении фото: " + e.getMessage());
        }

        return "redirect:/profile";
    }

    @PostMapping("/add-balance")
    public String addBalance(
            @RequestParam BigDecimal amount,
            Authentication authentication,
            RedirectAttributes redirectAttributes) {

        if (amount.compareTo(BigDecimal.ZERO) <= 0) {
            redirectAttributes.addFlashAttribute("errorMessage", "Сумма должна быть положительной");
            return "redirect:/profile";
        }

        String email = authentication.getName();
        User user = userService.findByEmail(email);

        userService.addBalance(user.getId(), amount);

        redirectAttributes.addFlashAttribute("successMessage", "Баланс успешно пополнен");
        return "redirect:/profile";
    }
}