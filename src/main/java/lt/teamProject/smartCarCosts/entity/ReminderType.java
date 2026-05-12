package lt.teamProject.smartCarCosts.entity;

import jakarta.persistence.*;

@Entity
@Table(name = "reminder_types")
public class ReminderType {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "reminder_name", nullable = false)
    private String name;

    public ReminderType(){
    }

    public Long getId(){
        return id;
    }

    public String getName(){
        return name;
    }

    public void setName(String name){
        this.name = name;
    }
}
