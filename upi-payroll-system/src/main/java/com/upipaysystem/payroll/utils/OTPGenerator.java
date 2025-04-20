package com.upipaysystem.payroll.utils;

import com.upipaysystem.payroll.model.Otp;
import com.upipaysystem.payroll.repo.OtpRepostory;
import org.springframework.stereotype.Component;

import java.security.SecureRandom;
import java.util.Optional;
import java.util.Random;

@Component
public class OTPGenerator {

    private OtpRepostory otpRepo;

    public OTPGenerator(OtpRepostory otpRepo){
        this.otpRepo = otpRepo;
    }

    private static final String OTP_CHARS = "0123456789";
    private static final int OTP_LENGTH = 6; // You can configure the length

    private static final Random random = new SecureRandom();

    public String generateOTP() {
        StringBuilder otp = new StringBuilder(OTP_LENGTH);
        for (int i = 0; i < OTP_LENGTH; i++) {
            int randomIndex = random.nextInt(OTP_CHARS.length());
            otp.append(OTP_CHARS.charAt(randomIndex));
        }
        return otp.toString();
    }

    public boolean verifyOTP(String email, String otp) {
        Optional<Otp> otp_object = otpRepo.findByEmailAndOtp(email.toLowerCase().trim(), otp);
        if(!otp_object.isPresent()) {
            return false;
        }else{
            return otp_object.get().getOtp().equals(otp);
        }
    }
}
