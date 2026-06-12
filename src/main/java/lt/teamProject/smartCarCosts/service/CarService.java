package lt.teamProject.smartCarCosts.service;

import lt.teamProject.smartCarCosts.dto.CarDto;
import lt.teamProject.smartCarCosts.entity.Car;
import lt.teamProject.smartCarCosts.entity.CarBrand;
import lt.teamProject.smartCarCosts.entity.CarModel;
import lt.teamProject.smartCarCosts.entity.UserCar;
import lt.teamProject.smartCarCosts.repository.*;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class CarService {

    private final ExpenseRepository expenseRepository;
    private final CarRepository carRepository;
    private final UserCarRepository userCarRepository;
    private final CarModelRepository carModelRepository;
    private final CarBrandRepository carBrandRepository;
    private final ReminderRepository reminderRepository;

    public CarService(CarRepository carRepository, UserCarRepository userCarRepository,
                      CarModelRepository carModelRepository, CarBrandRepository carBrandRepository,
                      ExpenseRepository expenseRepository, ReminderRepository reminderRepository) {
        this.carRepository = carRepository;
        this.userCarRepository = userCarRepository;
        this.carModelRepository = carModelRepository;
        this.carBrandRepository = carBrandRepository;
        this.expenseRepository = expenseRepository;
        this.reminderRepository = reminderRepository;
    }


    public List<Car> getUserCars(Long userId) {
        List<UserCar> links = userCarRepository.findByUserId(userId);
        return links.stream()
                .map(link -> carRepository.findById(link.getCarId()).orElse(null))
                .filter(car -> car != null)
                .collect(Collectors.toList());
    }

    public List<CarDto> getUserCarDtos(Long userId) {
        List<UserCar> links = userCarRepository.findByUserId(userId);
        return links.stream()
                .map(link -> carRepository.findById(link.getCarId()).orElse(null))
                .filter(car -> car != null)
                .map(car -> {
                    CarModel model = carModelRepository.findById(car.getModelId()).orElse(null);
                    String modelName = model != null ? model.getModelName() : "Unknown";
                    String brandName = "Unknown";
                    if (model != null) {
                        CarBrand brand = carBrandRepository.findById(model.getBrandId()).orElse(null);
                        brandName = brand != null ? brand.getName() : "Unknown";
                    }
                    return new CarDto(car.getId(), brandName, modelName, car.getGeneration(),
                            car.getEngineCapacity(), car.getLicencePlate(), car.getYear());
                })
                .collect(Collectors.toList());
    }

    public void addCar(Car car, Long userId) {
        long count = userCarRepository.countByUserId(userId);
        if (count >= 3) {
            throw new RuntimeException("MAX 3 CARS");
        }
        Car savedCar = carRepository.save(car);
        UserCar link = new UserCar();
        link.setUserId(userId);
        link.setCarId(savedCar.getId());
        userCarRepository.save(link);
    }

    @Transactional
    public void deleteCar(Long carId, Long userId) {
        userCarRepository.findByUserId(userId)
                .stream()
                .filter(link -> link.getCarId().equals(carId))
                .findFirst()
                .ifPresent(userCar -> {
                    reminderRepository.deleteByUserCarIdIn(List.of(userCar.getId()));
                    expenseRepository.deleteByUserCarId(userCar.getId());
                    userCarRepository.delete(userCar);
                    carRepository.deleteById(carId);
                });
    }

    public void addCarForService(Car car, Long userId) {
        Car savedCar = carRepository.save(car);
        UserCar link = new UserCar();
        link.setUserId(userId);
        link.setCarId(savedCar.getId());
        userCarRepository.save(link);
    }


}