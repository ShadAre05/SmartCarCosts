package lt.teamProject.smartCarCosts.dto;

public class AddCarRequest {

    private Long brandId;
    private Long modelId;
    private Integer year;
    private Double engineCapacity;
    private String generation;
    private String fuelType;
    private String licensePlate;
    private String vin;


    public Long getBrandId() { return brandId; }
    public void setBrandId(Long brandId) { this.brandId = brandId; }

    public Long getModelId() { return modelId; }
    public void setModelId(Long modelId) { this.modelId = modelId; }

    public Integer getYear() { return year; }
    public void setYear(Integer year) { this.year = year; }

    public Double getEngineCapacity() { return engineCapacity; }
    public void setEngineCapacity(Double engineCapacity) { this.engineCapacity = engineCapacity; }

    public String getGeneration() { return generation; }
    public void setGeneration(String generation) { this.generation = generation; }

    public String getFuelType() { return fuelType; }
    public void setFuelType(String fuelType) { this.fuelType = fuelType; }

    public String getLicensePlate() { return licensePlate; }
    public void setLicensePlate(String licensePlate) { this.licensePlate = licensePlate; }

    public String getVin() { return vin; }
    public void setVin(String vin) { this.vin = vin; }
}