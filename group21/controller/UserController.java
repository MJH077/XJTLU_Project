package com.cpt202.group21.controller;
import java.security.Principal;
import java.util.Map;
import java.util.stream.Collectors;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.csrf.CsrfToken;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;
import com.cpt202.group21.exception.DuplicateEmailException;
import com.cpt202.group21.model.User;
import com.cpt202.group21.request.VerificationRequest;
import com.cpt202.group21.service.UserInforService;
import com.cpt202.group21.service.VerificationCodeService;
import com.cpt202.group21.util.ResponseUtils;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import org.springframework.stereotype.Controller;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.ui.Model;

@Controller
public class UserController {
    private final UserInforService userInforService;
    private final VerificationCodeService verificationCodeService;
    

    public UserController(UserInforService userInforService, VerificationCodeService verificationCodeService) {
        this.userInforService = userInforService;
        this.verificationCodeService = verificationCodeService;
    }


    // User register
    @PostMapping("/userRegister/registerUser")
    public ResponseEntity<?> registerUser(@Valid @RequestBody VerificationRequest request, BindingResult result) {
        // 1. Check requested data
        if (result.hasErrors()) {
            Map<String, String> errors = result.getFieldErrors().stream()
                    .collect(Collectors.toMap(
                            FieldError::getField,
                            error -> error.getDefaultMessage() != null ? error.getDefaultMessage() : "Invalid value."
                    ));
            return ResponseEntity.badRequest().body(errors);
        }
    
        // 2. Verify that the verification code is correct
        if (!verificationCodeService.verifyCode(request.getUsername(), request.getVerificationCode())) {
            return ResponseEntity.badRequest()
                    .body(ResponseUtils.createResponse("error", "Invalid or expired verification code."));
        }
    
        // 3. Create user objects
        try {
            User user = new User();
            user.setEmail(request.getUsername()); 
            user.setUsername(request.getUsername());
            user.setPlainTextPassword(request.getPassword());
            user.setName(request.getUsername());
    
            // 4. Call registration logic
            User registeredUser = userInforService.registerUser(user);
    
            // 5. Return success message
            Map<String, Object> response = ResponseUtils.createResponse("message", "Registered successfully!");
            response.put("username", registeredUser.getUsername());
            return ResponseEntity.ok(response);
        } catch (DuplicateEmailException ex) {
            return ResponseEntity.status(HttpStatus.CONFLICT)
                    .body(ResponseUtils.createResponse("error", ex.getMessage()));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(ResponseUtils.createResponse("error", "Failed to register, please try again later."));
        }
    }


    // Upload avatar
    @PostMapping("/userInforManagement/uploadAvatar")
    @ResponseBody
    public ResponseEntity<?> uploadAvatar(@RequestParam("avatar") MultipartFile file, Principal principal) {
        if (file.isEmpty()) {
            return ResponseEntity.badRequest().body(ResponseUtils.createResponse("error", "Please upload a valid avatar."));
        }
        try {
            // The Service layer is invoked to process the profile picture upload
            String fileUrl = userInforService.uploadAvatar(file); 
            return ResponseEntity.ok(ResponseUtils.createResponse("message", "Avatar uploaded successfully!", "url", fileUrl));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(ResponseUtils.createResponse("error", "Failed to upload avatar, please try again later."));
        }
    }



    @GetMapping("/userHome")
    public String showUserHomePage(Model model) {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        Object principal = auth.getPrincipal();
    
        String username;
    
        if (principal instanceof User) {
            username = ((User) principal).getUsername();
        } else if (principal instanceof org.springframework.security.core.userdetails.User) {
            username = ((org.springframework.security.core.userdetails.User) principal).getUsername();
        } else {
            throw new RuntimeException("Unknown principal type: " + principal.getClass().getName());
        }
    
        User user = userInforService.getUserByUsername(username);
    
        model.addAttribute("userId", user.getId());
        model.addAttribute("userName", user.getName());
        model.addAttribute("userEmail", user.getEmail());
        model.addAttribute("userPhone", user.getPhone());
        model.addAttribute("avatarUrl", user.getAvatarUrl());
        model.addAttribute("user", user);
        return "UserHome";
    }

    // Change password
    @PostMapping("/userInforManagement/changePassword")
    public ResponseEntity<?> changePassword(
        @RequestParam("username") String username,  
        @RequestParam("currentPassword") String currentPassword,
        @RequestParam("newPassword") String newPassword) {
        try {
            userInforService.changePassword(username, currentPassword, newPassword);
            return ResponseEntity.ok(ResponseUtils.createResponse("message", "Password changed successfully!"));
        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(ResponseUtils.createResponse("error", "Parameter error: " + e.getMessage()));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(ResponseUtils.createResponse("error", "Failed to change password, please try again later."));
        }
    }
    @PostMapping("/userInforManagement/updateBasicInfo")
    public ResponseEntity<?> updateBasicInfo(
        @RequestParam("name") String name,
        @RequestParam("phone") String phone) {

        try {
            Authentication auth = SecurityContextHolder.getContext().getAuthentication();
            String currentUsername = auth.getName();

            User existingUser = userInforService.getUserByUsername(currentUsername);
            if (existingUser == null) {
                return ResponseEntity.badRequest()
                        .body(ResponseUtils.createResponse("error", "User not found."));
            }

            // 调用服务层更新基本信息
            User updatedUser = userInforService.updateUserProfile(existingUser, name, phone);

            // 更新当前认证信息
            UsernamePasswordAuthenticationToken newAuth =
                    new UsernamePasswordAuthenticationToken(updatedUser, auth.getCredentials(), auth.getAuthorities());
            SecurityContextHolder.getContext().setAuthentication(newAuth);

            return ResponseEntity.ok(ResponseUtils.createResponse("message", "Basic info updated successfully!"));

        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(ResponseUtils.createResponse("error", "Invalid input: " + e.getMessage()));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(ResponseUtils.createResponse("error", "Failed to update info. Please try again later."));
        }
    }
    
    // Register, login and lock the account
    @GetMapping("/userRegister")
    public String showUserRegistrationPage() {
        return "UserRegister"; 
    }
    @GetMapping("/userLogin")
    public String showUserLoginPage(HttpServletRequest request, Model model) {
        CsrfToken csrfToken = (CsrfToken) request.getAttribute(CsrfToken.class.getName());
        model.addAttribute("_csrf", csrfToken);
        return "UserLogin"; // 对应 userLogin.html（在 templates 下）
    }
    @GetMapping("/userInformationManagement/showUserInformationManagementPage")
    public String showUserInformationManagementPage() {
        return "UserInforManagement"; 
    }
    @GetMapping("/userDashboard")
    public String showDashboard() {
        return "UserDashboard";
    }

    @GetMapping("/userLocked")
    public String showLockedPage() {
        return "UserLock";
    }

}