package com.upipaysystem.payroll.controller;


import com.upipaysystem.payroll.dtos.auth.RegistrationResponse;
import com.upipaysystem.payroll.dtos.common.ApiResponse;
import com.upipaysystem.payroll.dtos.employee.*;
import com.upipaysystem.payroll.model.EmployeePaymentAccount;
import com.upipaysystem.payroll.model.UserPrinciple;
import com.upipaysystem.payroll.service.EmployeeService;

import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("api/employee")
@PreAuthorize("hasAuthority('ADMIN')")
public class EmployeeController {

    EmployeeService employeeService;

    public EmployeeController(EmployeeService employeeService){
        this.employeeService =employeeService;
    }

    @PostMapping("/register")
    public ResponseEntity<ApiResponse<RegistrationResponse>> registerEmployee(
            @Valid @RequestBody EmployeeRegisterRequest request,
            @AuthenticationPrincipal UserPrinciple userPrinciple
    ) {
        if (userPrinciple == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                    .body(new ApiResponse<>(false, "User not authenticated.", null));
        }

        RegistrationResponse registrationResponse = employeeService.registerEmployee(request, userPrinciple);

        return ResponseEntity.ok(new ApiResponse<>(true, "Employee registration initiated.", registrationResponse));
    }

    @PreAuthorize("permitAll()") // Override class-level authorization
    @GetMapping("/verify-email")
    public ResponseEntity<ApiResponse<EmployeeDTO>> verifyEmployee(@RequestParam("token") String token) {
        EmployeeDTO employeeDTO = employeeService.verifyEmployee(token);
        return ResponseEntity.ok(new ApiResponse<>(true, "User registered successfully", employeeDTO));
    }

    @GetMapping("/get-all")
    public ResponseEntity<ApiResponse<List<EmployeeDetailsDTO>>> getAllEmployee(
            @AuthenticationPrincipal UserPrinciple userPrinciple
    ){
        if (userPrinciple == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                    .body(new ApiResponse<>(false, "User not authenticated.", null));
        }
        List<EmployeeDetailsDTO> employees = employeeService.getAllEmployee(userPrinciple);

        return ResponseEntity.ok(
                new ApiResponse<>(true,"All employees fetched successfully",employees)
        );
    }

//    @PostMapping("/account-details")
//    @PreAuthorize("hasAuthority('EMPLOYEE')")
//    public ResponseEntity<ApiResponse<EmployeePaymentAccountDto>> addAccountDetails(
//            @AuthenticationPrincipal UserPrinciple userPrinciple,
//            @Valid @RequestBody EmployeeAccountRequest request
//    ){
//        if (userPrinciple == null) {
//            return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
//                    .body(new ApiResponse<>(false, "User not authenticated.", null));
//        }
//        EmployeePaymentAccountDto employeePaymentAccountDto =
//                employeeService.setPaymentAccount(userPrinciple,request);
//    }

}


