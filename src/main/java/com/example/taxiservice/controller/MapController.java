package com.example.taxiservice.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/map")
@RequiredArgsConstructor
public class MapController {

    @Value("${here.maps.app-id}")
    private String hereAppId;

    @Value("${here.maps.api-key}")
    private String hereApiKey;

    /**
     * Страница с картой для выбора маршрута
     */
    @GetMapping
    public String showMap(Model model) {
        model.addAttribute("appId", hereAppId);
        model.addAttribute("apiKey", hereApiKey);
        return "map/index";
    }

    /**
     * Страница с картой для создания заказа (для клиентов)
     */
    @GetMapping("/order")
    @PreAuthorize("hasRole('CLIENT')")
    public String showOrderMap(Model model) {
        model.addAttribute("appId", hereAppId);
        model.addAttribute("apiKey", hereApiKey);
        return "map/order";
    }

    /**
     * Страница с картой для отображения заказов водителя (для водителей)
     */
    @GetMapping("/driver")
    @PreAuthorize("hasRole('DRIVER')")
    public String showDriverMap(Model model) {
        model.addAttribute("appId", hereAppId);
        model.addAttribute("apiKey", hereApiKey);
        return "map/driver";
    }
}