

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;

@Controller
public class CarController {

    // These connect your Java code to your PostgreSQL database
    @Autowired
    private CarRepository carRepository;
    
    @Autowired
    private BrandRepository brandRepository; 
    
    @Autowired
    private ModelRepository modelRepository; 

    // 1. GET Request: This happens when the user clicks the link to open the page
    @GetMapping("/add-car")
    public String showAddCarPage(Model model) {
        
        // Creates an empty car object to hold the form data
        model.addAttribute("car", new Car());
        
        // Fetches ALL brands and models from the database and sends them to the Thymeleaf dropdowns
        model.addAttribute("brands", brandRepository.findAll());
        model.addAttribute("models", modelRepository.findAll());
        
        // Tells Spring Boot to look for 'add-car.html' in the templates folder
        return "add-car"; 
    }

    // 2. POST Request: This happens when the user clicks the green "Add" button
    @PostMapping("/add-car")
    public String saveCar(@ModelAttribute Car car) {
        
        // Takes the filled-out car object and saves it into the PostgreSQL database
        carRepository.save(car);
        
        // Reconnects to the Main Page, exactly like your Team Lead asked!
        return "redirect:/main.html"; 
    }
}
