package com.cpt202.group21.controller;
import java.util.Map;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.cpt202.group21.request.EmailRequest;
import com.cpt202.group21.request.VerificationRequest;
import com.cpt202.group21.service.VerificationCodeService;
import jakarta.validation.Valid;


@RestController 
@RequestMapping("/api/verification") 
@Validated  
public class VerificationCodeController {
    private final VerificationCodeService verificationCodeService;

    public VerificationCodeController(VerificationCodeService verificationCodeService) {
        this.verificationCodeService = verificationCodeService;
    }

    @PostMapping("/send")
    public ResponseEntity<Map<String, String>> sendVerificationCode(@Valid @RequestBody EmailRequest request) {
        boolean success = verificationCodeService.sendVerificationCode(request.getEmail());
        return success
                ? ResponseEntity.ok(Map.of("message", "The verification code has been sent!"))
                : ResponseEntity.badRequest().body(Map.of("error", "Please try again later."));
    }

    @PostMapping("/verify")
    public ResponseEntity<Map<String, String>> verifyCode(@Valid @RequestBody VerificationRequest request) {
        boolean valid = verificationCodeService.verifyCode(request.getUsername(), request.getVerificationCode());
        return valid
                ? ResponseEntity.ok(Map.of("message", "The verification code is correct!"))
                : ResponseEntity.badRequest().body(Map.of("error", "The verification code is incorrect or has expired."));
    }
}