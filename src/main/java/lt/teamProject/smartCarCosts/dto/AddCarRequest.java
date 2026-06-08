package lt.teamProject.smartCarCosts.dto;

import jakarta.validation.constraints.*;

public class AddCarRequest {

    private Long id;
    private Long brandId;

    @NotNull(message = "Model is required")
    private Long modelId;

    private String generation;

    @NotNull(message = "Year is required")
    @Min(value = 1970, message = "Year must be at least 1970")
    @Max(value = 2026, message = "Year must be at most 2026")
    private Integer year;

    @NotNull(message = "Engine capacity is required")
    @DecimalMin(value = "0.8", message = "Engine capacity must be at least 0.8")
    @DecimalMax(value = "10.0", message = "Engine capacity must be at most 10.0")
    private Double engineCapacity;

    @NotNull(message = "Fuel type is required")
    private Long fuelTypeId;

    private String licencePlate;
    private String vin;

    public Long getId() {
        return id;
    }

    public Long getBrandId() {
        return brandId;
    }

    public void setBrandId(Long brandId) {
        this.brandId = brandId;
    }

    public Long getModelId() {
        return modelId;
    }

    public void setModelId(Long modelId) {
        this.modelId = modelId;
    }

    public String getGeneration() {
        return generation;
    }

    public void setGeneration(String generation) {
        this.generation = generation;
    }

    public Integer getYear() {
        return year;
    }

    public void setYear(Integer year) {
        this.year = year;
    }

    public Double getEngineCapacity() {
        return engineCapacity;
    }

    public void setEngineCapacity(Double engineCapacity) {
        this.engineCapacity = engineCapacity;
    }

    public Long getFuelTypeId() {
        return fuelTypeId;
    }

    public void setFuelTypeId(Long fuelTypeId) {
        this.fuelTypeId = fuelTypeId;
    }

    public String getLicencePlate() {
        return licencePlate;
    }

    public void setLicencePlate(String licencePlate) {
        this.licencePlate = licencePlate;
    }

    public String getVin() {
        return vin;
    }

    public void setVin(String vin) {
        this.vin = vin;
    }
}