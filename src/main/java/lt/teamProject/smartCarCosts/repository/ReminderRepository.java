package lt.teamProject.smartCarCosts.repository;

import lt.teamProject.smartCarCosts.entity.Reminder;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDate;
import java.util.List;

public interface ReminderRepository extends JpaRepository<Reminder, Long> {
    void deleteByUserCarIdIn(List<Long> userCarIds);
    List<Reminder> findByRemindAtAndActiveTrue(LocalDate remindAt);
    List<Reminder> findByUserCarIdInAndActiveTrueOrderByRemindAtAsc(List<Long> userCarIds);
    void deleteById(Long id);
}
