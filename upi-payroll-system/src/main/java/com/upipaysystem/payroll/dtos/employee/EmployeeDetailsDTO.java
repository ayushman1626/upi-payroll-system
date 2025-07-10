package com.upipaysystem.payroll.dtos.employee;

import com.upipaysystem.payroll.dtos.organization.OrganizationSummaryDTO;
import com.upipaysystem.payroll.model.EmployeeDetails;
import com.upipaysystem.payroll.model.Organization;
import com.upipaysystem.payroll.model.User;
import jakarta.persistence.*;

import java.time.LocalDate;
import java.time.LocalDateTime;

public class EmployeeDetailsDTO {


    private Long id;
    private String fullName;
    private String email;
    private String phone;
    private String gender;
    private String designation;
    private Double salary;
    private String department;
    private LocalDate joiningDate;
    private User.Role role;
    private boolean active;
    private OrganizationSummaryDTO organization;
    private LocalDateTime createdAt;

    public EmployeeDetailsDTO(User user, EmployeeDetails employeeDetails){
        this.id = user.getId();
        this.fullName = user.getFullName();
        this.email = user.getEmail();
        this.gender = employeeDetails.getGender().toString();
        this.role = user.getRole();
        this.department = employeeDetails.getDepartment();
        this.designation = employeeDetails.getDesignation();
        this.joiningDate = employeeDetails.getJoiningDate();
        this.salary = employeeDetails.getSalary();
        this.active = user.isActive();
        this.organization = new OrganizationSummaryDTO(user.getOrganization());
        this.createdAt = user.getCreatedAt();
    }

    public OrganizationSummaryDTO getOrganization() {
        return organization;
    }

    public void setOrganization(OrganizationSummaryDTO organization) {
        this.organization = organization;
    }

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

    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    public String getDesignation() {
        return designation;
    }

    public void setDesignation(String designation) {
        this.designation = designation;
    }

    public Double getSalary() {
        return salary;
    }

    public void setSalary(Double salary) {
        this.salary = salary;
    }

    public String getDepartment() {
        return department;
    }

    public void setDepartment(String department) {
        this.department = department;
    }

    public LocalDate getJoiningDate() {
        return joiningDate;
    }

    public void setJoiningDate(LocalDate joiningDate) {
        this.joiningDate = joiningDate;
    }

    public User.Role getRole() {
        return role;
    }

    public void setRole(User.Role role) {
        this.role = role;
    }

    public boolean isActive() {
        return active;
    }

    public void setActive(boolean active) {
        this.active = active;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }
}
