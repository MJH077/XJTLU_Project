package com.cpt202.group21.service;
import java.util.Random;
import java.util.concurrent.TimeUnit;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;
import jakarta.mail.MessagingException;


@Service
public class VerificationCodeService {
    // The verification code is valid for 5 minutes
    private static final long CODE_EXPIRE_MINUTES = 5; 
    // The resend interval is 60 seconds
    private static final long RESEND_INTERVAL_SECONDS = 60; 
    private final StringRedisTemplate redisTemplate;
    private final EmailService emailService;
    private final Random random = new Random();


    public VerificationCodeService(StringRedisTemplate redisTemplate, EmailService emailService) {
        this.redisTemplate = redisTemplate;
        this.emailService = emailService;
    }


    public String generateVerificationCode() {
        // Generate a 6-bit random verification code
        return String.format("%06d", random.nextInt(1000000)); 
    }


    @SuppressWarnings("CallToPrintStackTrace")
    public boolean sendVerificationCode(String email) {
        if (email == null || email.isEmpty()) {
            return false; 
        }
        String redisKey = "verification_code:" + email;
        String lastSentTimeKey = "verification_last_sent:" + email;


        // First check whether the key exists
        Boolean exists = redisTemplate.hasKey(lastSentTimeKey);
        if (Boolean.TRUE.equals(exists)) {
            Long lastSentTime = redisTemplate.getExpire(lastSentTimeKey, TimeUnit.SECONDS);
            if (lastSentTime != null && lastSentTime > 0) {
                return false; 
            }
        }


        // Generate verification code
        String code = generateVerificationCode();
        redisTemplate.opsForValue().set(redisKey, code, CODE_EXPIRE_MINUTES, TimeUnit.MINUTES);
        redisTemplate.opsForValue().set(lastSentTimeKey, "1", RESEND_INTERVAL_SECONDS, TimeUnit.SECONDS);


        // Build message content
        String subject = "MusicRM Account Verification Code";
        String content = "<div style='font-family: Arial, sans-serif; line-height: 1.6;'>"
                + "<h2 style='color: #333;'>Dear user,</h2>"
                + "<p>Your verification code is: <strong style='font-size: 18px; color: #d9534f;'>" + code + "</strong></p>"
                + "<p>Please use this verification code within 5 minutes to complete your registration.</p>"
                + "<p>Thank you for using <strong>MusicRM</strong>!</p>"
                + "<br><p>Best regards,<br><strong>MusicRM Team</strong></p>"
                + "</div>";


        try {
            emailService.sendHtmlEmail(email, subject, content);
        } catch (MessagingException e) {
            e.printStackTrace();
            // If the sending fails, the Redis record is deleted to allow the user to request again
            redisTemplate.delete(redisKey);
            redisTemplate.delete(lastSentTimeKey);
            return false;
        }
        return true;
    }


    public boolean verifyCode(String email, String code) {
        if (email == null || email.isEmpty() || code == null || code.isEmpty()) {
            return false; 
        }
        String redisKey = "verification_code:" + email;
        String storedCode = redisTemplate.opsForValue().get(redisKey);
        if (storedCode != null && storedCode.equals(code)) {
            // After the verification succeeds, delete the verification code
            redisTemplate.delete(redisKey); 
            return true;
        }
        return false;
    }
}