package lt.teamProject.smartCarCosts.controller;

// Обязательно проверь, что эти импорты есть!
import lt.teamProject.smartCarCosts.entity.Car;
import lt.teamProject.smartCarCosts.repository.CarRepository;
import lt.teamProject.smartCarCosts.repository.BrandRepository;
import lt.teamProject.smartCarCosts.repository.ModelRepository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;

@Controller
public class CarController {

    @Autowired
    private CarRepository carRepository; // Убрали final!

    @Autowired
    private BrandRepository brandRepository;

    @Autowired
    private ModelRepository modelRepository;

    @GetMapping("/add-car")
    public String showAddCarPage(Model model) {
        model.addAttribute("car", new Car());
        model.addAttribute("brands", brandRepository.findAll());
        model.addAttribute("models", modelRepository.findAll());
        return "add-car";
    }

   // @PostMapping("/add-car")
    //public String saveCar(@ModelAttribute Car car) {
    //    carRepository.save(car);
     //   return "redirect:/main.html";
    //}
}