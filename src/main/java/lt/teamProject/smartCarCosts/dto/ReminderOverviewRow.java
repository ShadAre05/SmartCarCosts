package lt.teamProject.smartCarCosts.dto;

import java.time.LocalDate;
import java.util.List;

public class ReminderOverviewRow {

    private List<Long> ids;
    private String reminderType;
    private LocalDate expiresOn;
    private boolean monthBefore;
    private boolean weekBefore;
    private boolean dayBefore;

    public ReminderOverviewRow(List<Long> ids,
                               String reminderType,
                               LocalDate expiresOn,
                               boolean monthBefore,
                               boolean weekBefore,
                               boolean dayBefore) {
        this.ids = ids;
        this.reminderType = reminderType;
        this.expiresOn = expiresOn;
        this.monthBefore = monthBefore;
        this.weekBefore = weekBefore;
        this.dayBefore = dayBefore;
    }

    public List<Long> getIds() {
        return ids;
    }

    public Long getMainId() {
        return ids != null && !ids.isEmpty() ? ids.get(0) : null;
    }

    public String getReminderType() {
        return reminderType;
    }

    public LocalDate getExpiresOn() {
        return expiresOn;
    }

    public boolean isMonthBefore() {
        return monthBefore;
    }

    public boolean isWeekBefore() {
        return weekBefore;
    }

    public boolean isDayBefore() {
        return dayBefore;
    }
}