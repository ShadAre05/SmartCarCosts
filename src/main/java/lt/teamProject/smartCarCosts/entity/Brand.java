package lt.teamProject.smartCarCosts.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

@Entity
@Table(name = "brands") // Убедись, что таблица в Supabase называется именно так
public class Brand {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;

    // Обязательно сгенерируй Getters и Setters!
    // В IntelliJ IDEA: нажми Alt+Insert (или Cmd+N на Mac) -> Getter and Setter -> выдели всё -> OK

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}