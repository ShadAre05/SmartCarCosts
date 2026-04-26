package lt.teamProject.smartCarCosts.entity;


import jakarta.persistence.*;

@Entity
@Table(name = "cars")
public class Car {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "model_id")
    private Long modelId;

    private Integer year;

    @Column(name = "engine_capacity")
    private Double engineCapacity;

    @Column(name = "licence_plate")
    private String licencePlate;

    private String vin;

    private String generation;

    public Long getId(){
        return id;
    }

    public Long getModelId(){
        return modelId;
    }

    public Integer getYear() {
        return year;
    }

    public Double getEngineCapacity() {
        return engineCapacity;
    }

    public String getLicencePlate() {
        return licencePlate;
    }

    public String getVin() {
        return vin;
    }

    public String getGeneration() {
        return generation;
    }

    public void setModelId(Long modelId) {
        this.modelId = modelId;
    }

    public void setYear(Integer year){
        this.year = year;
    }

    public void setEngineCapacity(Double engineCapacity){
        this.engineCapacity = engineCapacity;
    }

    public void setLicencePlate(String licencePlate){
        this.licencePlate = licencePlate;
    }

    public void setVin(String vin){
        this.vin = vin;
    }

    public void setGeneration(String generation) {
        this.generation = generation;
    }
}
