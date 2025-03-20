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
        return "login";
    }

    @GetMapping("/register/client")
    public String registerClientPage(Model model) {
        model.addAttribute("registerRequest", new RegisterRequest());
        model.addAttribute("userType", "CLIENT");
        return "register";
    }

    @GetMapping("/register/driver")
    public String registerDriverPage(Model model) {
        model.addAttribute("registerRequest", new RegisterRequest());
        model.addAttribute("userType", "DRIVER");
        return "register";
    }

    @PostMapping("/register/client")
    public String registerClient(
            @Valid @ModelAttribute("registerRequest") RegisterRequest request,
            BindingResult result,
            RedirectAttributes redirectAttributes) {

        if (result.hasErrors()) {
            return "register";
        }

        if (userService.existsByEmail(request.getEmail())) {
            result.rejectValue("email", "error.email", "Этот email уже используется");
            return "register";
        }

        // Регистрируем клиента
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
            @Valid @ModelAttribute("registerRequest") RegisterRequest registerRequest,
            BindingResult result,
            RedirectAttributes redirectAttributes) {

        if (result.hasErrors()) {
            return "register";
        }

        if (userService.existsByEmail(registerRequest.getEmail())) {
            result.rejectValue("email", "error.email", "Этот email уже используется");
            return "register";
        }

        // Создаем нового водителя
        User user = new User();
        user.setName(registerRequest.getName());
        user.setEmail(registerRequest.getEmail());
        user.setPassword(passwordEncoder.encode(registerRequest.getPassword()));
        user.setAge(registerRequest.getAge());
        user.setRole(Role.DRIVER);
        user.setBalance(BigDecimal.ZERO);

        userService.save(user);

        redirectAttributes.addFlashAttribute("successMessage", "Регистрация успешна! Теперь вы можете войти.");
        return "redirect:/login";
    }

    @GetMapping("/dashboard")
    public String dashboard() {
        return "dashboard";
    }
}