package com.example.taxiservice.service;

import com.example.taxiservice.dto.CarDto;
import com.example.taxiservice.entity.Car;
import com.example.taxiservice.entity.User;
import com.example.taxiservice.enums.CarCategory;
import com.example.taxiservice.enums.Role;
import com.example.taxiservice.exception.ResourceNotFoundException;

import com.example.taxiservice.exception.UnauthorizedException;
import com.example.taxiservice.maper.CarMapper;
import com.example.taxiservice.repository.CarRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class CarService {

    private final CarRepository carRepository;
    private final UserService userService;
    private final CarBrandService carBrandService;
    private final CarMapper carMapper;

    /**
     * Находит все автомобили и преобразует их в DTO
     */
    public List<CarDto> findAll() {
        List<Car> cars = carRepository.findAll();
        return carMapper.toDtoList(cars);
    }

    /**
     * Находит автомобиль по ID и преобразует его в DTO
     */
    public CarDto findById(Long id) {
        Car car = findEntityById(id);
        return carMapper.toDto(car);
    }

    /**
     * Находит автомобиль по ID как сущность (для внутреннего использования)
     */
    public Car findEntityById(Long id) {
        return carRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Автомобиль не найден с ID: " + id));
    }

    /**
     * Находит автомобили по ID водителя и преобразует их в DTO
     */
    public List<CarDto> findByDriverId(Long driverId) {
        User driver = userService.findById(driverId);

        if (driver.getRole() != Role.DRIVER) {
            throw new UnauthorizedException("Пользователь не является водителем");
        }

        List<Car> cars = carRepository.findByDriver(driver);
        return carMapper.toDtoList(cars);
    }

    /**
     * Находит доступные автомобили по категории и преобразует их в DTO
     */
    public List<CarDto> findAvailableCarsByCategory(CarCategory category) {
        List<Car> cars = carRepository.findByIsAvailableAndCategory(true, category);
        return carMapper.toDtoList(cars);
    }

    /**
     * Регистрирует новый автомобиль для водителя из DTO
     */
    @Transactional
    public CarDto registerCar(CarDto carDto, Long driverId) {
        User driver = userService.findById(driverId);

        if (driver.getRole() != Role.DRIVER) {
            throw new UnauthorizedException("Пользователь не является водителем");
        }

        // Проверяем существование бренда
        if (!carBrandService.existsById(carDto.getBrandId())) {
            throw new ResourceNotFoundException("Бренд автомобиля не найден с ID: " + carDto.getBrandId());
        }

        // Проверяем уникальность номерного знака
        if (carRepository.existsByLicensePlate(carDto.getLicensePlate())) {
            throw new IllegalArgumentException("Автомобиль с таким номерным знаком уже существует");
        }

        carDto.setDriverId(driverId);
        carDto.setIsAvailable(true);

        Car car = carMapper.toEntity(carDto);
        Car savedCar = carRepository.save(car);

        return carMapper.toDto(savedCar);
    }

    /**
     * Обновляет существующий автомобиль из DTO
     */
    @Transactional
    public CarDto updateCar(CarDto carDto, Long driverId) {
        if (carDto.getId() == null) {
            throw new IllegalArgumentException("ID автомобиля должен быть указан для обновления");
        }

        Car existingCar = findEntityById(carDto.getId());
        User driver = userService.findById(driverId);

        if (!existingCar.getDriver().getId().equals(driver.getId())) {
            throw new UnauthorizedException("Водитель не владеет этим автомобилем");
        }

        // Проверяем уникальность номерного знака, если он изменился
        if (!existingCar.getLicensePlate().equals(carDto.getLicensePlate()) &&
                carRepository.existsByLicensePlate(carDto.getLicensePlate())) {
            throw new IllegalArgumentException("Автомобиль с таким номерным знаком уже существует");
        }

        // Сохраняем ID водителя из базы
        carDto.setDriverId(existingCar.getDriver().getId());

        carMapper.updateEntityFromDto(carDto, existingCar);
        Car updatedCar = carRepository.save(existingCar);

        return carMapper.toDto(updatedCar);
    }

    /**
     * Обновляет доступность автомобиля
     */
    @Transactional
    public CarDto updateAvailability(Long carId, boolean isAvailable, Long driverId) {
        Car car = findEntityById(carId);
        User driver = userService.findById(driverId);

        if (!car.getDriver().getId().equals(driver.getId())) {
            throw new UnauthorizedException("Водитель не владеет этим автомобилем");
        }

        car.setAvailable(isAvailable);
        Car updatedCar = carRepository.save(car);

        return carMapper.toDto(updatedCar);
    }

    /**
     * Обновляет местоположение автомобиля
     */
    @Transactional
    public CarDto updateLocation(Long carId, Double latitude, Double longitude, Long driverId) {
        Car car = findEntityById(carId);
        User driver = userService.findById(driverId);

        if (!car.getDriver().getId().equals(driver.getId())) {
            throw new UnauthorizedException("Водитель не владеет этим автомобилем");
        }

        car.setLatitude(latitude);
        car.setLongitude(longitude);
        Car updatedCar = carRepository.save(car);

        return carMapper.toDto(updatedCar);
    }

    /**
     * Удаляет автомобиль
     */
    @Transactional
    public void deleteCar(Long carId, Long driverId) {
        Car car = findEntityById(carId);
        User driver = userService.findById(driverId);

        if (!car.getDriver().getId().equals(driver.getId())) {
            throw new UnauthorizedException("Водитель не владеет этим автомобилем");
        }

        // Проверяем, нет ли активных заказов для этого автомобиля
        // (эту проверку можно добавить, когда будет реализован сервис заказов)

        carRepository.delete(car);
    }
}