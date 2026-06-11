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

        model.addAttribute("visitValidationError", session.getAttribute("visitValidationError"));
        model.addAttribute("openAddVisitModal", session.getAttribute("openAddVisitModal"));
        model.addAttribute("clientNameError", session.getAttribute("clientNameError"));
        model.addAttribute("clientPhoneError", session.getAttribute("clientPhoneError"));
        model.addAttribute("serviceDateError", session.getAttribute("serviceDateError"));
        model.addAttribute("workNameError", session.getAttribute("workNameError"));
        model.addAttribute("partsCostError", session.getAttribute("partsCostError"));
        model.addAttribute("laborCostError", session.getAttribute("laborCostError"));
        model.addAttribute("car", car);
        model.addAttribute("visits", visitService.getVisitsByCarId(carId.intValue()));
        model.addAttribute("totalRepairs", visitService.getTotalRepairsByCarId(carId.intValue()));
        model.addAttribute(
                "workNameError",
                session.getAttribute("workNameError")
        );

        session.removeAttribute("clientNameError");
        session.removeAttribute("clientPhoneError");
        session.removeAttribute("serviceDateError");
        session.removeAttribute("workNameError");
        session.removeAttribute("partsCostError");
        session.removeAttribute("laborCostError");
        session.removeAttribute("openAddVisitModal");
        session.removeAttribute("workNameError");

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

        boolean hasErrors = false;

        if (clientName == null || clientName.isBlank()) {
            session.setAttribute("clientNameError", "Client name is required");
            hasErrors = true;
        }

        if (clientPhone == null || clientPhone.isBlank()) {
            session.setAttribute("clientPhoneError", "Client phone is required");
            hasErrors = true;
        } else if (!clientPhone.matches("^[+0-9 ]+$")) {
            session.setAttribute("clientPhoneError", "Phone can contain only numbers, spaces and +");
            hasErrors = true;
        }

        if (serviceDate == null || serviceDate.isBlank()) {
            session.setAttribute("serviceDateError", "Service date is required");
            hasErrors = true;
        }

        boolean hasWork = false;

        if (workNames != null) {
            for (String workName : workNames) {

                if (workName != null && !workName.isBlank()) {
                    hasWork = true;
                    break;
                }
            }
        }

        if (!hasWork) {
            session.setAttribute("workNameError", "At least one repair work is required");
            hasErrors = true;
        }

        if (hasErrors) {
            session.setAttribute("openAddVisitModal", true);
            return "redirect:/cars/" + carId + "/repairs";
        }

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

        String validationError = validateVisit(
                clientName,
                clientPhone,
                serviceDate,
                workNames,
                partsCosts,
                laborCosts
        );

        if (validationError != null) {
            session.setAttribute("visitValidationError", validationError);
            return "redirect:/cars/" + carId + "/repairs";
        }

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

    private String validateVisit(String clientName,
                                 String clientPhone,
                                 String serviceDate,
                                 List<String> workNames,
                                 List<BigDecimal> partsCosts,
                                 List<BigDecimal> laborCosts) {

        BigDecimal max = new BigDecimal("10000000");

        if (clientName == null || clientName.isBlank()) {
            return "Client name is required";
        }

        if (clientPhone == null || clientPhone.isBlank()) {
            return "Client phone is required";
        }

        if (!clientPhone.matches("^[+0-9 ]+$")) {
            return "Client phone can contain only numbers, spaces and +";
        }

        if (serviceDate == null || serviceDate.isBlank()) {
            return "Service date is required";
        }

        boolean hasWork = workNames != null &&
                workNames.stream().anyMatch(name -> name != null && !name.isBlank());

        if (!hasWork) {
            return "At least one repair work is required";
        }

        if (partsCosts != null) {
            for (BigDecimal cost : partsCosts) {
                if (cost != null && cost.compareTo(max) > 0) {
                    return "Part cost cannot be greater than 10,000,000";
                }
            }
        }

        if (laborCosts != null) {
            for (BigDecimal cost : laborCosts) {
                if (cost != null && cost.compareTo(max) > 0) {
                    return "Cost of work cannot be greater than 10,000,000";
                }
            }
        }

        return null;
    }
}