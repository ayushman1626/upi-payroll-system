package com.upipaysystem.payroll.controller;

import com.upipaysystem.payroll.dtos.auth.LoginRequest;
import com.upipaysystem.payroll.dtos.auth.LoginResponse;
import com.upipaysystem.payroll.dtos.auth.RegisterRequest;
import com.upipaysystem.payroll.dtos.auth.RegistrationResponse;
import com.upipaysystem.payroll.service.AuthService;
import com.upipaysystem.payroll.dtos.common.ApiResponse;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/api/auth")
public class AuthController {

    @Autowired
    private AuthService authService;

    @PostMapping("/register")
    public ResponseEntity<ApiResponse<Map<String, Object>>> registerUser(
            @Valid @RequestBody RegisterRequest request) {

        //passing to service
        RegistrationResponse response = authService.registerUser(request);

        //response from service
        HttpStatus status = response.isSuccess() ? HttpStatus.CREATED : HttpStatus.CONFLICT;

        //api-response
        if (!response.isSuccess()) {
            return ResponseEntity.status(status).body(
                    new ApiResponse<>(false, response.getMessage(), null));
        } else {
            Map<String, Object> responseData = new HashMap<>();
            responseData.put("email", response.getEmail());
            responseData.put("otpExpiresInSeconds", 600);

            return ResponseEntity.status(status).body(
                    new ApiResponse<>(true, response.getMessage(), responseData));
        }
    }

    @PostMapping("register/verify-otp")
    ResponseEntity<ApiResponse<Map<String, Object>>> verifyOtp(@RequestBody Map<String, String> input){

        RegistrationResponse response = authService.verifyOtp(input.get("email"), input.get("otp"));

        HttpStatus status = response.isSuccess() ? HttpStatus.OK : HttpStatus.BAD_REQUEST;

        if (!response.isSuccess()) {
            return ResponseEntity.status(status).body(
                    new ApiResponse<>(false, response.getMessage(), null));
        } else {
            Map<String, Object> responseData = new HashMap<>();
            responseData.put("email", response.getEmail());

            return ResponseEntity.status(status).body(
                    new ApiResponse<>(true, response.getMessage(), responseData));
        }
    }

    @PostMapping("login")
    ResponseEntity<ApiResponse<Map<String, Object>>> login(
                @Valid @RequestBody LoginRequest request){
        LoginResponse response;
        response = authService.loginUser(request);

        return ResponseEntity.status(HttpStatus.OK).body(
                new ApiResponse<>(true, response.getMessage(), response.getData()));
    }


}
