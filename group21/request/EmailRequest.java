package com.cpt202.group21.request;
import jakarta.validation.constraints.*;


public class EmailRequest {
    @NotBlank(message = "Email cannot be empty!")
    @Email(message = "Invalid email format!")
    private String email;


    public EmailRequest() {}
    // Parameter Construction
    public EmailRequest(String email){
        this.email = email;
    }

    
    // Getter and Setter
    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }
}

