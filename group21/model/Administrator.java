package com.cpt202.group21.model;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;


@Entity
@Table(name = "admins")
public class Administrator {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String name;
    @Column(nullable = false)
    @NotBlank(message = "The password cannot be empty!")
    private String password;
    @Column(unique = true, nullable = false)
    @NotBlank(message = "The email cannot be empty!")
    @Email(message = "The email format is incorrect!")
    private String email;
    //0=active; 1=inactive; 2=deleted
    private Integer status; 
    

    public Administrator() {}  
    // Parameter Construction
    public Administrator(Long id, String name, String password, String email, Integer status) {
        this.id = id;
        this.name = name;
        this.password = password;
        this.email = email;
        this.status = status;
    }


    // Getters
    public Long getId() { return id; }
    public String getName() { return name; }
    public String getPassword() { return password; }
    public String getEmail() { return email; }
    public Integer getStatus() { return status; }

    
    // Setters
    public void setId(Long id) { this.id = id; }
    public void setName(String name) { this.name = name; }
    public void setPassword(String password) { this.password = password; }
    public void setEmail(String email) { this.email = email; }
    public void setStatus(Integer status) { this.status = status; }
}