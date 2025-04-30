package com.upipaysystem.payroll.dtos.common;

import com.upipaysystem.payroll.dtos.employee.EmployeeDetailsDTO;
import com.upipaysystem.payroll.dtos.organization.OrganizationSummaryDTO;
import com.upipaysystem.payroll.model.User;
import lombok.Data;

@Data
public class UserDTO {

    private Long id;
    private String fullName;
    private String email;
    private String phone;
    private String role;
    private boolean active;
    private OrganizationSummaryDTO organization; // Use a flat DTO instead of entity

    public UserDTO(User user) {
        this.id = user.getId();
        this.fullName = user.getFullName();
        this.email = user.getEmail();
        this.phone = user.getPhone();
        this.role = user.getRole().toString();
        this.active = user.isActive();
        this.organization = user.getOrganization() != null ? new OrganizationSummaryDTO(user.getOrganization()) : null;
    }

    public UserDTO() {}

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getFullName() {
        return fullName;
    }

    public void setFullName(String fullName) {
        this.fullName = fullName;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getRole() {
        return role;
    }

    public void setRole(String role) {
        this.role = role;
    }

    public boolean isActive() {
        return active;
    }

    public void setActive(boolean active) {
        this.active = active;
    }

    public OrganizationSummaryDTO getOrganization() {
        return organization;
    }

    public void setOrganization(OrganizationSummaryDTO organization) {
        this.organization = organization;
    }
}
