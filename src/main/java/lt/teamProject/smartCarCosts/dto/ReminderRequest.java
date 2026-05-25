package lt.teamProject.smartCarCosts.dto;

import jakarta.validation.constraints.NotNull;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalDate;

public class ReminderRequest {

    @NotNull(message = "Reminder type is required")
    private Long reminderTypeId;

    @NotNull(message = "End date is required")
    @DateTimeFormat(pattern = "yyyy.MM.dd")
    private LocalDate reminderDate;

    @NotNull(message = "Car is required")
    private Long carId;

    private boolean monthBefore;
    private boolean weekBefore;
    private boolean dayBefore;

    public Long getCarId() {
        return carId;
    }

    public ReminderRequest() {
    }

    public Long getReminderTypeId() {
        return reminderTypeId;
    }

    public void setReminderTypeId(Long reminderTypeId) {
        this.reminderTypeId = reminderTypeId;
    }

    public void setCarId(Long carId){
        this.carId = carId;
    }

    public LocalDate getReminderDate() {
        return reminderDate;
    }

    public void setReminderDate(LocalDate reminderDate) {
        this.reminderDate = reminderDate;
    }

    public boolean isMonthBefore() {
        return monthBefore;
    }

    public void setMonthBefore(boolean monthBefore) {
        this.monthBefore = monthBefore;
    }

    public boolean isWeekBefore() {
        return weekBefore;
    }

    public void setWeekBefore(boolean weekBefore) {
        this.weekBefore = weekBefore;
    }

    public boolean isDayBefore() {
        return dayBefore;
    }

    public void setDayBefore(boolean dayBefore) {
        this.dayBefore = dayBefore;
    }
}