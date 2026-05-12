package lt.teamProject.smartCarCosts.controller;

import lt.teamProject.smartCarCosts.dto.AddCarRequest;
import lt.teamProject.smartCarCosts.entity.Car;
import lt.teamProject.smartCarCosts.service.CarService;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
public class CarController {

    private final CarService carService;

    public CarController(CarService carService) {
        this.carService = carService;
    }

    @PostMapping("/my-cars/add")
    public String addCar(@ModelAttribute AddCarRequest addCarRequest) {
        Car car = new Car();
        car.setModelId(addCarRequest.getModelId());
        car.setYear(addCarRequest.getYear());
        car.setEngineCapacity(addCarRequest.getEngineCapacity());
        car.setLicencePlate(addCarRequest.getLicencePlate());
        car.setVin(addCarRequest.getVin());
        car.setGeneration(addCarRequest.getGeneration());

        Long userId = 1L;
        carService.addCar(car, userId);

        return "redirect:/main-interface";
    }

    @PostMapping("/my-cars/delete")
    public String deleteCar(@RequestParam Long carId) {
        Long userId = 1L;
        carService.deleteCar(carId, userId);

        return "redirect:/main-interface";
    }
}