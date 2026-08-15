package com.cpt202.group21.model;

import java.util.ArrayList;
import java.util.List;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import jakarta.persistence.Transient;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;

@Entity
@Table(name = "users")
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;

    @Column(name = "Phone")
    private Long Phone;

    private String avatarUrl;  // New: URL of the uploaded avatar
    @Column(unique = true, nullable = false)
    @NotBlank(message = "The username cannot be empty!")
    @Email(message = "The username format is incorrect!")
    private String username;

    @Column(unique = true)
    @Email(message = "Invalid email format!")
    private String email;

    @Column(nullable = false)
    @NotBlank(message = "The password cannot be empty!")
    private String password;

    @Transient
    private String plainTextPassword;

    private boolean active = true;
    private boolean locked = false;
    private String roles = "ROLE_USER";
    private int blockedContentCount = 0;

    @OneToMany(mappedBy = "uploader", cascade = CascadeType.ALL)
    @JsonManagedReference
    private List<MusicFile> uploadedMusic = new ArrayList<>();

    public User() {}

    public User(Long id, String name, String username, String email, String password, String plainTextPassword, Long Phone,
                boolean active, boolean locked, String roles, int blockedContentCount, List<MusicFile> uploadedMusic) {
        this.id = id;
        this.name = name;
        this.username = username;
        this.email = email;
        this.password = password;
        this.plainTextPassword = plainTextPassword;
        this.active = active;
        this.locked = locked;
        this.roles = roles;
        this.blockedContentCount = blockedContentCount;
        this.uploadedMusic = uploadedMusic;
    }

    // Getters
    public Long getId() { return id; }
    public String getName() { return name; }
    public String getUsername() { return username; } 
    public Long getPhone() {return Phone;}
    public String getEmail() { return email; }
    public String getPassword() { return password; }
    public String getPlainTextPassword() { return plainTextPassword; }
    public boolean isActive() { return active; }
    public boolean isLocked() { return locked; }
    public String getRoles() { return roles; }
    public int getBlockedContentCount() { return blockedContentCount; }
    public List<MusicFile> getUploadedMusic() { return uploadedMusic; }
    public String getAvatarUrl() {
        return avatarUrl;
    }
    // Setters
    public void setId(Long id) { this.id = id; }
    public void setPhone(Long Phone) { this.Phone = Phone; }
    public void setName(String name) { this.name = name; }
    public void setUsername(String username) { this.username = username; }
    public void setEmail(String email) { this.email = email; }
    public void setPassword(String password) { this.password = password; }
    public void setPlainTextPassword(String plainTextPassword) { this.plainTextPassword = plainTextPassword; }
    public void setActive(boolean active) { this.active = active; }
    public void setLocked(boolean locked) { this.locked = locked; }
    public void setRoles(String roles) { this.roles = roles; }
    public void setBlockedContentCount(int blockedContentCount) { this.blockedContentCount = blockedContentCount; }
    public void setUploadedMusic(List<MusicFile> uploadedMusic) { this.uploadedMusic = uploadedMusic; }
    public void setAvatarUrl(String avatarUrl) {
        this.avatarUrl = avatarUrl;
    }
    // Lock logic
    public void incrementBlockedContentCount() {
        this.blockedContentCount++;
        if (this.blockedContentCount >= 3) {
            this.locked = true;
        }
    }
}