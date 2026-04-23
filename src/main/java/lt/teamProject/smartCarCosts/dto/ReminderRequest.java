package lt.teamProject.smartCarCosts.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalDate;

public class ReminderRequest {

    @NotBlank(message = "Reminder type is required")
    private String reminderType;

    @NotNull(message = "End date is required")
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private LocalDate reminderDate;

    private boolean monthBefore;
    private boolean weekBefore;
    private boolean dayBefore;

    public ReminderRequest(){
    }

    public String getReminderType(){
        return reminderType;
    }

    public void setReminderType(String reminderType){
        this.reminderType = reminderType;
    }

    public LocalDate getReminderDate() {
        return reminderDate;
    }

    public void setReminderDate(LocalDate reminderDate) {
        this.reminderDate = reminderDate;
    }

    public boolean isMonthBefore(){
        return monthBefore;
    }

    public void setMonthBefore(boolean monthBefore){
        this.monthBefore = monthBefore;
    }

    public boolean isWeekBefore(){
        return weekBefore;
    }

    public void setWeekBefore(boolean weekBefore){
        this.weekBefore = weekBefore;
    }

    public boolean isDayBefore() {
        return dayBefore;
    }

    public void setDayBefore(boolean dayBefore){
        this.dayBefore = dayBefore;
    }
}

