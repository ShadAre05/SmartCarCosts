package lt.teamProject.smartCarCosts.dto;

public class CarDto {
    private Long id;
    private String brandName;
    private String modelName;
    private String generation;
    private Double engineCapacity;
    private String licencePlate;
    private Integer year;

    public CarDto(Long id, String brandName, String modelName, String generation, Double engineCapacity, String licencePlate, Integer year) {
        this.id = id;
        this.brandName = brandName;
        this.modelName = modelName;
        this.generation = generation;
        this.engineCapacity = engineCapacity;
        this.licencePlate = licencePlate;
        this.year = year;
    }

    public Long getId() {
        return id;
    }

    public String getBrandName() {
        return brandName;
    }

    public String getModelName() {
        return modelName;
    }

    public String getGeneration() {
        return generation;
    }

    public Double getEngineCapacity() {
        return engineCapacity;
    }

    public String getLicencePlate() {
        return licencePlate;
    }

    public Integer getYear() {
        return year;
    }
}