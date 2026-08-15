package com.cpt202.group21.request;

import jakarta.validation.constraints.*;

public class VerificationRequest {

    @NotBlank(message = "The username cannot be empty!")
    @Email(message = "Invalid username format!")
    private String username;

    @NotBlank(message = "The password cannot be empty!")
    private String password;

    @NotBlank(message = "Verification code cannot be empty!")
    private String verificationCode;

    private String name; 

    public VerificationRequest() {}

    // Parameter constructor
    public VerificationRequest(String username, String password, String verificationCode, String name) {
        this.username = username;
        this.password = password;
        this.verificationCode = verificationCode;
        this.name = name;
    }

    // Getters
    public String getUsername() {
        return username;
    }

    public String getPassword() {
        return password;
    }

    public String getVerificationCode() {
        return verificationCode;
    }

    public String getName() {
        return name;
    }

    // Setters
    public void setUsername(String username) {
        this.username = username;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public void setVerificationCode(String verificationCode) {
        this.verificationCode = verificationCode;
    }

    public void setName(String name) {
        this.name = name;
    }
}