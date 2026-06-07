package lt.teamProject.smartCarCosts.service;

import lt.teamProject.smartCarCosts.dto.ServiceVisitDto;
import lt.teamProject.smartCarCosts.dto.ServiceWorkDto;
import lt.teamProject.smartCarCosts.entity.ServiceVisit;
import lt.teamProject.smartCarCosts.entity.ServiceWork;
import lt.teamProject.smartCarCosts.repository.ServiceVisitRepository;
import lt.teamProject.smartCarCosts.repository.ServiceWorkRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class ServiceVisitService {

    private final ServiceVisitRepository visitRepository;
    private final ServiceWorkRepository workRepository;

    public ServiceVisitService(ServiceVisitRepository visitRepository,
                               ServiceWorkRepository workRepository) {
        this.visitRepository = visitRepository;
        this.workRepository = workRepository;
    }

    public List<ServiceVisitDto> getVisitsByCarId(Integer carId) {
        return visitRepository.findByCarId(carId).stream()
                .map(visit -> {
                    ServiceVisitDto dto = new ServiceVisitDto();
                    dto.setId(visit.getId());
                    dto.setClientName(visit.getClientName());
                    dto.setClientPhone(visit.getClientPhone());
                    dto.setDescription(visit.getDescription());
                    dto.setIsPaid(visit.getIsPaid());
                    dto.setTotalCost(visit.getTotalCost());
                    dto.setServiceDate(visit.getServiceDate() != null ? visit.getServiceDate().toString() : null);
                    dto.setWorks(getWorksByVisitId(visit.getId()));
                    return dto;
                })
                .collect(Collectors.toList());
    }

    public List<ServiceWorkDto> getWorksByVisitId(Integer visitId) {
        return workRepository.findByVisitId(visitId).stream()
                .map(work -> {
                    ServiceWorkDto dto = new ServiceWorkDto();
                    dto.setId(work.getId());
                    dto.setName(work.getName());
                    dto.setPartsCost(work.getPartsCost());
                    dto.setLaborCost(work.getLaborCost());
                    dto.setTotalCost(work.getTotalCost());
                    return dto;
                })
                .collect(Collectors.toList());
    }

    @Transactional
    public void addVisit(Integer carId, Integer userId, ServiceVisitDto dto) {
        ServiceVisit visit = new ServiceVisit();
        visit.setCarId(carId);
        visit.setServiceUserId(userId);
        visit.setClientName(dto.getClientName());
        visit.setClientPhone(dto.getClientPhone());
        visit.setDescription(dto.getDescription());
        visit.setIsPaid(dto.getIsPaid() != null ? dto.getIsPaid() : false);
        visit.setServiceDate(dto.getServiceDate() != null && !dto.getServiceDate().isBlank()
                ? LocalDate.parse(dto.getServiceDate()) : LocalDate.now());
        visit.setCreatedAt(LocalDateTime.now());

        BigDecimal total = BigDecimal.ZERO;
        if (dto.getWorks() != null) {
            for (ServiceWorkDto w : dto.getWorks()) {
                BigDecimal parts = w.getPartsCost() != null ? w.getPartsCost() : BigDecimal.ZERO;
                BigDecimal labor = w.getLaborCost() != null ? w.getLaborCost() : BigDecimal.ZERO;
                total = total.add(parts).add(labor);
            }
        }
        visit.setTotalCost(total);
        ServiceVisit saved = visitRepository.save(visit);

        if (dto.getWorks() != null) {
            for (ServiceWorkDto w : dto.getWorks()) {
                ServiceWork work = new ServiceWork();
                work.setVisitId(saved.getId());
                work.setName(w.getName() != null ? w.getName() : "");
                BigDecimal parts = w.getPartsCost() != null ? w.getPartsCost() : BigDecimal.ZERO;
                BigDecimal labor = w.getLaborCost() != null ? w.getLaborCost() : BigDecimal.ZERO;
                work.setPartsCost(parts);
                work.setLaborCost(labor);
                work.setTotalCost(parts.add(labor));
                work.setCreatedAt(LocalDateTime.now());
                workRepository.save(work);
            }
        }
    }

    @Transactional
    public void updateVisit(Integer visitId, ServiceVisitDto dto) {
        ServiceVisit visit = visitRepository.findById(visitId).orElseThrow();
        visit.setClientName(dto.getClientName());
        visit.setClientPhone(dto.getClientPhone());
        visit.setDescription(dto.getDescription());
        visit.setIsPaid(dto.getIsPaid() != null ? dto.getIsPaid() : false);
        visit.setServiceDate(dto.getServiceDate() != null && !dto.getServiceDate().isBlank()
                ? LocalDate.parse(dto.getServiceDate()) : LocalDate.now());

        workRepository.deleteByVisitId(visitId);

        BigDecimal total = BigDecimal.ZERO;
        if (dto.getWorks() != null) {
            for (ServiceWorkDto w : dto.getWorks()) {
                ServiceWork work = new ServiceWork();
                work.setVisitId(visitId);
                work.setName(w.getName() != null ? w.getName() : "");
                BigDecimal parts = w.getPartsCost() != null ? w.getPartsCost() : BigDecimal.ZERO;
                BigDecimal labor = w.getLaborCost() != null ? w.getLaborCost() : BigDecimal.ZERO;
                work.setPartsCost(parts);
                work.setLaborCost(labor);
                work.setTotalCost(parts.add(labor));
                work.setCreatedAt(LocalDateTime.now());
                workRepository.save(work);
                total = total.add(parts).add(labor);
            }
        }
        visit.setTotalCost(total);
        visitRepository.save(visit);
    }

    @Transactional
    public void deleteVisit(Integer visitId) {
        workRepository.deleteByVisitId(visitId);
        visitRepository.deleteById(visitId);
    }

    public int getTotalRepairsByCarId(Integer carId) {
        return visitRepository.findByCarId(carId).size();
    }
}