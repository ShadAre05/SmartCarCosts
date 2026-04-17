@Controller
public class CarController {

    @Autowired
    private CarRepository carRepository;
    @Autowired
    private BrandRepository brandRepository; // You'll need a repository for the Brands table
    @Autowired
    private ModelRepository modelRepository; // You'll need a repository for the Models table

    @GetMapping("/add-car")
    public String showAddCarPage(Model model) {
        // 1. Send a blank car object for the form to fill out
        model.addAttribute("car", new Car());
        
        // 2. Fetch the lists from the Database and send them to the HTML dropdowns
        model.addAttribute("brands", brandRepository.findAll());
        model.addAttribute("models", modelRepository.findAll());
        
        // 3. Return the name of the HTML file in the 'templates' folder
        return "add-car"; 
    }

    @PostMapping("/add-car")
    public String saveCar(@ModelAttribute Car car) {
        // 4. Save the completed car object to the database
        carRepository.save(car);
        
        // 5. Reconnect to the main page as requested by the team lead
        return "redirect:/main.html"; 
    }
}
