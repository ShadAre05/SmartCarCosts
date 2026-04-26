package lt.teamProject.smartCarCosts.entity;


import jakarta.persistence.*;

@Entity
@Table(name = "user_cars")
public class UserCar {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "user_id")
    private Long userId;

    @Column(name = "car_id")
    private Long carId;

    public Long getId() {
        return id;
    }

    public Long getUserId(){
        return userId;
    }

    public Long getCarId(){
        return carId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public void setCarId(Long carId) {
        this.carId = carId;
    }
}
