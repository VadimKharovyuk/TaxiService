package com.example.taxiservice.controller;

import com.example.taxiservice.dto.CarDto;
import com.example.taxiservice.entity.User;
import com.example.taxiservice.enums.CarCategory;
import com.example.taxiservice.service.CarBrandService;
import com.example.taxiservice.service.CarService;
import com.example.taxiservice.service.UserService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
@RequestMapping("/cars")
@PreAuthorize("hasRole('ROLE_DRIVER')")
@RequiredArgsConstructor
public class CarController {

    private final CarService carService;
    private final CarBrandService carBrandService;
    private final UserService userService;

    /**
     * Отображает список автомобилей водителя
     */
    @GetMapping
    public String listCars(Authentication authentication, Model model) {
        User driver = userService.findByEmail(authentication.getName());
        model.addAttribute("cars", carService.findByDriverId(driver.getId()));
        return "cars/list";
    }

    /**
     * Форма для создания нового автомобиля
     */
    @GetMapping("/new")
    public String showCreateForm(Model model) {
        model.addAttribute("car", new CarDto());
        model.addAttribute("brands", carBrandService.findAll());
        model.addAttribute("categories", CarCategory.values());
        return "cars/form";
    }

    /**
     * Обработка создания нового автомобиля
     */
    @PostMapping("/new")
    public String createCar(
            @Valid @ModelAttribute("car") CarDto carDto,
            BindingResult result,
            Authentication authentication,
            Model model,
            RedirectAttributes redirectAttributes) {

        if (result.hasErrors()) {
            model.addAttribute("brands", carBrandService.findAll());
            model.addAttribute("categories", CarCategory.values());
            return "cars/form";
        }

        try {
            User driver = userService.findByEmail(authentication.getName());
            carService.registerCar(carDto, driver.getId());
            redirectAttributes.addFlashAttribute("successMessage", "Автомобиль успешно зарегистрирован");
            return "redirect:/cars";
        } catch (Exception e) {
            model.addAttribute("errorMessage", e.getMessage());
            model.addAttribute("brands", carBrandService.findAll());
            model.addAttribute("categories", CarCategory.values());
            return "cars/form";
        }
    }

    /**
     * Форма для редактирования автомобиля
     */
    @GetMapping("/edit/{id}")
    public String showEditForm(@PathVariable Long id, Authentication authentication, Model model, RedirectAttributes redirectAttributes) {
        try {
            User driver = userService.findByEmail(authentication.getName());
            CarDto car = carService.findById(id);

            // Проверяем, что автомобиль принадлежит текущему водителю
            if (!car.getDriverId().equals(driver.getId())) {
                redirectAttributes.addFlashAttribute("errorMessage", "У вас нет доступа к этому автомобилю");
                return "redirect:/cars";
            }

            model.addAttribute("car", car);
            model.addAttribute("brands", carBrandService.findAll());
            model.addAttribute("categories", CarCategory.values());
            return "cars/form";
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("errorMessage", e.getMessage());
            return "redirect:/cars";
        }
    }

    /**
     * Обработка обновления автомобиля
     */
    @PostMapping("/edit/{id}")
    public String updateCar(
            @PathVariable Long id,
            @Valid @ModelAttribute("car") CarDto carDto,
            BindingResult result,
            Authentication authentication,
            Model model,
            RedirectAttributes redirectAttributes) {

        carDto.setId(id);

        if (result.hasErrors()) {
            model.addAttribute("brands", carBrandService.findAll());
            model.addAttribute("categories", CarCategory.values());
            return "cars/form";
        }

        try {
            User driver = userService.findByEmail(authentication.getName());
            carService.updateCar(carDto, driver.getId());
            redirectAttributes.addFlashAttribute("successMessage", "Автомобиль успешно обновлен");
            return "redirect:/cars";
        } catch (Exception e) {
            model.addAttribute("errorMessage", e.getMessage());
            model.addAttribute("brands", carBrandService.findAll());
            model.addAttribute("categories", CarCategory.values());
            return "cars/form";
        }
    }

    /**
     * Обработка удаления автомобиля
     */
    @PostMapping("/delete/{id}")
    public String deleteCar(@PathVariable Long id, Authentication authentication, RedirectAttributes redirectAttributes) {
        try {
            User driver = userService.findByEmail(authentication.getName());
            carService.deleteCar(id, driver.getId());
            redirectAttributes.addFlashAttribute("successMessage", "Автомобиль успешно удален");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("errorMessage", e.getMessage());
        }

        return "redirect:/cars";
    }

    /**
     * Обновление доступности автомобиля
     */
    @PostMapping("/availability/{id}")
    public String updateAvailability(
            @PathVariable Long id,
            @RequestParam boolean isAvailable,
            Authentication authentication,
            RedirectAttributes redirectAttributes) {

        try {
            User driver = userService.findByEmail(authentication.getName());
            carService.updateAvailability(id, isAvailable, driver.getId());

            String status = isAvailable ? "доступным" : "недоступным";
            redirectAttributes.addFlashAttribute("successMessage", "Автомобиль отмечен как " + status);
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("errorMessage", e.getMessage());
        }

        return "redirect:/cars";
    }

    /**
     * Обновление местоположения автомобиля
     */
    @PostMapping("/location/{id}")
    public String updateLocation(
            @PathVariable Long id,
            @RequestParam Double latitude,
            @RequestParam Double longitude,
            Authentication authentication,
            RedirectAttributes redirectAttributes) {

        try {
            User driver = userService.findByEmail(authentication.getName());
            carService.updateLocation(id, latitude, longitude, driver.getId());
            redirectAttributes.addFlashAttribute("successMessage", "Местоположение автомобиля обновлено");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("errorMessage", e.getMessage());
        }

        return "redirect:/cars";
    }
}