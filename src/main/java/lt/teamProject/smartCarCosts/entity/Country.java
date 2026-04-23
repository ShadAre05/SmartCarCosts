package lt.teamProject.smartCarCosts.entity;

import jakarta.persistence.*;

@Entity
@Table(name = "country")
public class Country {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "country_name")
    private String countryName;

    public Country() {
    }

    public Long getId(){
        return id;
    }

    public String getCountryName(){
        return countryName;
    }

    public void setCountryName(String countryName) {
        this.countryName = countryName;
    }
}
