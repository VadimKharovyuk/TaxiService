package com.example.taxiservice.controller;

import com.example.taxiservice.dto.auth.RegisterRequest;
import com.example.taxiservice.entity.User;
import com.example.taxiservice.enums.Role;
import com.example.taxiservice.service.UserService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.math.BigDecimal;
@Controller
@RequiredArgsConstructor
public class AuthController {

    private final UserService userService;
    private final PasswordEncoder passwordEncoder;

    @GetMapping("/login")
    public String loginPage() {
        return "aut/login";
    }

    @GetMapping("/register/client")
    public String registerClientPage(Model model) {
        model.addAttribute("registerRequest", new RegisterRequest());
        model.addAttribute("userType", "CLIENT");
        return "aut/register";
    }

    @GetMapping("/register/driver")
    public String registerDriverPage(Model model) {
        model.addAttribute("registerRequest", new RegisterRequest());
        model.addAttribute("userType", "DRIVER");
        return "aut/register";
    }

    @PostMapping("/register/client")
    public String registerClient(
            @Valid @ModelAttribute("registerRequest") RegisterRequest request,
            BindingResult result,
            Model model,
            RedirectAttributes redirectAttributes) {

        if (result.hasErrors()) {
            model.addAttribute("userType", "CLIENT");
            return "aut/register";
        }

        if (userService.existsByEmail(request.getEmail())) {
            result.rejectValue("email", "error.email", "Этот email уже используется");
            model.addAttribute("userType", "CLIENT");
            return "aut/register";
        }

        // Регистрируем клиента через сервис
        userService.registerClient(
                request.getName(),
                request.getEmail(),
                passwordEncoder.encode(request.getPassword()),
                request.getAge()
        );

        redirectAttributes.addFlashAttribute("successMessage", "Регистрация успешна! Теперь вы можете войти.");
        return "redirect:/login";
    }

    @PostMapping("/register/driver")
    public String registerDriver(
            @Valid @ModelAttribute("registerRequest") RegisterRequest request,
            BindingResult result,
            Model model,
            RedirectAttributes redirectAttributes) {

        if (result.hasErrors()) {
            model.addAttribute("userType", "DRIVER");
            return "aut/register";
        }

        if (userService.existsByEmail(request.getEmail())) {
            result.rejectValue("email", "error.email", "Этот email уже используется");
            model.addAttribute("userType", "DRIVER");
            return "aut/register";
        }

        // Регистрируем водителя через сервис
        userService.registerDriver(
                request.getName(),
                request.getEmail(),
                passwordEncoder.encode(request.getPassword()),
                request.getAge()
        );

        redirectAttributes.addFlashAttribute("successMessage", "Регистрация успешна! Теперь вы можете войти.");
        return "redirect:/login";
    }

}