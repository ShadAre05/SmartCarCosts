package lt.teamProject.smartCarCosts.controller;

import jakarta.servlet.http.HttpSession;
import lt.teamProject.smartCarCosts.dto.AddCarRequest;
import lt.teamProject.smartCarCosts.entity.Car;
import lt.teamProject.smartCarCosts.service.CarService;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import jakarta.validation.Valid;
import org.springframework.validation.BindingResult;


@Controller
public class CarController {

    private final CarService carService;

    public CarController(CarService carService) {
        this.carService = carService;
    }

    @PostMapping("/my-cars/add")
    public String addCar(@Valid @ModelAttribute AddCarRequest request,
                         BindingResult bindingResult,
                         HttpSession session) {
        Long userId = (Long) session.getAttribute("userId");
        if (userId == null) return "redirect:/login";

        if (bindingResult.hasErrors()) {
            return "redirect:/main-interface?error=validation";
        }

        try {
            Car car = new Car();
            car.setModelId(request.getModelId());
            car.setYear(request.getYear());
            car.setEngineCapacity(request.getEngineCapacity());
            car.setFuelTypeId(request.getFuelTypeId());
            car.setLicencePlate(request.getLicencePlate() != null && !request.getLicencePlate().isBlank() ? request.getLicencePlate() : null);
            car.setVin(request.getVin() != null && !request.getVin().isBlank() ? request.getVin() : null);
            car.setGeneration(request.getGeneration() != null && !request.getGeneration().isBlank() ? request.getGeneration() : null);

            carService.addCar(car, userId);
            return "redirect:/main-interface?success";
        } catch (Exception e) {
            e.printStackTrace();
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

    @PostMapping("/service/cars/add")
    public String addServiceCar(@Valid @ModelAttribute AddCarRequest request,
                                BindingResult bindingResult,
                                HttpSession session) {
        Long userId = (Long) session.getAttribute("userId");
        if (userId == null) return "redirect:/login";

        if (bindingResult.hasErrors()) {
            return "redirect:/service-main-interface?error=validation";
        }

        try {
            Car car = new Car();
            car.setModelId(request.getModelId());
            car.setYear(request.getYear());
            car.setEngineCapacity(request.getEngineCapacity());
            car.setFuelTypeId(request.getFuelTypeId());
            car.setLicencePlate(request.getLicencePlate() != null && !request.getLicencePlate().isBlank() ? request.getLicencePlate() : null);
            car.setVin(request.getVin() != null && !request.getVin().isBlank() ? request.getVin() : null);
            car.setGeneration(request.getGeneration() != null && !request.getGeneration().isBlank() ? request.getGeneration() : null);

            carService.addCarForService(car, userId);
            return "redirect:/service-main-interface";
        } catch (Exception e) {
            e.printStackTrace();
            return "redirect:/service-main-interface?error=db_error";
        }
    }

    @PostMapping("/service/cars/delete")
    public String deleteServiceCar(@RequestParam Long carId, HttpSession session) {
        Long userId = (Long) session.getAttribute("userId");
        if (userId != null) {
            carService.deleteCar(carId, userId);
        }
        return "redirect:/service-main-interface";
    }

}