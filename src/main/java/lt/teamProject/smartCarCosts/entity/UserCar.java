package lt.teamProject.smartCarCosts.entity;

import jakarta.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "user_cars")
public class UserCar {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "user_id", nullable = false)
    private Long userId;

    @Column(name = "car_id", nullable = false)
    private Long carId;

    @Column(name = "created_at", nullable = false, updatable = false)
    private LocalDateTime createdAt;

    @PrePersist
    public void prePersist() {
        this.createdAt = LocalDateTime.now();
    }

    public Long getId() {
        return id;
    }

    public Long getUserId(){
        return userId;
    }

    public Long getCarId(){
        return carId;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public void setCarId(Long carId) {
        this.carId = carId;
    }
}
