package lt.teamProject.smartCarCosts.entity;

import jakarta.persistence.*;

import java.time.LocalDate;

@Entity
@Table(name = "reminders")
public class Reminder {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "user_car_id", nullable = false)
    private Long userCarId;

    @Column(name = "reminder_type_id", nullable = false)
    private Long reminderTypeId;

    @Column(name = "remind_at", nullable = false)
    private LocalDate remindAt;

    @Column(name = "notify_before_days", nullable = false)
    private Integer notifyBeforeDays;

    @Column(name = "is_active", nullable = false)
    private Boolean active = true;

    public Long getId() {
        return id;
    }

    public Long getUserCarId() { return userCarId; }
    public void setUserCarId(Long userCarId) { this.userCarId = userCarId; }

    public Long getReminderTypeId() { return reminderTypeId; }
    public void setReminderTypeId(Long reminderTypeId) { this.reminderTypeId = reminderTypeId; }

    public LocalDate getRemindAt() { return remindAt; }
    public void setRemindAt(LocalDate remindAt) { this.remindAt = remindAt; }

    public Integer getNotifyBeforeDays() { return notifyBeforeDays; }
    public void setNotifyBeforeDays(Integer notifyBeforeDays) { this.notifyBeforeDays = notifyBeforeDays; }

    public Boolean getActive() { return active; }
    public void setActive(Boolean active) { this.active = active; }
}
