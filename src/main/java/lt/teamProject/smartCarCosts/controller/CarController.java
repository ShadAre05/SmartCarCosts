package lt.teamProject.smartCarCosts.controller;

import jakarta.servlet.http.HttpSession;
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
    public String addCar(@ModelAttribute AddCarRequest request, HttpSession session) {
        // Retrieve the actual user ID from the session
        Long userId = (Long) session.getAttribute("userId");
        if (userId == null) {
            return "redirect:/login"; // Redirect to login page if the user is not authenticated
        }

        try {
            Car car = new Car();
            car.setModelId(request.getModelId());
            car.setYear(request.getYear());
            car.setEngineCapacity(request.getEngineCapacity());
            car.setFuelTypeId(request.getFuelTypeId());
            car.setLicencePlate(request.getLicencePlate());
            car.setVin(request.getVin());
            car.setGeneration(request.getGeneration());

            // Saves user's car
            carService.addCar(car, userId);

            return "redirect:/main-interface?success";
        } catch (Exception e) {
            System.out.println("Add Car Error: " + e.getMessage());
            return "redirect:/main-interface?error=db_error";
        }
    }

    @PostMapping("/my-cars/delete")
    public String deleteCar(@RequestParam Long carId, HttpSession session) {
        Long userId = (Long) session.getAttribute("userId");
        if (userId != null) {
            carService.deleteCar(carId, userId);
        }
        return "redirect:/main-interface";
    }
}