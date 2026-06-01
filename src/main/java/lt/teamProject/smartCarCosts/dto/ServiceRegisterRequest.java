package lt.teamProject.smartCarCosts.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import lt.teamProject.smartCarCosts.validation.ValidFullName;
import lt.teamProject.smartCarCosts.validation.ValidPassword;

public class ServiceRegisterRequest {

    @NotBlank(message = "Country is required")
    private String country;

    @ValidFullName
    private String fullName;

    @NotBlank(message = "Email is required")
    @Email(message = "Invalid email")
    @Pattern(
            regexp = "^[A-Za-z0-9+_.-]+@[A-Za-z0-9.-]+$",
            message = "Email must contain only English letters"
    )
    private String email;

    @ValidPassword
    private String password;

    public String getCountry() {
        return country;
    }

    public void setCountry(String country) {
        this.country = country;
    }

    public String getFullName() {
        return fullName;
    }

    public void setFullName(String fullName) {
        this.fullName = fullName;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }
}