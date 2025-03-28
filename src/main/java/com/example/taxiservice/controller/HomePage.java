package com.example.taxiservice.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
@RequiredArgsConstructor
public class HomePage {

    @GetMapping
    public String home() {
        return "home";
    }
}
