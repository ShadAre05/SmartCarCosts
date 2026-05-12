package lt.teamProject.smartCarCosts.entity;

import jakarta.persistence.*;

@Entity
@Table(name = "currencies")
public class Currency {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "currency_code")
    private String currencyCode;

    @Column(name = "currency_symbol")
    private String currencySymbol;

    public Long getId() {
        return id;
    }

    public String getCurrencyCode() {
        return currencyCode;
    }

    public String getCurrencySymbol() {
        return currencySymbol;
    }
}