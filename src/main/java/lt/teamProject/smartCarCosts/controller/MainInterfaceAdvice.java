package lt.teamProject.smartCarCosts.controller;

import lt.teamProject.smartCarCosts.repository.CarBrandRepository;
import lt.teamProject.smartCarCosts.repository.CarModelRepository;
import lt.teamProject.smartCarCosts.repository.CurrencyRepository;
import lt.teamProject.smartCarCosts.repository.FuelTypeRepository;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ModelAttribute;

// This class works only for an AuthController

//For what we need this class??????
@ControllerAdvice(assignableTypes = AuthController.class)
public class MainInterfaceAdvice {

    private final CarBrandRepository carBrandRepository;
    private final CarModelRepository carModelRepository;
    private final FuelTypeRepository fuelTypeRepository;
    private final CurrencyRepository currencyRepository;

    public MainInterfaceAdvice(CarBrandRepository carBrandRepository,
                               CarModelRepository carModelRepository,
                               FuelTypeRepository fuelTypeRepository,
                               CurrencyRepository currencyRepository) {
        this.carBrandRepository = carBrandRepository;
        this.carModelRepository = carModelRepository;
        this.fuelTypeRepository = fuelTypeRepository;
        this.currencyRepository = currencyRepository;
    }

    // Spring automatically calls this method to HTML
    @ModelAttribute
    public void addGlobalAttributes(Model model) {
        model.addAttribute("brands", carBrandRepository.findAll());
        model.addAttribute("models", carModelRepository.findAll());
        model.addAttribute("fuelTypes", fuelTypeRepository.findAll());
        model.addAttribute("currencies", currencyRepository.findAll());
    }
}