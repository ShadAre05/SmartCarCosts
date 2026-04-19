package lt.teamProject.smartCarCosts.dto;
import jakarta.validation.constraints.NotBlank;

public class ReminderRequest {

    @NotBlank(message = "Reminder type is required")
    private String reminderType;

    @NotBlank(message = "End date is required")
    private String reminderDate;

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

    public String getReminderDate(){
        return reminderDate;
    }

    public void setReminderDate(String reminderDate){
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

