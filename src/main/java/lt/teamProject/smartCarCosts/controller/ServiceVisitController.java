package lt.teamProject.smartCarCosts.controller;

import jakarta.servlet.http.HttpSession;
import lt.teamProject.smartCarCosts.dto.CarDto;
import lt.teamProject.smartCarCosts.dto.ServiceVisitDto;
import lt.teamProject.smartCarCosts.dto.ServiceWorkDto;
import lt.teamProject.smartCarCosts.service.CarService;
import lt.teamProject.smartCarCosts.service.ServiceVisitService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Controller
public class ServiceVisitController {

    private final ServiceVisitService visitService;
    private final CarService carService;

    public ServiceVisitController(ServiceVisitService visitService, CarService carService) {
        this.visitService = visitService;
        this.carService = carService;
    }

    @GetMapping("/cars/{carId}/repairs")
    public String viewRepairs(@PathVariable Long carId,
                              Model model,
                              HttpSession session) {
        Long userId = (Long) session.getAttribute("userId");
        if (userId == null) return "redirect:/login";
        String role = (String) session.getAttribute("userRole");
        if (!"SERVICE".equals(role)) return "redirect:/main-interface";
        CarDto car = carService.getUserCarDtos(userId).stream()
                .filter(c -> c.getId().equals(carId))
                .findFirst().orElse(null);

        if (car == null) return "redirect:/service-main-interface";

        model.addAttribute("car", car);
        model.addAttribute("visits", visitService.getVisitsByCarId(carId.intValue()));
        model.addAttribute("totalRepairs", visitService.getTotalRepairsByCarId(carId.intValue()));
        return "car-repairs";
    }

    @PostMapping("/cars/{carId}/repairs/add")
    public String addVisit(@PathVariable Long carId,
                           @RequestParam String clientName,
                           @RequestParam String clientPhone,
                           @RequestParam(required = false) String description,
                           @RequestParam(required = false) String serviceDate,
                           @RequestParam(required = false) Boolean isPaid,
                           @RequestParam(required = false) List<String> workNames,
                           @RequestParam(required = false) List<BigDecimal> partsCosts,
                           @RequestParam(required = false) List<BigDecimal> laborCosts,
                           HttpSession session) {
        Long userId = (Long) session.getAttribute("userId");
        if (userId == null) return "redirect:/login";
        String role = (String) session.getAttribute("userRole");
        if (!"SERVICE".equals(role)) return "redirect:/main-interface";

        ServiceVisitDto dto = new ServiceVisitDto();
        dto.setClientName(clientName);
        dto.setClientPhone(clientPhone);
        dto.setDescription(description);
        dto.setIsPaid(isPaid != null ? isPaid : false);
        dto.setServiceDate(serviceDate != null && !serviceDate.isBlank() ? serviceDate : LocalDate.now().toString());

        List<ServiceWorkDto> works = new ArrayList<>();
        if (workNames != null) {
            for (int i = 0; i < workNames.size(); i++) {
                if (workNames.get(i) != null && !workNames.get(i).isBlank()) {
                    ServiceWorkDto w = new ServiceWorkDto();
                    w.setName(workNames.get(i));
                    w.setPartsCost(partsCosts != null && i < partsCosts.size() ? partsCosts.get(i) : BigDecimal.ZERO);
                    w.setLaborCost(laborCosts != null && i < laborCosts.size() ? laborCosts.get(i) : BigDecimal.ZERO);
                    works.add(w);
                }
            }
        }
        dto.setWorks(works);

        visitService.addVisit(carId.intValue(), userId.intValue(), dto);
        return "redirect:/cars/" + carId + "/repairs";
    }

    @PostMapping("/cars/{carId}/repairs/{visitId}/edit")
    public String editVisit(@PathVariable Long carId,
                            @PathVariable Integer visitId,
                            @RequestParam String clientName,
                            @RequestParam String clientPhone,
                            @RequestParam(required = false) String description,
                            @RequestParam(required = false) String serviceDate,
                            @RequestParam(required = false) Boolean isPaid,
                            @RequestParam(required = false) List<String> workNames,
                            @RequestParam(required = false) List<BigDecimal> partsCosts,
                            @RequestParam(required = false) List<BigDecimal> laborCosts,
                            HttpSession session) {
        Long userId = (Long) session.getAttribute("userId");
        if (userId == null) return "redirect:/login";
        String role = (String) session.getAttribute("userRole");
        if (!"SERVICE".equals(role)) return "redirect:/main-interface";

        ServiceVisitDto dto = new ServiceVisitDto();
        dto.setClientName(clientName);
        dto.setClientPhone(clientPhone);
        dto.setDescription(description);
        dto.setIsPaid(isPaid != null ? isPaid : false);
        dto.setServiceDate(serviceDate != null && !serviceDate.isBlank() ? serviceDate : LocalDate.now().toString());

        List<ServiceWorkDto> works = new ArrayList<>();
        if (workNames != null) {
            for (int i = 0; i < workNames.size(); i++) {
                if (workNames.get(i) != null && !workNames.get(i).isBlank()) {
                    ServiceWorkDto w = new ServiceWorkDto();
                    w.setName(workNames.get(i));
                    w.setPartsCost(partsCosts != null && i < partsCosts.size() ? partsCosts.get(i) : BigDecimal.ZERO);
                    w.setLaborCost(laborCosts != null && i < laborCosts.size() ? laborCosts.get(i) : BigDecimal.ZERO);
                    works.add(w);
                }
            }
        }
        dto.setWorks(works);

        visitService.updateVisit(visitId, dto);
        return "redirect:/cars/" + carId + "/repairs";
    }

    @PostMapping("/cars/{carId}/repairs/{visitId}/delete")
    public String deleteVisit(@PathVariable Long carId,
                              @PathVariable Integer visitId,
                              HttpSession session) {
        Long userId = (Long) session.getAttribute("userId");
        if (userId == null) return "redirect:/login";
        String role = (String) session.getAttribute("userRole");
        if (!"SERVICE".equals(role)) return "redirect:/main-interface";
        visitService.deleteVisit(visitId);
        return "redirect:/cars/" + carId + "/repairs";
    }

    @GetMapping("/cars/{carId}/repairs/{visitId}")
    public String viewSpecificRepair(@PathVariable Long carId,
                                     @PathVariable Integer visitId,
                                     Model model,
                                     HttpSession session) {
        Long userId = (Long) session.getAttribute("userId");
        if (userId == null) return "redirect:/login";

        CarDto car = carService.getUserCarDtos(userId).stream()
                .filter(c -> c.getId().equals(carId))
                .findFirst().orElse(null);

        if (car == null) return "redirect:/main-interface";

        ServiceVisitDto visit = visitService.getVisitsByCarId(carId.intValue()).stream()
                .filter(v -> v.getId().equals(visitId))
                .findFirst().orElse(null);

        model.addAttribute("car", car);
        model.addAttribute("visit", visit);
        model.addAttribute("totalRepairs", visitService.getTotalRepairsByCarId(carId.intValue()));
        return "car-repair-detail";
    }
}