package com.upipaysystem.payroll.controller;

import com.upipaysystem.payroll.dtos.common.ApiResponse;
import com.upipaysystem.payroll.dtos.organization.OrganizationSummaryDTO;
import com.upipaysystem.payroll.dtos.organization.OrganizationRegisterRequest;
import com.upipaysystem.payroll.dtos.organization.OrganizationDetailsDTO;
import com.upipaysystem.payroll.model.Organization;
import com.upipaysystem.payroll.model.UserPrinciple;
import com.upipaysystem.payroll.service.OrganizationService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;

@RestController
@RequestMapping("api/organization")
public class OrganizationController {

    OrganizationService organizationService;
    public OrganizationController(OrganizationService organizationService){
        this.organizationService = organizationService;
    }

    @PostMapping("/register")
    public ResponseEntity<ApiResponse<OrganizationSummaryDTO>> registerOrganization(
            @Valid @RequestBody OrganizationRegisterRequest request,
            @AuthenticationPrincipal UserPrinciple userPrinciple
            ){

        if(userPrinciple == null){
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                    .body(new ApiResponse<>(false, "User not Authenticated",null));
        }

        OrganizationSummaryDTO organizationDTO = organizationService.registerOrganization(request,userPrinciple);

        return ResponseEntity.ok(
                new ApiResponse<>(true,"Organization Registered Succesfully",organizationDTO)
        );
    }



    @GetMapping("/me")
    @PreAuthorize("hasAuthority('ADMIN')")
    public ResponseEntity<ApiResponse<OrganizationDetailsDTO>> getOrganization(
            @AuthenticationPrincipal UserPrinciple userPrinciple
    ){
        if(userPrinciple == null){
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                    .body(new ApiResponse<>(false, "User not Authenticated",null));
        }

        OrganizationDetailsDTO organizationDetailsDTO = organizationService.getOrganization(userPrinciple);

        return ResponseEntity.ok(new ApiResponse<>(true,"",organizationDetailsDTO));
    }
}
