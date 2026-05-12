package lt.teamProject.smartCarCosts.entity;

import jakarta.persistence.*;

@Entity
@Table(name = "roles")
public class Role {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true)
    private String role;

    public Role() {
    }

    public Long getId() {
        return id;
    }

    public String getRole() {
        return role;
    }
}