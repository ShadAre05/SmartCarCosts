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

    ////Wrong methods, didn't see in web
    @PostMapping("/my-cars/add")
    public String addCar(@ModelAttribute AddCarRequest request) {
        try {
            Car car = new Car();
            car.setModelId(request.getModelId());
            car.setYear(request.getYear());
            car.setEngineCapacity(request.getEngineCapacity());
            car.setFuelTypeId(request.getFuelTypeId());
            car.setLicencePlate(request.getLicencePlate());
            car.setVin(request.getVin());
            car.setGeneration(request.getGeneration());

            Long userId = 1L;
            carService.addCar(car, userId);

            return "redirect:/main-interface?success";
        } catch (Exception e) {
            System.out.println("Add Car Error: " + e.getMessage());
            return "redirect:/main-interface?error=db_error";
        }
    }

    ////I cant even delete, because add-car incorrect
    @PostMapping("/my-cars/delete")
    public String deleteCar(@RequestParam Long carId) {
        Long userId = 1L;
        carService.deleteCar(carId, userId);

        return "redirect:/main-interface";
    }
}