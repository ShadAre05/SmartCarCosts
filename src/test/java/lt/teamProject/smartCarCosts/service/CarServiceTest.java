package lt.teamProject.smartCarCosts.service;

import lt.teamProject.smartCarCosts.entity.Car;
import lt.teamProject.smartCarCosts.entity.UserCar;
import lt.teamProject.smartCarCosts.repository.CarRepository;
import lt.teamProject.smartCarCosts.repository.UserCarRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.test.util.ReflectionTestUtils;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class CarServiceTest {

    @Mock
    private CarRepository carRepository;

    @Mock
    private UserCarRepository userCarRepository;

    @InjectMocks
    private CarService carService;

    @Test
    void addCar_WhenLimitNotReached_ShouldSaveCarAndLink() {
        Long userId = 1L;
        Car carToSave = new Car();

        Car savedCar = new Car();
        ReflectionTestUtils.setField(savedCar, "id", 100L);

        when(userCarRepository.countByUserId(userId)).thenReturn(0L);
        when(carRepository.save(carToSave)).thenReturn(savedCar);

        carService.addCar(carToSave, userId);

        verify(carRepository, times(1)).save(carToSave);
        verify(userCarRepository, times(1)).save(any(UserCar.class));
    }

    @Test
    void addCar_WhenLimitReached_ShouldThrowException() {
        Long userId = 1L;
        Car car = new Car();

        when(userCarRepository.countByUserId(userId)).thenReturn(3L);

        RuntimeException exception = assertThrows(RuntimeException.class, () -> {
            carService.addCar(car, userId);
        });

        assertEquals("MAX 3 CARS", exception.getMessage());

        verify(carRepository, never()).save(any(Car.class));
        verify(userCarRepository, never()).save(any(UserCar.class));
    }

    @Test
    void getUserCars_ShouldReturnOnlyExistingCars() {
        Long userId = 1L;

        UserCar link1 = new UserCar();
        link1.setCarId(10L);

        UserCar link2 = new UserCar();
        link2.setCarId(20L);

        Car car1 = new Car();
        ReflectionTestUtils.setField(car1, "id", 10L);

        when(userCarRepository.findByUserId(userId)).thenReturn(List.of(link1, link2));
        when(carRepository.findById(10L)).thenReturn(Optional.of(car1));
        when(carRepository.findById(20L)).thenReturn(Optional.empty());

        List<Car> result = carService.getUserCars(userId);

        assertEquals(1, result.size(), "Should return only 1 car");
        assertEquals(10L, result.get(0).getId());
    }

    @Test
    void deleteCar_ShouldDeleteLinkAndCar() {
        Long userId = 1L;
        Long carId = 99L;
        UserCar link = new UserCar();
        link.setCarId(carId);

        when(userCarRepository.findByUserId(userId)).thenReturn(List.of(link));

        carService.deleteCar(carId, userId);

        verify(userCarRepository, times(1)).delete(link);
        verify(carRepository, times(1)).deleteById(carId);
    }
}