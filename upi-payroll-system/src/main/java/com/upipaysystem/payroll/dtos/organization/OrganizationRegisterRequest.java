package com.upipaysystem.payroll.dtos.organization;

import jakarta.validation.Valid;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;

public class OrganizationRegisterRequest {
    @NotBlank(message = "Organization name is required")
    private String name;

    @NotBlank(message = "Location is required")
    private String organizationLocation;

    @NotBlank(message = "Industry is required")
    private String industry;

    @NotBlank(message = "GST Number is required")
    @Pattern(
            regexp = "\\d{2}[A-Z]{5}\\d{4}[A-Z]{1}[1-9A-Z]{1}Z[0-9A-Z]{1}",
            message = "Invalid GST number format"
    )
    private String gstNumber;

    @NotBlank(message = "Registered email is required")
    @Email(message = "Invalid email format")
    private String registeredEmail;

    @Valid
    private AddressDTO address;

    public @NotBlank(message = "Organization name is required") String getName() {
        return name;
    }

    public void setName(@NotBlank(message = "Organization name is required") String name) {
        this.name = name;
    }

    public @NotBlank(message = "Location is required") String getOrganizationLocation() {
        return organizationLocation;
    }

    public void setOrganizationLocation(@NotBlank(message = "Location is required") String organizationLocation) {
        this.organizationLocation = organizationLocation;
    }

    public @NotBlank(message = "Industry is required") String getIndustry() {
        return industry;
    }

    public void setIndustry(@NotBlank(message = "Industry is required") String industry) {
        this.industry = industry;
    }

    public @NotBlank(message = "GST Number is required") @Pattern(
            regexp = "\\d{2}[A-Z]{5}\\d{4}[A-Z]{1}[1-9A-Z]{1}Z[0-9A-Z]{1}",
            message = "Invalid GST number format"
    ) String getGstNumber() {
        return gstNumber;
    }

    public void setGstNumber(@NotBlank(message = "GST Number is required") @Pattern(
            regexp = "\\d{2}[A-Z]{5}\\d{4}[A-Z]{1}[1-9A-Z]{1}Z[0-9A-Z]{1}",
            message = "Invalid GST number format"
    ) String gstNumber) {
        this.gstNumber = gstNumber;
    }

    public @NotBlank(message = "Registered email is required") @Email(message = "Invalid email format") String getRegisteredEmail() {
        return registeredEmail;
    }

    public void setRegisteredEmail(@NotBlank(message = "Registered email is required") @Email(message = "Invalid email format") String registeredEmail) {
        this.registeredEmail = registeredEmail;
    }

    public @Valid AddressDTO getAddress() {
        return address;
    }

    public void setAddress(@Valid AddressDTO address) {
        this.address = address;
    }
}
