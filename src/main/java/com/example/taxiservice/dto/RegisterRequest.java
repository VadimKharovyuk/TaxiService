package com.example.taxiservice.dto.auth;

import jakarta.validation.constraints.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * DTO для передачи данных формы регистрации пользователя
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class RegisterRequest {

    @NotBlank(message = "Имя не может быть пустым")
    @Size(min = 2, max = 50, message = "Имя должно быть от 2 до 50 символов")
    private String name;

    @NotBlank(message = "Email не может быть пустым")
    @Email(message = "Должен быть действительным email адресом")
    @Size(max = 50, message = "Email не может быть длиннее 50 символов")
    private String email;

    @NotBlank(message = "Пароль не может быть пустым")
    @Size(min = 6, max = 40, message = "Пароль должен быть от 6 до 40 символов")
    private String password;

    @NotNull(message = "Возраст не может быть пустым")
    @Min(value = 18, message = "Возраст должен быть не менее 18 лет")
    @Max(value = 100, message = "Возраст должен быть не более 100 лет")
    private Integer age;
}