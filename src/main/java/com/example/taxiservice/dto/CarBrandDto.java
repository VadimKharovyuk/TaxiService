package com.example.taxiservice.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class CarBrandDto {

    private Long id;

    @NotBlank(message = "Название бренда не может быть пустым")
    @Size(min = 2, max = 50, message = "Название бренда должно содержать от 2 до 50 символов")
    private String name;


}