package lt.teamProject.SmartCarCosts.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public class RegisterRequest {

    // User full name (2-30 characters)
    @NotBlank(message = "Full name is required")
    @Size(min = 2, max = 30, message = "Full name must be between 2 and 30 characters")
    private String fullName;

    // User email (must be valid format)
    @NotBlank(message = "Email is required")
    @Email(message = "Email must be valid")
    private String email;

    // User password (minimum 8 characters)
    @NotBlank(message = "Password is required")
    @Size(min = 8, message = "Password must be at least 8 characters long")
    private String password;

    // Optional user country
    private String country;

    public RegisterRequest() {
    }

    public String getFullName() {
        return fullName;
    }

    public void setFullName(String fullName){
        this.fullName = fullName;
    }

    public String getEmail(){
        return email;
    }

    public void setEmail(String email){
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password){
        this.password = password;
    }

    public String getCountry(){
        return country;
    }

    public void setCountry(String country){
        this.country = country;
    }
}
