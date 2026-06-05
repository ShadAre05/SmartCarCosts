package lt.teamProject.smartCarCosts.dto;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

public class ServiceVisitDto {
    private Integer id;
    private String clientName;
    private String clientPhone;
    private String description;
    private Boolean isPaid;
    private BigDecimal totalCost;
    private String serviceDate;
    private List<ServiceWorkDto> works;

    public ServiceVisitDto() {}

    public Integer getId() { return id; }
    public void setId(Integer id) { this.id = id; }
    public String getClientName() { return clientName; }
    public void setClientName(String clientName) { this.clientName = clientName; }
    public String getClientPhone() { return clientPhone; }
    public void setClientPhone(String clientPhone) { this.clientPhone = clientPhone; }
    public String getDescription() { return description; }
    public void setDescription(String description) { this.description = description; }
    public Boolean getIsPaid() { return isPaid; }
    public void setIsPaid(Boolean isPaid) { this.isPaid = isPaid; }
    public BigDecimal getTotalCost() { return totalCost; }
    public void setTotalCost(BigDecimal totalCost) { this.totalCost = totalCost; }
    public String getServiceDate() { return serviceDate; }
    public void setServiceDate(String serviceDate) { this.serviceDate = serviceDate; }
    public List<ServiceWorkDto> getWorks() { return works; }
    public void setWorks(List<ServiceWorkDto> works) { this.works = works; }
}