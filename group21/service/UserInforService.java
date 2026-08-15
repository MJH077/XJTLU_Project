package com.cpt202.group21.service;
import com.cpt202.group21.exception.DuplicateEmailException;
import com.cpt202.group21.exception.InvalidPasswordException;
import com.cpt202.group21.model.User;
import com.cpt202.group21.repository.UserRepository;
import jakarta.transaction.Transactional;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;


@Service
public class UserInforService {
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private static final String UPLOAD_DIR = "uploads/"; // Local storage path
    

    // Using Construction to build passwordEncoder
    public UserInforService(UserRepository userRepository,
                            @Qualifier("userPasswordEncoder") PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
    }


    @Transactional
    public User registerUser(User user) {
        // 1. Check if the email address has been registered
        if (userRepository.findByUsername(user.getUsername()).isPresent()) {
            throw new DuplicateEmailException("The email has been registered!");
        }
        // 2. Make sure the password field passed by the front end is valid
        String rawPassword = user.getPlainTextPassword(); // Get plaintext password
        if (rawPassword == null || rawPassword.trim().isEmpty()) {
            throw new IllegalArgumentException("The password cannot be empty!");
        }
        // 3. Encrypt passwords and store them
        String encodePassword = passwordEncoder.encode(rawPassword);
        user.setPassword(encodePassword);
        // 4. Save the user to the database
        return userRepository.save(user);
    }


    public String uploadAvatar(MultipartFile file) {
        try {
            // 1. 上传文件
            Path uploadPath = Paths.get(UPLOAD_DIR);
            if (!Files.exists(uploadPath)) {
                Files.createDirectories(uploadPath);
            }

            String fileName = System.currentTimeMillis() + "_" + file.getOriginalFilename(); // 避免重复名
            Path filePath = uploadPath.resolve(fileName);
            Files.write(filePath, file.getBytes());

            String avatarUrl = "/uploads/" + fileName;

            // 2. 获取当前登录用户并更新 avatarUrl 字段
            String username = SecurityContextHolder.getContext().getAuthentication().getName();
            User user = userRepository.findByUsername(username).orElseThrow(() -> new RuntimeException("User not found"));
            user.setAvatarUrl(avatarUrl);
            userRepository.save(user);

            return avatarUrl;
        } catch (IOException e) {
            throw new RuntimeException("Failed to upload: " + e.getMessage());
        }
    }
    public User getUserByUsername(String username) {
        return userRepository.findByUsername(username)
            .orElseThrow(() -> new RuntimeException("User not found with username: " + username));
    }

    // Update user profile
    public User updateUserProfile(User user) {
        User existingUser = userRepository.findByUsername(user.getUsername()).orElseThrow(
            () -> new RuntimeException("The user does not exist.")
        );
        // Reset name
        existingUser.setName(user.getName());
        existingUser.setPhone(user.getPhone());
        return userRepository.save(existingUser);
    }
    public User updateUserProfile(User existingUser, String name, String phone) {
        existingUser.setName(name);
        if (phone != null && !phone.trim().isEmpty()) {
            try {
                existingUser.setPhone(Long.parseLong(phone));
            } catch (NumberFormatException e) {
                throw new IllegalArgumentException("Invalid phone number format.");
            }
        }
        return userRepository.save(existingUser);
    }

    
    // Change password
    public void changePassword(String email, String currentPassword, String newPassword) {
        User user = userRepository.findByUsername(email).orElseThrow(
            () -> new RuntimeException("The user does not exist.")
        );
        // Check current password
        if (!passwordEncoder.matches(currentPassword, user.getPassword())) {
            throw new InvalidPasswordException("Current password is incorrect.");
        }
        // New password (encrypted storage)
        user.setPassword(passwordEncoder.encode(newPassword));
        userRepository.save(user);
    }
}