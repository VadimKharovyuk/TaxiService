package com.example.taxiservice.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public enum Role {
    CLIENT("Клиент"),
    DRIVER("Водитель"),
    MODERATOR("Модератор"), // Администратор с ограниченными правами
    ADMIN("Администратор"); // Администратор с полными правами

    private final String description;
}