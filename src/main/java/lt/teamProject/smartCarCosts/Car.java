@Entity
@Table(name = "cars")
public class Car {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    private String vin;
    private Integer year;
    private Double engineCapacity;
    private String fuelType;
    private String licensePlate;
    
    // Add Getters and Setters here
}
