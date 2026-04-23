package lt.teamProject.smartCarCosts.entity;

import jakarta.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "confirmation_token")
public class ConfirmationToken {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true)
    private String token;

    @Column(nullable = false)
    private String email;

    @Column(name = "created_at", nullable = false)
    private LocalDateTime createdAt;

    @Column(name = "expires_at", nullable = false)
    private LocalDateTime expiresAt;

    public ConfirmationToken() {
    }

    public ConfirmationToken(String token, String email, LocalDateTime createdAt, LocalDateTime expiresAt) {
        this.token = token;
        this.email = email;
        this.createdAt = createdAt;
        this.expiresAt = expiresAt;
    }

    public Long getId() {
        return id;
    }

    public String getToken(){
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public String getEmail(){
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public LocalDateTime getCreatedAt(){
        return createdAt;
    }

    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }

    public LocalDateTime getExpiresAt(){
        return expiresAt;
    }

    public void setExpiresAt(LocalDateTime expiresAt){
        this.expiresAt = expiresAt;
    }
}
