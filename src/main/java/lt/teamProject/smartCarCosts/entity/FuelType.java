package lt.teamProject.smartCarCosts.entity;

import jakarta.persistence.*;

@Entity
@Table(name = "fuel_types")
public class FuelType {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "fuel_type_name")
    private String fuelTypeName;

    public Long getId() {
        return id;
    }

    public String getFuelTypeName() {
        return fuelTypeName;
    }
}