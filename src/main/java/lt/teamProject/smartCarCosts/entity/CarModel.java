package lt.teamProject.smartCarCosts.entity;

import jakarta.persistence.*;

@Entity
@Table(name = "car_models")
public class CarModel {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "brand_id")
    private Long brandId;

    @Column(name = "model_name")
    private String modelName;

    public Long getId() {
        return id;
    }

    public Long getBrandId() {
        return brandId;
    }

    public String getModelName() {
        return modelName;
    }
}