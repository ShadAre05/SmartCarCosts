package lt.teamProject.smartCarCosts.service;

import lt.teamProject.smartCarCosts.entity.Car;
import lt.teamProject.smartCarCosts.entity.UserCar;
import lt.teamProject.smartCarCosts.repository.CarRepository;
import lt.teamProject.smartCarCosts.repository.UserCarRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class CarService {

    private final CarRepository carRepository;
    private final UserCarRepository userCarRepository;

    public CarService(CarRepository carRepository, UserCarRepository userCarRepository){
        this.carRepository = carRepository;
        this.userCarRepository = userCarRepository;
    }

    // get the user's machines
    public List<Car> getUserCars(Long userId) {
        List<UserCar> links = userCarRepository.findByUserId(userId);

        return links.stream()
                .map(link -> carRepository.findById(link.getCarId()).orElse(null))
                .filter(car -> car != null)
                .collect(Collectors.toList());
    }

    //add a car (limit 3)
    public void addCar(Car car, Long userId) {
        long count = userCarRepository.countByUserId(userId);

        if (count >= 3){
            throw new RuntimeException("MAX 3 CARS");
        }

        // save car
        Car savedCar = carRepository.save(car);

        // connect to the user
        UserCar link = new UserCar();
        link.setUserId(userId);
        link.setCarId(savedCar.getId());

        userCarRepository.save(link);
    }

    // delete car
    public void deleteCar(Long carId, Long userId) {
        userCarRepository.findByUserId(userId)
                .stream()
                .filter(link -> link.getCarId().equals(carId))
                .findFirst()
                .ifPresent(userCarRepository::delete);

        carRepository.deleteById(carId);
    }
}
