package lt.teamProject.smartCarCosts.entity;


import jakarta.persistence.*;

@Entity
@Table(name = "expense_categories")
public class ExpenseCategory {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "name", nullable = false)
    private String name;

    public ExpenseCategory(){
    }

    public Long getId(){
        return id;
    }

    public String getName(){
        return name;
    }
}
