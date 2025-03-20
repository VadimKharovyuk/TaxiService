package com.example.taxiservice.service;

import com.example.taxiservice.entity.User;
import com.example.taxiservice.enums.Role;
import com.example.taxiservice.exception.ResourceNotFoundException;
import com.example.taxiservice.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.List;

@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;

    public User findById(Long id) {
        return userRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Пользователь с ID " + id + " не найден"));
    }

    public User findByEmail(String email) {
        return userRepository.findByEmail(email)
                .orElseThrow(() -> new ResourceNotFoundException("Пользователь с email " + email + " не найден"));
    }

    public boolean existsByEmail(String email) {
        return userRepository.existsByEmail(email);
    }

    public List<User> findAllDrivers() {
        return userRepository.findByRole(Role.DRIVER);
    }

    public List<User> findAllClients() {
        return userRepository.findByRole(Role.CLIENT);
    }

    @Transactional
    public User save(User user) {
        return userRepository.save(user);
    }

    @Transactional
    public User update(User user) {
        // Проверяем существование пользователя
        User existingUser = findById(user.getId());

        // Обновляем только разрешенные поля
        existingUser.setName(user.getName());
        existingUser.setAge(user.getAge());
        existingUser.setPhoto(user.getPhoto());

        return userRepository.save(existingUser);
    }

    @Transactional
    public User addBalance(Long userId, BigDecimal amount) {
        User user = findById(userId);
        user.setBalance(user.getBalance().add(amount));
        return userRepository.save(user);
    }

    @Transactional
    public User withdrawBalance(Long userId, BigDecimal amount) {
        User user = findById(userId);

        if (user.getBalance().compareTo(amount) < 0) {
            throw new IllegalArgumentException("Недостаточно средств на балансе");
        }

        user.setBalance(user.getBalance().subtract(amount));
        return userRepository.save(user);
    }

    @Transactional
    public void delete(Long userId) {
        User user = findById(userId);
        userRepository.delete(user);
    }

    @Transactional
    public User registerClient(String name, String email, String encodedPassword, int age) {
        User user = new User();
        user.setName(name);
        user.setEmail(email);
        user.setPassword(encodedPassword);
        user.setAge(age);
        user.setRole(Role.CLIENT);
        user.setBalance(BigDecimal.ZERO);

        return userRepository.save(user);
    }

    @Transactional
    public User registerDriver(String name, String email, String encodedPassword, int age) {
        User user = new User();
        user.setName(name);
        user.setEmail(email);
        user.setPassword(encodedPassword);
        user.setAge(age);
        user.setRole(Role.DRIVER);
        user.setBalance(BigDecimal.ZERO);

        return userRepository.save(user);
    }
}