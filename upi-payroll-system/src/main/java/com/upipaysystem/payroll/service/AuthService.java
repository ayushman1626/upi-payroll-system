package com.upipaysystem.payroll.service;

import com.upipaysystem.payroll.dtos.auth.LoginRequest;
import com.upipaysystem.payroll.dtos.auth.LoginResponse;
import com.upipaysystem.payroll.dtos.auth.RegisterRequest;
import com.upipaysystem.payroll.dtos.auth.RegistrationResponse;
import com.upipaysystem.payroll.model.Otp;
import com.upipaysystem.payroll.model.PendingUser;
import com.upipaysystem.payroll.model.User;
import com.upipaysystem.payroll.repo.OtpRepostory;
import com.upipaysystem.payroll.repo.PendingUserRepository;
import com.upipaysystem.payroll.repo.UserRepository;
import com.upipaysystem.payroll.utils.JwtUtil;
import com.upipaysystem.payroll.utils.OTPGenerator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.util.ResourceUtils;

import java.io.File;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

import static com.upipaysystem.payroll.model.User.Role.USER;


@Service
public class AuthService {

    private final UserRepository userRepository;
    private final PendingUserRepository pendingUserRepository;
    private final OTPGenerator otpGenerator;
    private final EmailService emailService;
    private final BCryptPasswordEncoder encoder;
    private final OtpRepostory otpRepo;
    private final AuthenticationManager authenticationManager;
    private final JwtUtil jwtUtil;

    @Autowired
    public AuthService(UserRepository userRepository, PendingUserRepository pendingUserRepository,
                       OTPGenerator otpGenerator, OtpRepostory otpRepo, EmailService emailService,
                       AuthenticationManager authenticationManager, JwtUtil jwtUtil) {
        this.userRepository = userRepository;
        this.pendingUserRepository = pendingUserRepository;
        this.otpGenerator = otpGenerator;
        this.otpRepo = otpRepo;
        this.emailService = emailService;
        this.encoder = new BCryptPasswordEncoder(12); // Initialize BCryptPasswordEncoder here
        this.authenticationManager = authenticationManager;
        this.jwtUtil = jwtUtil;
    }


    public RegistrationResponse registerUser(RegisterRequest request) {
        String email = request.getEmail().toLowerCase().trim();

        if (userRepository.existsByEmail(email)) {
            return new RegistrationResponse(email,"Email already registered. Login instead.", false);
        }

        PendingUser pendingUser = new PendingUser();
        pendingUser.setFullname(request.getFullName());
        pendingUser.setPassword(encoder.encode(request.getPassword()));
        pendingUser.setEmail(email);
        pendingUser.setCreatedAt(LocalDateTime.now());
        pendingUserRepository.save(pendingUser);

        String otp = otpGenerator.generateOTP();
        otpRepo.save(new Otp(otp,email,LocalDateTime.now().plusMinutes(5), Otp.Topic.REGISTRATION));

       
        // Sending OTP email
        try {

            String htmlContent = getOTPEmailTemplate(otp);
            emailService.sendEmail(email, "Verify Your Email Address", htmlContent);
            return new RegistrationResponse(email, "Registration successful. Verification email sent.", true);
        } catch (Exception e) {
            pendingUserRepository.delete(pendingUser);
            return new RegistrationResponse(email, "Registration failed. Could not send verification email.", false);
        }
    }



    private String getOTPEmailTemplate(String otp) throws RuntimeException {
        try{
            File templateFile = ResourceUtils.getFile("classpath:templates/otp_email_template.html");
            String htmlTemplate = new String(Files.readAllBytes(templateFile.toPath()), StandardCharsets.UTF_8);
            return htmlTemplate.replace("[[OTP]]", otp);
        } catch (IOException e) {
            throw new RuntimeException("Failed to load email template", e);
        }
    }


    public RegistrationResponse verifyOtp(String email, String otp) {
        Optional<Otp> optionalOtp = otpRepo.findByEmailAndOtp(email, otp);

        if (optionalOtp.isEmpty()) {
            return new RegistrationResponse(email, "Invalid OTP or email.", false);
        }

        Otp otpEntity = optionalOtp.get();

        if (LocalDateTime.now().isAfter(otpEntity.getExpiration())) {
            otpRepo.delete(otpEntity);
            return new RegistrationResponse(email, "OTP has expired.", false);
        }

        if (otpEntity.getOtp().equals(otp)) {
            // Fetch PendingUser
            Optional<PendingUser> optionalPendingUser = pendingUserRepository.findByEmail(email);

            if (optionalPendingUser.isEmpty()) {
                return new RegistrationResponse(email, "No pending registration found.", false);
            }
            PendingUser pendingUser = optionalPendingUser.get();

            // Move data to actual User entity
            User newUser = new User();
            newUser.setEmail(pendingUser.getEmail());
            newUser.setFullName(pendingUser.getFullname());
            newUser.setPassword(pendingUser.getPassword());
            newUser.setCreatedAt(LocalDateTime.now());
            newUser.setActive(false);
            newUser.setRole(USER);
            // Set role, company etc. if needed
            userRepository.save(newUser);

            // Clean up
            pendingUserRepository.delete(pendingUser);
            otpRepo.delete(otpEntity);

            return new RegistrationResponse(email, "Email verified and user registered successfully.", true);
        }

        return new RegistrationResponse(email, "OTP did not match.", false);
    }


    public LoginResponse loginUser(LoginRequest request) {

        User user = userRepository.findByEmail(request.getEmail()).orElseThrow(()->
                new BadCredentialsException("NO VERIFIED USER FOUND WITH THAT EMAIL")
        );

        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(request.getEmail(), request.getPassword())
        );

        SecurityContextHolder.getContext().setAuthentication(authentication);

        String token = jwtUtil.generateToken(user.getEmail());

        Map<String, Object> data = new HashMap<>();
        data.put("token",token);
        data.put("email", user.getEmail());
        data.put("fullName",user.getFullName());
        data.put("role",user.getRole().toString());

        return new LoginResponse(data,true,"Login successful");
    }
}

